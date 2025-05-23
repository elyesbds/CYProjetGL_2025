package menu;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 * The Leaderboard class displays a modal dialog showing player high scores.
 * It reads scores from a file, sorts them in descending order, and presents them in a styled table.
 * 
 * <p>Key features:
 * <ul>
 *   <li>Reads scores from "highscores.txt" file</li>
 *   <li>Displays scores in descending order</li>
 *   <li>Uses consistent game-themed styling</li>
 *   <li>Provides modal view that blocks parent window interaction</li>
 * </ul>
 */
public class Leaderboard extends JDialog {

    /**
     * Constructs a Leaderboard dialog attached to a parent frame.
     * 
     * @param parent the parent JFrame to which this dialog is attached
     */
    public Leaderboard(JFrame parent) {
        super(parent, "Classement", true);
        setSize(400, 400);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(Color.BLACK);

        String[] colonnes = {"Pseudo", "Score"};
        DefaultTableModel model = new DefaultTableModel(colonnes, 0);

        List<String[]> scores = lireScores();
        for (String[] ligne : scores) {
            model.addRow(ligne);
        }

        JTable table = new JTable(model);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < colonnes.length; i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        table.setBackground(Color.BLACK);
        table.setForeground(new Color(255, 204, 0));
        table.setFont(new Font("Monospaced", Font.PLAIN, 14));
        table.setRowHeight(25);
        table.setShowGrid(false);
        table.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.BLACK);

        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Reads scores from the highscores file and sorts them in descending order.
     * 
     * @return List of String arrays where each array contains [pseudo, score]
     */
    private List<String[]> lireScores() {
        List<String[]> result = new ArrayList<>();
        try (Scanner sc = new Scanner(new File("highscores.txt"))) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] parts = line.split(" *: *");
                if (parts.length == 2) {
                    result.add(parts);
                }
            }
        } catch (IOException e) {
            result.add(new String[]{"Aucun", "score"});
        }
        result.sort((a, b) -> Integer.parseInt(b[1]) - Integer.parseInt(a[1]));
        return result;
    }
}
