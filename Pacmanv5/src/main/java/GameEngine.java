import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.Timer;

/**
 * The main game engine that handles game loop, input management and game state updates.
 * This class implements ActionListener for the game loop and KeyListener for player input.
 */
public class GameEngine implements ActionListener, KeyListener{

    private Map map;
    Timer gameLoop;

    /**
     * Constructs a new GameEngine with the specified map.
     * Initializes the game loop timer and sets up key listening.
     *
     * @param map The game map containing all game entities
     */
    GameEngine(Map map){
        this.map = map;

        map.addKeyListener(this);
        map.setFocusable(true);

        gameLoop = new Timer(16, this);
        gameLoop.start();
    }

    /**
     * Called on each game tick by the timer.
     * Updates game state including pacman and ghost movements.
     *
     * @param e The action event triggered by the timer
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        map.getPacman().updateDirection(map.getPacman().getNextDirection());
        map.getPacman().move();
        for(Ghost ghost : map.getGhosts()){
            ghost.move();
        }
        map.repaint();
        if(map.getPacman().isGameOver()){
            gameLoop.stop();
        }
    }

    /**
     * Handles key typing events (not used).
     *
     * @param e The key event
     */
    @Override
    public void keyTyped(KeyEvent e) {}

    /**
     * Handles key press events (not used).
     *
     * @param e The key event
     */
    @Override
    public void keyPressed(KeyEvent e) {}

    /**
     * Handles key release events.
     * Processes player input for movement and game restart.
     *
     * @param e The key event
     */
    @Override
    public void keyReleased(KeyEvent e) {
        if(map.getPacman().isGameOver()) {
            map.loadMap();
            map.resetPositions();
            map.getPacman().setLives(3);
            map.getPacman().setScore(0);
            map.getPacman().setGameOver(false);
            gameLoop.start();
        }

        if (e.getKeyCode() == KeyEvent.VK_UP){
            map.getPacman().updateNextDirection('U');
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN){
            map.getPacman().updateNextDirection('D');
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT){
            map.getPacman().updateNextDirection('L');
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT){
            map.getPacman().updateNextDirection('R');
        }
    }
}
