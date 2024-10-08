package brickbreaker;

import java.io.*;

public class GameData {
    // File name to store game data
    private static final String FILENAME = "game_data.txt";

    // Method to save player's score to a file
    public static void saveScore(String playerName, int score) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILENAME))) {
            // Write player's name and score separated by a comma to the file
            writer.write(playerName + "," + score);
        } catch (IOException e) {
            // Print stack trace in case of an error while writing to the file
            e.printStackTrace();
        }
    }

    // Method to load best score from the file
    public static int loadBestScore() {
        int bestScore = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(FILENAME))) {
            // Read the first line from the file
            String line = reader.readLine();
            // If the line is not null and not empty, parse it to get the best score
            if (line != null && !line.isEmpty()) {
                String[] parts = line.split(",");
                bestScore = Integer.parseInt(parts[1]);
            }
        } catch (IOException | NumberFormatException e) {
            // Print stack trace in case of an error while reading or parsing the file
            e.printStackTrace();
        }
        // Return the best score
        return bestScore;
    }

    // Method to update and return the best score
    public static int updateBestScore(int newScore) {
        // Load the current best score from the file
        int currentBestScore = loadBestScore();
        // If the new score is greater than the current best score
        if (newScore > currentBestScore) {
            // Save the new score as the best score by overwriting the previous score in the file
            saveScore("", newScore); // Empty string to overwrite previous best score
            // Return the new score
            return newScore;
        }
        // If the new score is not greater, return the current best score
        return currentBestScore;
    }
}
