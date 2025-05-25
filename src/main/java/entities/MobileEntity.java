package entities;

import java.awt.*;
import map.Map;
import map.Wall;

/**
 * The {@code MobileEntity} class represents an abstract entity that can move within the game.
 * It extends the {@link Entity} class and provides additional functionality for movement,
 * collision detection, and alignment to the game grid.
 *
 * <p>This class is intended to be extended by specific mobile entities such as Pac-Man or ghosts.</p>
 */
public abstract class MobileEntity extends Entity {

    /** The starting X position of the entity. */
    private int startX;

    /** The starting Y position of the entity. */
    private int startY;

    /** The next direction the entity will move in. */
    private char nextDirection = ' ';

    /** The current direction the entity is moving in. */
    private char direction;

    /** The X velocity of the entity. */
    private int velocityX;

    /** The Y velocity of the entity. */
    private int velocityY;

    /** The speed of the entity. */
    protected int speed = 4;

    /** The center point of the entity. */
    private Point center;

    /** The radius of the entity, used for collision detection. */
    private int radius;

    /** The map the entity is interacting with. */
    private Map map;

    /**
     * Constructs a new {@code MobileEntity}.
     *
     * @param img    the image representing the entity
     * @param x      the initial X position
     * @param y      the initial Y position
     * @param width  the width of the entity
     * @param height the height of the entity
     * @param map    the map the entity interacts with
     */
    public MobileEntity(Image img, int x, int y, int width, int height, Map map) {
        super(img, x, y, width, height);
        this.startX = x;
        this.startY = y;
        this.map = map;
    }

    /**
     * Updates the next direction the entity will move in.
     *
     * @param direction the next direction
     */
    public void updateNextDirection(char direction) {
        this.nextDirection = direction;
    }

    /**
     * Resets the entity to its starting position.
     */
    public void reset() {
        super.setX(this.startX);
        super.setY(this.startY);
    }

    /**
     * Abstract method to update the velocity of the entity.
     * Must be implemented by subclasses.
     */
    public abstract void updateVelocity();

    /**
     * Updates the direction of the entity and checks for collisions.
     *
     * @param direction the new direction
     */
    public void updateDirection(char direction) {
        int tempX = super.getX();
        int tempY = super.getY();

        char prevDirection = this.direction;
        this.direction = direction;
        updateVelocity();

        tempX += this.velocityX;
        tempY += this.velocityY;

        boolean collides = false;
        for (Wall wall : map.getWalls()) {
            if (collision(new Entity(null, tempX, tempY, super.getWidth(), super.getHeight()), wall)) {
                collides = true;
                break;
            }
        }
        if (collides) {
            this.direction = prevDirection;
            updateVelocity();
        }
    }

    /**
     * Aligns the entity to the grid based on the given step size.
     *
     * @param step the step size for alignment
     */
    public void alignToGrid(int step) {
        int x = super.getX();
        int y = super.getY();
        int tileSize = this.getMap().getTileSize();

        int distX = x % tileSize;
        int distY = y % tileSize;

        char dir = this.getDirection();

        switch (dir) {
            case 'U':
            case 'D':
                if (distX < step || tileSize - distX < step) {
                    super.setX(x - distX + ((distX > tileSize / 2) ? tileSize : 0));
                }
                break;
            case 'L':
            case 'R':
                if (distY < step || tileSize - distY < step) {
                    super.setY(y - distY + ((distY > tileSize / 2) ? tileSize : 0));
                }
                break;
            default:
                break;
        }
    }

    /**
     * Checks if this entity is colliding with another mobile entity.
     *
     * @param other the other mobile entity
     * @return true if the entities are colliding, false otherwise
     */
    public boolean isColliding(MobileEntity other) {
        Point center1 = this.getCenter();
        Point center2 = other.getCenter();
        double distance = center1.distance(center2);
        return distance < this.getRadius() + other.getRadius();
    }

    /**
     * Gets the radius of the entity.
     *
     * @return the radius
     */
    public int getRadius() {
        return super.getWidth() / 2;
    }

    /**
     * Gets the center point of the entity.
     *
     * @return the center point
     */
    public Point getCenter() {
        return new Point(super.getX() + super.getWidth() / 2, super.getY() + super.getHeight() / 2);
    }

    /**
     * Gets the starting X position of the entity.
     *
     * @return the starting X position
     */
    public int getStartX() {
        return startX;
    }

    /**
     * Sets the starting X position of the entity.
     *
     * @param startX the starting X position to set
     */
    public void setStartX(int startX) {
        this.startX = startX;
    }

    /**
     * Gets the starting Y position of the entity.
     *
     * @return the starting Y position
     */
    public int getStartY() {
        return startY;
    }

    /**
     * Sets the starting Y position of the entity.
     *
     * @param startY the starting Y position to set
     */
    public void setStartY(int startY) {
        this.startY = startY;
    }

    /**
     * Gets the next direction the entity will move in.
     *
     * @return the next direction
     */
    public char getNextDirection() {
        return nextDirection;
    }

    /**
     * Sets the next direction the entity will move in.
     *
     * @param nextDirection the next direction to set
     */
    public void setNextDirection(char nextDirection) {
        this.nextDirection = nextDirection;
    }

    /**
     * Gets the current direction the entity is moving in.
     *
     * @return the current direction
     */
    public char getDirection() {
        return direction;
    }

    /**
     * Sets the current direction the entity is moving in.
     *
     * @param direction the direction to set
     */
    public void setDirection(char direction) {
        this.direction = direction;
    }

    /**
     * Gets the X velocity of the entity.
     *
     * @return the X velocity
     */
    public int getVelocityX() {
        return velocityX;
    }

    /**
     * Sets the X velocity of the entity.
     *
     * @param velocityX the X velocity to set
     */
    public void setVelocityX(int velocityX) {
        this.velocityX = velocityX;
    }

    /**
     * Gets the Y velocity of the entity.
     *
     * @return the Y velocity
     */
    public int getVelocityY() {
        return velocityY;
    }

    /**
     * Sets the Y velocity of the entity.
     *
     * @param velocityY the Y velocity to set
     */
    public void setVelocityY(int velocityY) {
        this.velocityY = velocityY;
    }

    /**
     * Gets the map the entity is interacting with.
     *
     * @return the map
     */
    public Map getMap() {
        return this.map;
    }

    /**
     * Sets the map the entity is interacting with.
     *
     * @param map the map to set
     */
    public void setMap(Map map) {
        this.map = map;
    }

    /**
     * Gets the speed of the entity.
     *
     * @return the speed
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * Sets the speed of the entity.
     *
     * @param speed the speed to set
     */
    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
