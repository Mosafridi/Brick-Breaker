package brickbreaker;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class MapGenerator {
    // Attributes to store the map grid and brick dimensions
    public int map[][];
    public int brickWidth;
    public int brickHeight;

    // Constructor to initialise the map grid and calculate brick dimensions
    public MapGenerator(int row, int col) {
        map = new int[row][col];
        // Initialise all grid elements with value 1 (indicating brick exists)
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                map[i][j] = 1;
            }
        }

        // Calculate dimensions of each brick based on the number of rows and columns
        brickWidth = 540 / col;
        brickHeight = 150 / row;
    }

    // Method to draw the map (bricks) on the screen
    public void draw(Graphics2D g) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] > 0) {
                    // Choose a colour based on the row number for a gradient effect
                    Color brickColor = new Color(100, 100 + i * 25, 255 - i * 25);
                    g.setColor(brickColor);

                    // Add padding to bricks for better appearance
                    int padding = 5;
                    int x = j * brickWidth + 80 + padding;
                    int y = i * brickHeight + 50 + padding;
                    int width = brickWidth - 2 * padding;
                    int height = brickHeight - 2 * padding;

                    // Draw and fill the brick rectangle
                    g.fillRect(x, y, width, height);

                    // Draw the border of the brick
                    g.setStroke(new BasicStroke(3));
                    g.setColor(Color.black);
                    g.drawRect(x, y, width, height);
                }
            }
        }
    }

    // Method to set the value of a specific brick in the map grid
    public void setBrickValue(int value, int row, int col) {
        map[row][col] = value;
    }
}
