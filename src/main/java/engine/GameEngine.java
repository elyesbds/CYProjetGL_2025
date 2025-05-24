package engine;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.Timer;

import entities.ghosts.Ghost;
import map.Map;

/**
 * The {@code GameEngine} class manages the main game loop and handles user input
 * for controlling the Pac-Man game. It supports both AI-controlled and player-controlled modes.
 */
public class GameEngine implements ActionListener, KeyListener {

    /** The game map containing Pac-Man, ghosts, walls, and food. */
    private Map map;

    /** Timer for the game loop, running at approximately 60 FPS. */
    private Timer gameLoop;

    /** Indicates whether the game is currently paused. */
    private boolean paused = false;

    /** Indicates whether the game is running in AI-controlled mode. */
    private boolean isAI;

    /**
     * Constructs a new {@code GameEngine} instance.
     *
     * @param map  the game map to be used
     * @param isAI true if the game is controlled by AI, false for player control
     */
    public GameEngine(Map map, boolean isAI) {
        this.map = map;
        this.isAI = isAI;

        map.addKeyListener(this);
        map.setFocusable(true);
        map.requestFocusInWindow();

        gameLoop = new Timer(16, this); // ~60 FPS
        gameLoop.start();
    }

    /**
     * Handles the game loop's action events.
     *
     * @param e the action event triggered by the timer
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        gameTick();
    }

    /**
     * Handles key press events. This method is not used in this implementation.
     *
     * @param e the key event
     */
    @Override
    public void keyPressed(KeyEvent e) {}

    /**
     * Handles key release events to update Pac-Man's direction.
     *
     * @param e the key event
     */
    @Override
    public void keyReleased(KeyEvent e) {
        if (paused || isAI) return;

        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
            case KeyEvent.VK_Z:
                map.getPacman().updateNextDirection('U');
                break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
                map.getPacman().updateNextDirection('D');
                break;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_Q:
                map.getPacman().updateNextDirection('L');
                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                map.getPacman().updateNextDirection('R');
                break;
        }
    }

    /**
     * Handles key typed events. This method is not used in this implementation.
     *
     * @param e the key event
     */
    @Override
    public void keyTyped(KeyEvent e) {}

    /**
     * Pauses the game by stopping the game loop.
     */
    public void pause() {
        paused = true;
        gameLoop.stop();
    }

    /**
     * Resumes the game by restarting the game loop.
     */
    public void resume() {
        paused = false;
        gameLoop.start();
        map.requestFocusInWindow();
    }

    /**
     * Checks if the game is over.
     *
     * @return true if the game is over, false otherwise
     */
    public boolean isGameOver() {
        return map.getPacman().isGameOver();
    }

    /**
     * Gets the current score of the game.
     *
     * @return the current score
     */
    public int getScore() {
        return map.getPacman().getScore();
    }

    /**
     * Gets the game map.
     *
     * @return the game map
     */
    public Map getMap() {
        return map;
    }

    /**
     * Executes a single tick of the game loop.
     * Handles movement, collisions, and game state updates.
     */
    protected void gameTick() {
        if (paused) return;

        if (isAI) {
            map.getPacman().move(); // AI-controlled movement
        } else {
            map.getPacman().updateDirection(map.getPacman().getNextDirection());
            map.getPacman().move();
        }

        for (Ghost ghost : map.getGhosts()) {
            ghost.move(); // Move each ghost
        }

        map.repaint(); // Redraw the map

        if (map.getPacman().isGameOver()) {
            gameLoop.stop(); // Stop the game loop if the game is over
        }
    }
}
