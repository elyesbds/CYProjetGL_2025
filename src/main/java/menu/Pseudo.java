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

/**
 * The Pseudo class represents a dialog for entering player username.
 * It provides a modal input dialog with validation to ensure non-empty usernames.
 * 
 * <p>Features include:
 * <ul>
 *   <li>Modal dialog that blocks parent frame interaction</li>
 *   <li>Input validation for non-empty usernames</li>
 *   <li>Consistent Pac-Man themed styling</li>
 *   <li>Simple API to retrieve entered username</li>
 * </ul>
 */
public class Pseudo extends JDialog {

    private String pseudo = null;
    private final JTextField champPseudo = new JTextField(20);

    /**
     * Constructs a new username input dialog.
     * 
     * @param parent the parent JFrame to which this dialog is attached
     */
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

    /**
     * Returns the username entered by the player.
     * 
     * @return the entered username, or null if dialog was canceled or closed without valid input
     */
    public String getPseudo() {
        return pseudo;
    }
}

