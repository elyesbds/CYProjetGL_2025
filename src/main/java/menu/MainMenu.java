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

public class MainMenu extends JFrame {

    private JButton btnNouvellePartie;
    private JButton btnQuitter;
    private JButton btnClassement;

    private boolean enFrancais = true;

    public MainMenu() {
        setTitle("Pac-Man - Menu Principal");
        setSize(400, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.BLACK);

        JPanel panel = new JPanel();
        panel.setBackground(Color.BLACK);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        Font arcadeFont = new Font("Monospaced", Font.BOLD, 36);
        Font buttonFont = new Font("Monospaced", Font.BOLD, 16);

        JLabel titre = new JLabel("PAC-MAN");
        titre.setForeground(new Color(255, 255, 0));
        titre.setFont(arcadeFont);
        titre.setAlignmentX(Component.CENTER_ALIGNMENT);
        titre.setBorder(BorderFactory.createEmptyBorder(20, 0, 40, 0));

        btnNouvellePartie = new JButton();
        btnClassement = new JButton();
        btnQuitter = new JButton();

        JButton[] boutons = {btnNouvellePartie, btnClassement, btnQuitter};

        for (JButton btn : boutons) {
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setMaximumSize(new Dimension(220, 50));
            btn.setFocusable(false);
            btn.setFont(buttonFont);
            btn.setBackground(new Color(255, 204, 0));
            btn.setForeground(Color.BLACK);
            btn.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 153), 3));
            btn.setOpaque(true);
            panel.add(btn);
            panel.add(Box.createRigidArea(new Dimension(0, 20)));
        }

        btnNouvellePartie.addActionListener(e -> {
            Pseudo fenetrePseudo = new Pseudo(this);
            fenetrePseudo.setVisible(true);  // modal, bloque ici proprement

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
            Leaderboard fenetreClassement = new Leaderboard(this);
            fenetreClassement.setVisible(true);
        });

        btnQuitter.addActionListener(e -> System.exit(0));

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(titre, BorderLayout.NORTH);
        getContentPane().add(panel, BorderLayout.CENTER);

        updateTexts();
    }

    private String getText(String fr, String en) {
        return enFrancais ? fr : en;
    }

    private void updateTexts() {
        btnNouvellePartie.setText(getText("Nouvelle Partie", "New Game"));
        btnClassement.setText(getText("Classement", "High Scores"));
        btnQuitter.setText(getText("Quitter", "Exit"));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainMenu().setVisible(true));
    }

    
}
