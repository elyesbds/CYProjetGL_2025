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

public class App {

    public static void main(String[] args) {
        // Au lancement, on ouvre le menu principal
        SwingUtilities.invokeLater(() -> {
            new MainMenu().setVisible(true);
        });
    }

    public static void lancerJeu(String pseudo) {

        /*SoundPlayer music = new SoundPlayer("src/main/resources/music.wav");
        music.playLoop();*/

        int rows = 21;
        int cols = 19;
        int tileSize = 32;

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

        GameEngine engine = new GameEngine(pacmanMap);

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

    // Fenêtre modale Pause
    private static class PauseDialog extends JDialog {
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
