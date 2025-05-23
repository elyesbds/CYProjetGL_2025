package menu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import app.App;

/**
 * The MainMenu class represents the main menu screen of the Pac-Man game.
 * It provides navigation to start new games, view high scores, and exit the application.
 * 
 * <p>Features include:
 * <ul>
 *   <li>Classic Pac-Man styled interface with yellow and black color scheme</li>
 *   <li>Three main action buttons (New Game, High Scores, Exit)</li>
 *   <li>Language toggle capability (French/English)</li>
 *   <li>Modal dialog for username input</li>
 * </ul>
 */
public class MainMenu extends JFrame {

    private JButton btnNouvellePartie;
    private JButton btnQuitter;
    private JButton btnClassement;
    private boolean enFrancais = true;

    /**
     * Constructs the main menu window with all UI components.
     * Initializes the frame with Pac-Man themed styling and sets up:
     * - Title label
     * - Action buttons
     * - Event handlers
     */
    public MainMenu() {
        // Window configuration
        setTitle("Pac-Man - Menu Principal");
        setSize(400, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.BLACK);

        // Main panel setup
        JPanel panel = new JPanel();
        panel.setBackground(Color.BLACK);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Font configuration
        Font arcadeFont = new Font("Monospaced", Font.BOLD, 36);
        Font buttonFont = new Font("Monospaced", Font.BOLD, 16);

        // Title label setup
        JLabel titre = new JLabel("PAC-MAN");
        titre.setForeground(new Color(255, 255, 0)); // Yellow
        titre.setFont(arcadeFont);
        titre.setAlignmentX(Component.CENTER_ALIGNMENT);
        titre.setBorder(BorderFactory.createEmptyBorder(20, 0, 40, 0));

        // Initialize buttons
        btnNouvellePartie = new JButton();
        btnClassement = new JButton();
        btnQuitter = new JButton();

        // Configure buttons
        JButton[] boutons = {btnNouvellePartie, btnClassement, btnQuitter};
        for (JButton btn : boutons) {
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setMaximumSize(new Dimension(220, 50));
            btn.setFocusable(false);
            btn.setFont(buttonFont);
            btn.setBackground(new Color(255, 204, 0)); // Yellow
            btn.setForeground(Color.BLACK);
            btn.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 153), 3)); // Blue border
            btn.setOpaque(true);
            panel.add(btn);
            panel.add(Box.createRigidArea(new Dimension(0, 20))); // Spacing
        }

        // Button action handlers
        btnNouvellePartie.addActionListener(e -> {
            Pseudo fenetrePseudo = new Pseudo(this);
            fenetrePseudo.setVisible(true); // Modal dialog

            String pseudo = fenetrePseudo.getPseudo();
            if (pseudo != null && !pseudo.trim().isEmpty()) {
                dispose();
                try {
                    App.lancerJeu(pseudo.trim());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        btnClassement.addActionListener(e -> {
            new Leaderboard(this).setVisible(true);
        });

        btnQuitter.addActionListener(e -> System.exit(0));

        // Final layout
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(titre, BorderLayout.NORTH);
        getContentPane().add(panel, BorderLayout.CENTER);

        updateTexts(); // Set initial button texts
    }

    /**
     * Returns the appropriate text based on current language setting.
     * 
     * @param fr The French version of the text
     * @param en The English version of the text
     * @return The text in the currently selected language
     */
    private String getText(String fr, String en) {
        return enFrancais ? fr : en;
    }

    /**
     * Updates all UI texts based on current language setting.
     * Called when language is changed or during initialization.
     */
    private void updateTexts() {
        btnNouvellePartie.setText(getText("Nouvelle Partie", "New Game"));
        btnClassement.setText(getText("Classement", "High Scores"));
        btnQuitter.setText(getText("Quitter", "Exit"));
    }

    /**
     * Main entry point for standalone testing of the menu.
     * 
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainMenu().setVisible(true));
    }
}
