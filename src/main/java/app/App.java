package app;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import engine.GameEngine;
import map.Map;
import menu.EndScreen;
import menu.MainMenu;
import menu.SoundPlayer;


/**
 * Main application class for the Pac-Man game that handles game initialization,
 * UI management, and application lifecycle.
 * 
 * <p>The App class serves as the entry point and coordinator for the Pac-Man game
 * application. It manages the following core functionalities:
 * <ul>
 * <li>Application startup and main menu display</li>
 * <li>Game window creation and configuration</li>
 * <li>Game engine initialization and control</li>
 * <li>Pause/resume functionality with modal dialog</li>
 * <li>Score persistence and high score management</li>
 * <li>Game state monitoring and end screen transitions</li>
 * </ul>
 * 
 * <p>The application follows a standard Swing-based architecture with proper
 * thread management for game monitoring and UI responsiveness. The class
 * integrates various game components including the map, game engine, and
 * menu system to provide a complete gaming experience.
 * 
 * @author Equipe 1
 * @version 6.0
 * @since 1.0
 */
public class App {

    /**
     * Application entry point that initializes and displays the main menu.
     * 
     * <p>This method launches the application by creating and displaying the
     * main menu on the Event Dispatch Thread (EDT) to ensure proper Swing
     * threading. The main menu serves as the starting point for all game
     * interactions and navigation.
     * 
     * @param args command line arguments (currently unused)
     */
    public static void main(String[] args) {
        // Au lancement, on ouvre le menu principal
        SwingUtilities.invokeLater(() -> {
            new MainMenu().setVisible(true);
        });
    }

    /**
     * Initializes and starts a new game session with the specified player name.
     * 
     * <p>This method handles the complete game setup process including:
     * <ol>
     * <li>Window creation and configuration with proper sizing</li>
     * <li>Game map initialization with specified dimensions</li>
     * <li>UI component setup including pause functionality</li>
     * <li>Game engine initialization and event handling</li>
     * <li>Background thread creation for game state monitoring</li>
     * <li>Score persistence and end screen management</li>
     * </ol>
     * 
     * <p>The game window is configured with a standard Pac-Man layout of 21 rows
     * by 19 columns with 36-pixel tiles, providing the classic game experience.
     * A pause button is integrated into the top panel for game control.
     * 
     * <p>The method creates a monitoring thread that continuously checks for
     * game completion, handles score saving to persistent storage, and manages
     * the transition to the end screen when the game concludes.
     * 
     * @param pseudo the player's chosen username for score tracking and display
     * @throws RuntimeException if game initialization fails or required resources are unavailable
     * 
     * @see GameEngine
     * @see Map
     * @see PauseDialog
     * @see EndScreen
     */
    public static void lancerJeu(String pseudo) {

        SoundPlayer music = new SoundPlayer("src/main/resources/music.wav");
        music.playLoop();

        int rows = 21;
        int cols = 19;
        int tileSize = 36;

        int width = cols * tileSize;
        int height = rows * tileSize;


        JFrame frame = new JFrame("Pacman");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, height + 50); // espace en plus pour le bouton pause
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        Map pacmanMap = new Map(tileSize);
        frame.add(pacmanMap, BorderLayout.CENTER);

        JButton pauseButton = new JButton("Pause");
        pauseButton.setFont(new Font("Monospaced", Font.BOLD, 16));
        pauseButton.setBackground(new Color(255, 204, 0));
        pauseButton.setForeground(Color.BLACK);
        pauseButton.setFocusPainted(false);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.add(pauseButton);
        frame.add(topPanel, BorderLayout.NORTH);

        frame.pack();
        pacmanMap.requestFocus();
        frame.setVisible(true);

        GameEngine engine = new GameEngine(pacmanMap, false);

        pauseButton.addActionListener(e -> {
            engine.pause();
            PauseDialog pauseDialog = new PauseDialog(frame, engine, pseudo);
            pauseDialog.setVisible(true);
        });

        // Thread surveillant la fin de la partie
        new Thread(() -> {
            while (!engine.isGameOver()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }

            int finalScore = engine.getScore();

            // Sauvegarde le score
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("highscores.txt", true))) {
                writer.write(pseudo + " : " + finalScore);
                writer.newLine();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            frame.dispose();

            // Affiche l'écran de fin
            SwingUtilities.invokeLater(() -> new EndScreen(pseudo, finalScore).setVisible(true));
        }).start();
    }

    /**
     * Modal dialog window for game pause functionality with resume and quit options.
     * 
     * <p>The PauseDialog class extends JDialog to provide a modal pause interface
     * that temporarily suspends the game while allowing the player to choose
     * between resuming the current session or quitting to the main menu.
     * 
     * <p>Key features of the pause dialog:
     * <ul>
     * <li>Modal behavior that blocks interaction with the main game window</li>
     * <li>Consistent visual styling matching the game's color scheme</li>
     * <li>Proper game engine state management for pause/resume functionality</li>
     * <li>Keyboard focus restoration when resuming gameplay</li>
     * <li>Clean navigation back to the main menu when quitting</li>
     * </ul>
     * 
     * <p>The dialog automatically handles game engine pause state and ensures
     * proper cleanup and state restoration based on user selection.
     * 
     * @author Equipe 1
     * @version 6.0
     * @since 1.0
     */
    private static class PauseDialog extends JDialog {

        /**
         * Constructs a new pause dialog with the specified parent window and game context.
         * 
         * <p>The dialog is initialized as a modal window with two primary actions:
         * resume gameplay or quit to main menu. The visual styling matches the
         * game's overall design with appropriate fonts and colors.
         * 
         * <p>Button functionality:
         * <ul>
         * <li><strong>Resume:</strong> Unpauses the game engine, closes the dialog, 
         *     and restores keyboard focus to the game map</li>
         * <li><strong>Quit:</strong> Closes both the dialog and main game window, 
         *     then returns to the main menu</li>
         * </ul>
         * 
         * @param parent the parent JFrame window that owns this dialog
         * @param engine the game engine instance to control pause/resume functionality
         * @param pseudo the player's username (maintained for consistency but not actively used in dialog)
         */
        public PauseDialog(JFrame parent, GameEngine engine, String pseudo) {
            super(parent, "Pause", true);
            setSize(300, 150);
            setLocationRelativeTo(parent);
            setLayout(new FlowLayout());
            getContentPane().setBackground(Color.BLACK);

            JButton resumeBtn = new JButton("Reprendre");
            JButton quitBtn = new JButton("Quitter");

            Font btnFont = new Font("Monospaced", Font.BOLD, 16);
            for (JButton btn : new JButton[] { resumeBtn, quitBtn }) {
                btn.setFont(btnFont);
                btn.setBackground(new Color(255, 204, 0));
                btn.setForeground(Color.BLACK);
                btn.setFocusPainted(false);
                add(btn);
            }

            resumeBtn.addActionListener(e -> {
                engine.resume();
                dispose();
                engine.getMap().requestFocus(); // pour récupérer le focus clavier
            });

            quitBtn.addActionListener(e -> {
                dispose();
                parent.dispose();
                SwingUtilities.invokeLater(() -> new MainMenu().setVisible(true));
            });
        }
    }
}
