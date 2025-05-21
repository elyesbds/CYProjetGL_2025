import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.Toolkit;

public class App {
    public static void main(String[] args) throws Exception {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        
        double screenUsage = 0.8;
        int maxWidth = (int)(screenSize.width * screenUsage);
        int maxHeight = (int)(screenSize.height * screenUsage);

        int rows = 21;
        int cols = 19;

        int tileSize = Math.min(maxWidth / cols, maxHeight / rows);

        /*SoundPlayer music = new SoundPlayer("src/main/resources/music.wav");
        music.playLoop();*/

        JFrame frame = new JFrame("Pacman");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        Map pacmanMap = new Map(tileSize);
        frame.add(pacmanMap);
        frame.pack();
        frame.setLocationRelativeTo(null);
        
        pacmanMap.requestFocus();
        frame.setVisible(true);

        GameEngine engine = new GameEngine(pacmanMap);
    }
}