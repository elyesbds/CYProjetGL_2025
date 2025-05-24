package engine;

import map.*;
import neural.*;
import entities.*;
import entities.ghosts.*;
import java.awt.*;

/**
 * The {@code GameAI} class simulates a full game session of Pac-Man
 * controlled by a neural network. It runs the game logic step-by-step,
 * including Pac-Man's decisions, ghost movement, food collection, and 
 * collision handling.
 *
 * <p>This class is typically used during training or evaluation of neural
 * networks in a headless (non-GUI) mode.</p>
 *
 * <p>The game ends when either all food is eaten, Pac-Man loses all lives, 
 * or a maximum number of idle steps (steps without eating) is reached.</p>
 *
 */
public class GameAI {

    /** The game map containing all entities (walls, food, ghosts, etc.). */
    private Map map;

    /** The AI-controlled Pac-Man player. */
    private PlayerAI pacman;

    /** The neural network controlling Pac-Man's decisions. */
    private NeuralNet brain;

    /** Whether the game is currently running. */
    private boolean running = false;

    /** Maximum allowed steps without eating food before stopping the game. */
    private int MAX_IDLE_STEPS = 3000;

    /** Start time of the current game simulation. */
    private long startTime;

    /** Time elapsed since the start of the game (in milliseconds). */
    private long elapsed;

    /**
     * Constructs a {@code GameAI} with the given neural network to control Pac-Man.
     * Initializes the game map and replaces the default player with a {@link PlayerAI}.
     *
     * @param net the neural network controlling Pac-Man
     */
    public GameAI(NeuralNet net) {
        this.brain = net;
        this.map = new Map(64); // Load map without an active player initially

        // Replace default Pac-Man with AI-controlled version
        Player original = map.getPacman();
        this.pacman = new PlayerAI(
            original.getImage(),
            original.getX(),
            original.getY(),
            original.getWidth(),
            original.getHeight(),
            map,
            net
        );
        map.setPacman(this.pacman);
        this.pacman.setMap(this.map); // ensure reference consistency
    }

    /**
     * Starts the simulation loop. Runs until the game ends due to:
     * <ul>
     *   <li>Pac-Man losing all lives</li>
     *   <li>All food being eaten</li>
     *   <li>Too many idle steps without eating</li>
     * </ul>
     */
public void start() {
    running = true;
    int stepsSinceLastFood = 0;
    startTime = System.currentTimeMillis();

    while (running && !pacman.isGameOver() && stepsSinceLastFood < MAX_IDLE_STEPS) {
        // Déplacer Pacman
        pacman.updateDirection(pacman.getNextDirection());
        pacman.move();

        // Vérifier collisions AVANT que les fantômes bougent
        for (Ghost ghost : map.getGhosts()) {
            if (ghost.isColliding(pacman) && !ghost.isScared()) {
                pacman.setLives(pacman.getLives() - 1);
                if (pacman.getLives() == 0) {
                    pacman.setGameOver(true);
                } else {
                    map.resetPositions();
                }
                break;
            }
        }

        // Déplacer les fantômes
        for (Ghost ghost : map.getGhosts()) {
            ghost.move();
        }

        // Vérifier collisions APRÈS
        for (Ghost ghost : map.getGhosts()) {
            if (ghost.isColliding(pacman) && !ghost.isScared()) {
                pacman.setLives(pacman.getLives() - 1);
                if (pacman.getLives() == 0) {
                    pacman.setGameOver(true);
                } else {
                    map.resetPositions();
                }
                break;
            }
        }

        // Victoire ?
        if (map.getFoods().isEmpty()) {
            pacman.setGameOver(true);
        }

        // Compter les pas sans manger
        if (pacman.ateFoodThisStep()) {
            stepsSinceLastFood = 0;
        } else {
            stepsSinceLastFood++;
        }
    }

    running = false;
    elapsed = System.currentTimeMillis() - startTime;
}

    /**
     * Resets the map and Pac-Man state to initial conditions.
     * Used before running a new simulation.
     */
    public void reset() {
        map.loadMap();
        map.resetPositions();
        pacman.setScore(0);
        pacman.setLives(3);
        pacman.setGameOver(false);
        running = false;
    }

    /**
     * Returns whether the game is currently running.
     *
     * @return {@code true} if running, otherwise {@code false}
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Returns the elapsed time of the last game session.
     *
     * @return time in milliseconds
     */
    public long getElapsedTime() {
        return elapsed;
    }

    /**
     * Returns the game map used in the simulation.
     *
     * @return the current {@link Map}
     */
    public Map getMap() {
        return this.map;
    }

    /**
     * Returns the AI-controlled Pac-Man player.
     *
     * @return the {@link PlayerAI} instance
     */
    public PlayerAI getPacman() {
        return this.pacman;
    }
}
