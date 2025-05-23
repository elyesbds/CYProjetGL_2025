package entities;

import java.awt.*;
import javax.swing.ImageIcon;
import entities.ghosts.Ghost;
import map.Food;
import map.Map;
import map.Wall;

/**
 * The Player class represents the main player character (Pacman) in the game.
 * It handles player movement, scoring, lives, and interactions with game elements.
 * 
 * <p>Key responsibilities include:
 * <ul>
 *   <li>Managing player movement and direction changes</li>
 *   <li>Handling collision detection with walls and ghosts</li>
 *   <li>Tracking and updating player score</li>
 *   <li>Managing player lives and game over state</li>
 *   <li>Processing food collection and power-up effects</li>
 * </ul>
 */
public class Player extends MobileEntity implements Mobile {

    private int score = 0;
    private int lives = 3;
    private boolean gameOver;

    /**
     * Constructs a new Player with specified parameters.
     *
     * @param image  the default image for the player
     * @param x      the initial x-coordinate in pixels
     * @param y      the initial y-coordinate in pixels
     * @param width  the width of the player in pixels
     * @param height the height of the player in pixels
     * @param map    the game map reference
     */
    public Player(Image image, int x, int y, int width, int height, Map map) {
        super(image, x, y, width, height, map);
        super.setVelocityX(0);
        super.setVelocityY(0);
        super.setDirection('U');
        this.gameOver = false;
    }

    /**
     * Updates the player's position and handles all game interactions.
     * This includes:
     * <ul>
     *   <li>Updating the player's sprite based on direction</li>
     *   <li>Handling screen wrapping at map edges</li>
     *   <li>Detecting wall collisions</li>
     *   <li>Collecting food and updating score</li>
     *   <li>Activating power-up effects</li>
     *   <li>Checking for level completion</li>
     * </ul>
     */
    @Override
    public void move() {
        // Update sprite based on direction
        if (super.getDirection() == 'U') {
            super.setImage(new ImageIcon(getClass().getResource("/pacmanUp.png")).getImage());
        } else if (super.getDirection() == 'D') {
            super.setImage(new ImageIcon(getClass().getResource("/pacmanDown.png")).getImage());
        } else if (super.getDirection() == 'L') {
            super.setImage(new ImageIcon(getClass().getResource("/pacmanLeft.png")).getImage());
        } else if (super.getDirection() == 'R') {
            super.setImage(new ImageIcon(getClass().getResource("/pacmanRight.png")).getImage());
        }

        // Update position
        super.setX(super.getX() + this.getVelocityX());
        super.setY(super.getY() + this.getVelocityY());

        // Handle screen wrapping
        if (super.getX() <= 0) {
            super.setX(getMap().getCols() * getMap().getTileSize() - getMap().getTileSize());
        } else if (super.getX() >= (getMap().getCols() * getMap().getTileSize() - getMap().getTileSize())) {
            super.setX(0);
        }

        // Wall collision detection
        for (Wall wall : getMap().getWalls()) {
            if (collision(this, wall)) {
                super.setX(super.getX() - this.getVelocityX());
                super.setY(super.getY() - this.getVelocityY());
                break;
            }
        }

        // Food collection
        Food foodEaten = null;
        for (Food food : getMap().getFoods()) {
            if (collision(this, food)) {
                foodEaten = food;
                this.score += foodEaten.getScore();
                if ("big".equals(food.getFoodType())) {
                    for (Ghost ghost : getMap().getGhosts()) {
                        ghost.setScared(true);
                    }
                }
            }
        }
        getMap().getFoods().remove(foodEaten);

        // Level completion check
        if (getMap().getFoods().isEmpty()) {
            getMap().setLevel(getMap().getLevel() + 1);
            getMap().loadMap();
            getMap().resetPositions();
        }
    }

    /**
     * Updates the player's velocity based on current direction.
     * The velocity is calculated to ensure smooth movement aligned with the game grid.
     */
    @Override
    public void updateVelocity() {
        int s = 10;
        int tileSize = super.getMap().getTileSize();
        int step = Math.round(tileSize / s);

        alignToGrid(step);

        if (super.getDirection() == 'U') {
            super.setVelocityX(0);
            super.setVelocityY(-step);
        } else if (super.getDirection() == 'D') {
            super.setVelocityX(0);
            super.setVelocityY(step);
        } else if (super.getDirection() == 'L') {
            super.setVelocityX(-step);
            super.setVelocityY(0);
        } else if (super.getDirection() == 'R') {
            super.setVelocityX(step);
            super.setVelocityY(0);
        }
    }

    /**
     * @return the player's current score
     */
    public int getScore() {
        return score;
    }

    /**
     * @param score the new score to set
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * @return the number of remaining lives
     */
    public int getLives() {
        return lives;
    }

    /**
     * @param lives the new number of lives to set
     */
    public void setLives(int lives) {
        this.lives = lives;
    }

    /**
     * @return true if the game is over, false otherwise
     */
    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * @param gameOver the game over state to set
     */
    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }
}
