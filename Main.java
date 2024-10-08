package brickbreaker;

import javax.swing.JFrame;

public class Main {

    public static void main(String[] args) {
        // Create a new JFrame object to represent the game window
        JFrame obj = new JFrame();
        
        // Create a new Gameplay object, which will handle the game mechanics
        Gameplay gamePlay = new Gameplay();

        // Set the size and position of the game window
        obj.setBounds(10, 10, 700, 600);

        // Set the title of the game window
        obj.setTitle("Breakout Game");

        // Disable window resizing to maintain game layout
        obj.setResizable(false);

        // Make the game window visible
        obj.setVisible(true);

        // Specify what happens when the user closes the window
        obj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Add the Gameplay object to the game window
        obj.add(gamePlay);
    }
}
