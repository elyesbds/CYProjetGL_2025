import javax.swing.JFrame;

public class App {
    public static void main(String[] args) {

        int rows = 21;
        int columns = 19;
        int tileSize = 32;
        int gameWidth = columns * tileSize;
        int gameHeight = rows * tileSize;

        JFrame frame = new JFrame("Pacman");
        //frame.setVisible(true);
        frame.setSize(gameWidth, gameHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Pacman pacmanGame = new Pacman();
        frame.add(pacmanGame);
        frame.pack();
        pacmanGame.requestFocus();
        frame.setVisible(true);
    }
}