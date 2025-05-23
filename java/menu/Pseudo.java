package menu;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class Pseudo extends JDialog {

    private String pseudo = null;
    private final JTextField champPseudo = new JTextField(20);

    public Pseudo(JFrame parent) {
        super(parent, "Entrer votre pseudo", true); // true = modal
        setLayout(new BorderLayout(10,10));
        setSize(300, 150);
        setLocationRelativeTo(parent);
        getContentPane().setBackground(Color.BLACK);

        JLabel label = new JLabel("Entrez votre pseudo :", SwingConstants.CENTER);
        label.setForeground(new Color(255, 204, 0));
        label.setFont(new Font("Monospaced", Font.BOLD, 16));
        add(label, BorderLayout.NORTH);

        champPseudo.setFont(new Font("Monospaced", Font.PLAIN, 16));
        add(champPseudo, BorderLayout.CENTER);

        JButton btnValider = new JButton("Valider");
        btnValider.setBackground(new Color(255, 204, 0));
        btnValider.setForeground(Color.BLACK);
        btnValider.setFont(new Font("Monospaced", Font.BOLD, 14));

        btnValider.addActionListener(e -> {
            if (!champPseudo.getText().trim().isEmpty()) {
                pseudo = champPseudo.getText().trim();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Le pseudo ne peut pas être vide.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel panelBtn = new JPanel();
        panelBtn.setBackground(Color.BLACK);
        panelBtn.add(btnValider);
        add(panelBtn, BorderLayout.SOUTH);
    }

    public String getPseudo() {
        return pseudo;
    }
}

