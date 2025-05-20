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

public class MainMenu extends JFrame {

    private JButton btnNouvellePartie;
    private JButton btnQuitter;

    private boolean enFrancais = true;

    public MainMenu() {
        setTitle("Pac-Man - Menu Principal");
        setSize(400, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Fond noir comme dans le jeu
        getContentPane().setBackground(Color.BLACK);

        JPanel panel = new JPanel();
        panel.setBackground(Color.BLACK);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Police type arcade
        Font arcadeFont = new Font("Monospaced", Font.BOLD, 36);
        Font buttonFont = new Font("Monospaced", Font.BOLD, 16);

        JLabel titre = new JLabel("PAC-MAN");
        titre.setForeground(new Color(255, 255, 0)); // jaune vif style titre du jeu
        titre.setFont(arcadeFont);
        titre.setAlignmentX(Component.CENTER_ALIGNMENT);
        titre.setBorder(BorderFactory.createEmptyBorder(20, 0, 40, 0));

        btnNouvellePartie = new JButton();
        btnQuitter = new JButton();

        JButton[] boutons = {btnNouvellePartie, btnQuitter};

        for (JButton btn : boutons) {
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setMaximumSize(new Dimension(220, 50));
            btn.setFocusable(false);
            btn.setFont(buttonFont);

            // Jaune Pac-Man avec bordure bleu labyrinthe
            btn.setBackground(new Color(255, 204, 0));
            btn.setForeground(Color.BLACK);
            btn.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 153), 3));
            btn.setOpaque(true);
        }

        for (JButton btn : boutons) {
            panel.add(btn);
            panel.add(Box.createRigidArea(new Dimension(0, 20)));
        }

        btnNouvellePartie.addActionListener(e -> {
            dispose(); // ferme le menu
            try {
                App.main(null); // lance le jeu
            } catch (Exception ex) {
                ex.printStackTrace();
            }
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
        btnQuitter.setText(getText("Quitter", "Exit"));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainMenu().setVisible(true));
    }
}
