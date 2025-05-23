package engine;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.Timer;

import entities.ghosts.Ghost;
import map.Map;

/**
 * The main game engine that handles game loop, input management and game state updates.
 * This class implements ActionListener for the game loop and KeyListener for player input.
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

        gameLoop = new Timer(20, this);
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

    public void pause() {
        paused = true;
        gameLoop.stop();
    }

    public void resume() {
        paused = false;
        gameLoop.start();
        map.requestFocusInWindow(); // Redonne le focus clavier après la pause
    }

    public boolean isGameOver() {
        return map.getPacman().isGameOver();
    }

    public int getScore() {
        return map.getPacman().getScore();
    }

    public Map getMap() {
        return map;
    }
}

