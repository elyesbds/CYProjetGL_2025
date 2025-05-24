package app;

import neural.*;
import map.*;
import entities.*;
import engine.GameEngine;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * The {@code AppWithAI} class is the entry point for running the Pac-Man game
 * with a neural network-controlled AI player.
 *
 * <p>It loads a pre-trained neural network from file {@code n2.ser}, initializes
 * the game map, and replaces the default player with an instance of {@link PlayerAI}.
 * The game is then launched inside a Swing window.</p>
 *
 * <p>The neural network controls Pac-Man's decisions during gameplay based on
 * environmental inputs such as food and ghost positions.</p>
 */
public class AppWithAI {

    /**
     * Main method to launch the game using the AI-controlled Pac-Man.
     *
     * @param args command-line arguments (not used)
     * @throws ClassNotFoundException if the neural network class cannot be deserialized
     * @throws IOException            if the file containing the neural network cannot be read
     */
    public static void main(String[] args) throws ClassNotFoundException, IOException {
        // Load the neural network from file
        NeuralNet net = NeuralNet.load(new File("n2.ser"));
        if (net == null) {
            System.err.println("Error: Neural network not loaded (file n2.ser not found)");
            return;
        }

        // Screen setup
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double screenUsage = 0.8; // Percentage of the screen to use for the game window
        int maxWidth = (int) (screenSize.width * screenUsage);
        int maxHeight = (int) (screenSize.height * screenUsage);

        // Determine appropriate tile size
        int rows = 21; // Number of rows in the game grid
        int cols = 19; // Number of columns in the game grid
        int tileSize = Math.min(maxWidth / cols, maxHeight / rows);
        while (tileSize % 10 != 0) tileSize--; // Ensure tile size is a multiple of 10

        // Create the application window
        JFrame frame = new JFrame("Pacman AI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        // Initialize the game map
        Map pacmanMap = new Map(tileSize);
        Player original = pacmanMap.getPacman();

        // Create an AI-controlled player using the loaded neural network
        PlayerAI aiPlayer = new PlayerAI(
            original.getImage(),
            original.getX(),
            original.getY(),
            original.getWidth(),
            original.getHeight(),
            pacmanMap,
            net
        );
        pacmanMap.setPacman(aiPlayer);

        // Initialize the game engine with an AI tick hook
        GameEngine engine = new GameEngine(pacmanMap, true) {
            /**
             * Overrides the game tick to include AI-specific logic.
             */
            @Override
            protected void gameTick() {
                // Hook before default tick logic
                if (pacmanMap.getPacman() instanceof PlayerAI) {
                    PlayerAI ai = (PlayerAI) pacmanMap.getPacman();
                    // AI decision-making occurs in PlayerAI.move()
                }
                super.gameTick(); // Execute normal tick: movement, collisions, etc.
            }
        };

        // Add the map to the window and display it
        frame.add(pacmanMap);
        frame.pack(); // Adjust the window size to fit its content
        frame.setLocationRelativeTo(null); // Center the window on the screen
        pacmanMap.requestFocus(); // Ensure the map has focus for input
        frame.setVisible(true); // Make the window visible
    }
}
