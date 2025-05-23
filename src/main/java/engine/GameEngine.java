package engine;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.Timer;

import entities.ghosts.Ghost;
import map.Map;

/**
 * The main game engine that handles the game loop, keyboard input, and game state updates.
 * <p>
 * This class implements {@link ActionListener} to handle periodic updates via a {@link Timer},
 * and {@link KeyListener} to process user input for controlling Pac-Man.
 * </p>
 */
public class GameEngine implements ActionListener, KeyListener{

    private Map map;
    Timer gameLoop;
    private boolean paused = false;

    /**
     * Constructs a new GameEngine with the specified map.
     * Initializes the game loop timer and sets up key listening.
     *
     * @param map The game map containing all game entities
     */
    public GameEngine(Map map){
        this.map = map;

        map.addKeyListener(this);
        map.setFocusable(true);
        map.requestFocusInWindow();

        gameLoop = new Timer(16, this);
        gameLoop.start();
    }

    /**
     * Called on each game tick by the timer.
     * Updates game state including Pac-Man's and ghosts' movements, and repaints the game view.
     *
     * @param e The action event triggered by the timer
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (paused) return; // Ne rien faire si en pause

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
     * Handles key typed events.
     * Not used in this implementation.
     *
     * @param e The key event
     */
    @Override
    public void keyTyped(KeyEvent e) {}

    /**
     * Handles key pressed events.
     * Not used in this implementation.
     *
     * @param e The key event
     */
    @Override
    public void keyPressed(KeyEvent e) {}

    /**
     * Handles key released events to control Pac-Man's movement direction.
     * Also supports ZQSD layout for AZERTY keyboards.
     *
     * @param e The key event
     */
    @Override
    public void keyReleased(KeyEvent e) {
        if (paused) return; // Ne rien faire si en pause

        if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_Z){
            map.getPacman().updateNextDirection('U');
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S){
            map.getPacman().updateNextDirection('D');
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_Q){
            map.getPacman().updateNextDirection('L');
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D){
            map.getPacman().updateNextDirection('R');
        }
    }

    /**
     * Pauses the game loop.
     * Stops the timer to freeze all game updates.
     */
    public void pause() {
        paused = true;
        gameLoop.stop();
    }

    /**
     * Resumes the game loop after a pause.
     * Restarts the timer and requests focus for the map component.
     */
    public void resume() {
        paused = false;
        gameLoop.start();
        map.requestFocusInWindow(); // Redonne le focus clavier après la pause
    }

    /**
     * Returns whether the game is over.
     *
     * @return {@code true} if the game is over; {@code false} otherwise
     */
    public boolean isGameOver() {
        return map.getPacman().isGameOver();
    }

    /**
     * Returns the current score of the player.
     *
     * @return The player's current score
     */
    public int getScore() {
        return map.getPacman().getScore();
    }

    /**
     * Returns the current game map.
     *
     * @return The map used by the game engine
     */
    public Map getMap() {
        return map;
    }
}

