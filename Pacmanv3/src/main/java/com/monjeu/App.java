package main.java.com.monjeu;

import javax.swing.JFrame;

public class App {
    public static void main(String[] args) throws Exception {
        int rows = 21;
        int cols = 19;
        int tileSize = 64;
        int width = cols * tileSize;
        int height = rows * tileSize;

        JFrame frame = new JFrame("Pacman");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, height);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        Map pacmanMap = new Map();
        frame.add(pacmanMap);
        frame.pack();
        pacmanMap.requestFocus();
        frame.setVisible(true);

        GameEngine engine = new GameEngine(pacmanMap);
    }
}
