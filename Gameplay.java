package brickbreaker;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Gameplay extends JPanel implements KeyListener, ActionListener {
    // Game state variables
    private boolean play = false; // Flag to indicate whether the game is in progress
    private int score = 0; // Current score of the player
    private int bestScore; // Best score achieved by the player
    private String playerName; // Player's name

    // Game elements variables
    private int totalBricks = 21; // Total number of bricks in the game
    private Timer timer; // Timer for game animation
    private int delay = 8; // Delay for timer
    private int playerX = 310; // Initial position of the player's paddle
    private int ballposX = 120; // Initial X position of the ball
    private int ballposY = 350; // Initial Y position of the ball
    private int ballXdir = -1; // Initial X direction of the ball
    private int ballYdir = -2; // Initial Y direction of the ball

    private MapGenerator map; // Object to generate the game map

    // Constructor
    public Gameplay() {
        // Ask for player's name and load best score from previous games
        playerName = askPlayerName();
        bestScore = GameData.loadBestScore();
        
        // Initialise game elements
        map = new MapGenerator(3, 7);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();
    }

    // Method to prompt the player to enter their name
    public String askPlayerName() {
        return JOptionPane.showInputDialog("Enter your name:");
    }

    // Method to paint the game elements on the screen
    public void paint(Graphics g) {
        // Drawing the background
        g.setColor(new Color(0, 128, 0)); // Green
        g.fillRect(1, 1, 692, 592 / 3);
        g.setColor(Color.white);
        g.fillRect(1, 592 / 3, 692, 592 / 3);
        g.setColor(new Color(255, 165, 0)); // Orange
        g.fillRect(1, 2 * (592 / 3), 692, 592 / 3);

        // Drawing game elements
        map.draw((Graphics2D) g);

        // Displaying scores
        g.setColor(Color.white);
        g.setFont(new Font("serif", Font.BOLD, 25));
        g.drawString("Score: " + score, 590, 30);
        g.setFont(new Font("serif", Font.BOLD, 20));
        g.drawString("Best Score: " + bestScore, 10, 30);

        // Drawing borders
        g.setColor(Color.yellow);
        g.fillRect(0, 0, 3, 592);
        g.fillRect(0, 0, 692, 3);
        g.fillRect(691, 0, 3, 592);

        // Drawing the paddle
        g.setColor(Color.green);
        g.fillRect(playerX, 550, 100, 8);

        // Drawing the ball
        g.setColor(Color.black);
        g.fillOval(ballposX, ballposY, 20, 20);

        // Displaying winning message if all bricks are destroyed
        if (totalBricks <= 0) {
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            g.setColor(Color.red);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("You Won :  ", 190, 300);
            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Press Enter to Restart ", 230, 350);

            // Store score and update best score
            GameData.saveScore(playerName, score);
            bestScore = GameData.updateBestScore(score);
        }

        // Displaying game over message if the ball falls below the paddle
        if (ballposY > 570) {
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            g.setColor(Color.red);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("Game Over, Scores: ", 190, 300);
            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Press Enter to Restart ", 230, 350);

            // Store score and update best score
            GameData.saveScore(playerName, score);
            bestScore = GameData.updateBestScore(score);
        }

        // Displaying player's name
        g.setFont(new Font("serif", Font.BOLD, 20));
        FontMetrics fm = g.getFontMetrics();
        int playerNameWidth = fm.stringWidth(playerName);
        g.drawString(playerName, (getWidth() - playerNameWidth) / 2, 30);

        g.dispose();
    }

    @Override
    // Action performed when timer ticks
    public void actionPerformed(ActionEvent e) {
        timer.start();
        if (play) {
            // Detecting collision with paddle
            if (new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX, 550, 100, 8))) {
                ballYdir = -ballYdir;
            }

            // Handling collision with bricks
            A: for (int i = 0; i < map.map.length; i++) {
                for (int j = 0; j < map.map[0].length; j++) {
                    if (map.map[i][j] > 0) {
                        int brickX = j * map.brickWidth + 80;
                        int brickY = i * map.brickHeight + 50;
                        int brickWidth = map.brickWidth;
                        int brickHeight = map.brickHeight;

                        Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                        Rectangle ballRect = new Rectangle(ballposX, ballposY, 20, 20);
                        Rectangle brickRect = rect;

                        if (ballRect.intersects(brickRect)) {
                            map.setBrickValue(0, i, j);
                            totalBricks--;
                            score += 5;

                            if (ballposX + 19 <= brickRect.x || ballposX + 1 >= brickRect.x + brickRect.width) {
                                ballXdir = -ballXdir;
                            } else {
                                ballYdir = -ballYdir;
                            }
                            break A;
                        }
                    }
                }
            }

            // Moving the ball
            ballposX += ballXdir;
            ballposY += ballYdir;
            if (ballposX < 0) {
                ballXdir = -ballXdir;
            }
            if (ballposY < 0) {
                ballYdir = -ballYdir;
            }
            if (ballposX > 670) {
                ballXdir = -ballXdir;
            }
        }

        // Repainting the panel
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    // Handling key presses
    public void keyPressed(KeyEvent e) {
        // Moving the paddle left and right
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (playerX >= 600) {
                playerX = 600;
            } else {
                moveRight();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (playerX < 10) {
                playerX = 10;
            } else {
                moveLeft();
            }
        }
        // Restarting the game when Enter is pressed
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!play) {
                play = true;
                ballposX = 120;
                ballposY = 350;
                ballXdir = -1;
                ballYdir = -2;
                playerX = 310;
                score = 0;
                totalBricks = 21;
                map = new MapGenerator(3, 7);
                repaint();
            }
        }
    }

    // Method to move the paddle to the right
    private void moveRight() {
        play = true;
        playerX += 20;
    }

    // Method to move the paddle to the left
    private void moveLeft() {
        play = true;
        playerX -= 20;
    }
}
