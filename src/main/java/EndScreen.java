import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class EndScreen extends JFrame {

    public EndScreen(String pseudo, int score) {
        setTitle("Fin de la partie");
        setSize(450, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.BLACK);

        JLabel titre = new JLabel("Game Over", SwingConstants.CENTER);
        titre.setFont(new Font("Monospaced", Font.BOLD, 36));
        titre.setForeground(Color.RED);
        titre.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        JLabel info = new JLabel(pseudo + ", vous avez marqué " + score + " points.", SwingConstants.CENTER);
        info.setFont(new Font("Monospaced", Font.PLAIN, 16));
        info.setForeground(Color.WHITE);

        JPanel boutons = new JPanel();
        boutons.setBackground(Color.BLACK);

        JButton rejouerBtn = new JButton("Rejouer");
        JButton classementBtn = new JButton("Classement");
        JButton menuBtn = new JButton("Menu principal");

        Font btnFont = new Font("Monospaced", Font.BOLD, 14);
        for (JButton btn : new JButton[]{rejouerBtn, classementBtn, menuBtn}) {
            btn.setFont(btnFont);
            btn.setFocusPainted(false);
            btn.setBackground(new Color(255, 204, 0));
            btn.setForeground(Color.BLACK);
            btn.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 153), 2));
            boutons.add(btn);
        }

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

        add(titre, BorderLayout.NORTH);
        add(info, BorderLayout.CENTER);
        add(boutons, BorderLayout.SOUTH);
    }
}
