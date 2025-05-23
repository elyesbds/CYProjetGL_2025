package menu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import app.App;

/**
 * The EndScreen class represents the game over screen displayed when a player's game ends.
 * It shows the player's score and provides options to replay, view leaderboard, or return to main menu.
 * 
 * <p>Features include:
 * <ul>
 *   <li>Display of player's final score</li>
 *   <li>Navigation options for replay or menu access</li>
 *   <li>Consistent styling with game theme</li>
 * </ul>
 */
public class EndScreen extends JFrame {

    /**
     * Constructs an EndScreen with player information and score.
     *
     * @param pseudo the player's username to display
     * @param score the player's final score to display
     */
    public EndScreen(String pseudo, int score) {
        // Window configuration
        setTitle("Fin de la partie");
        setSize(450, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.BLACK);

        // Title label setup
        JLabel titre = new JLabel("Game Over", SwingConstants.CENTER);
        titre.setFont(new Font("Monospaced", Font.BOLD, 36));
        titre.setForeground(Color.RED);
        titre.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        // Score information label
        JLabel info = new JLabel(pseudo + ", vous avez marqué " + score + " points.", SwingConstants.CENTER);
        info.setFont(new Font("Monospaced", Font.PLAIN, 16));
        info.setForeground(Color.WHITE);

        // Button panel setup
        JPanel boutons = new JPanel();
        boutons.setBackground(Color.BLACK);

        // Create action buttons
        JButton rejouerBtn = new JButton("Rejouer");
        JButton classementBtn = new JButton("Classement");
        JButton menuBtn = new JButton("Menu principal");

        // Style all buttons consistently
        Font btnFont = new Font("Monospaced", Font.BOLD, 14);
        for (JButton btn : new JButton[]{rejouerBtn, classementBtn, menuBtn}) {
            btn.setFont(btnFont);
            btn.setFocusPainted(false);
            btn.setBackground(new Color(255, 204, 0)); // Yellow
            btn.setForeground(Color.BLACK);
            btn.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 153), 2)); // Dark blue border
            boutons.add(btn);
        }

        // Button action listeners
        rejouerBtn.addActionListener(e -> {
            dispose();
            App.lancerJeu(pseudo);
        });

        classementBtn.addActionListener(e -> {
            new Leaderboard(this).setVisible(true);
        });

        menuBtn.addActionListener(e -> {
            dispose();
            new MainMenu().setVisible(true);
        });

        // Add components to frame
        add(titre, BorderLayout.NORTH);
        add(info, BorderLayout.CENTER);
        add(boutons, BorderLayout.SOUTH);
    }
}
