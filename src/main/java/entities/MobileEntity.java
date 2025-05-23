package entities;
import java.awt.*;

import entities.Entity;

import map.Map;
import map.Wall;


/**
 * An abstract base class representing all mobile entities in the game.
 * Provides core functionality for movement, collision detection, and positioning
 * that is shared by both player characters and ghosts.
 * 
 * <p>This class handles:
 * <ul>
 *   <li>Movement velocity and direction management</li>
 *   <li>Collision detection with walls and other entities</li>
 *   <li>Position alignment to the game grid</li>
 *   <li>Reset functionality to starting positions</li>
 * </ul>
 */
public abstract class MobileEntity extends Entity {
	private int startX;
    private int startY;
    private char nextDirection = ' ';
    private char direction;
    private int velocityX;
    private int velocityY;

    private Point center;
    private int radius;
    
    private Map map; // les fantomes et pacman ont besoin de connaitre la map
    

    /**
     * Constructs a new MobileEntity with specified parameters.
     *
     * @param img    the image representing this entity
     * @param x      the initial x-coordinate in pixels
     * @param y      the initial y-coordinate in pixels
     * @param width  the width of the entity in pixels
     * @param height the height of the entity in pixels
     * @param map    the game map reference for collision detection
     */
    public MobileEntity(Image img, int x, int y, int width, int height, Map map) {
    	super(img,x,y,width,height);
    	this.startX = x;
    	this.startY = y;
    	this.map = map;
    }

     /**
     * Updates the queued direction for this entity.
     * The direction change will be processed during the next movement update.
     *
     * @param direction the new direction character ('U', 'D', 'L', 'R')
     */
    public void updateNextDirection(char direction){
        this.nextDirection = direction;
    }
    
    /**
     * Resets the entity to its starting position.
     */
    public void reset(){
        super.setX(this.startX);
        super.setY(this.startY);
    }

    /**
     * Abstract method to update velocity based on current direction.
     * Must be implemented by concrete subclasses to define specific movement behavior.
     */
    public abstract void updateVelocity();
    
    /**
     * Attempts to change the entity's movement direction.
     * Verifies that the new direction won't cause immediate wall collisions.
     *
     * @param direction the new direction to attempt ('U', 'D', 'L', 'R')
     */
    public void updateDirection(char direction){
        int tempX = super.getX();
        int tempY = super.getY();

        char prevDirection = this.direction;
        this.direction = direction;
        updateVelocity();

        tempX += this.velocityX;
        tempY += this.velocityY;

        boolean collides = false;
        for(Wall wall : map.getWalls()){
            if(collision(new Entity(null, tempX, tempY, super.getWidth(), super.getHeight()), wall)){
                collides = true;
                break;
            }
        }
        if(collides){
            this.direction = prevDirection;
            updateVelocity();
        } else {
        }
    }

    /**
     * Aligns the entity to the game grid when approaching tile boundaries.
     * Ensures smooth movement and proper positioning for turns.
     *
     * @param step the distance threshold for alignment in pixels
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
     * @param other the other mobile entity to check against
     * @return true if the entities are colliding, false otherwise
     */
    public boolean isColliding(MobileEntity other) {
        Point center1 = this.getCenter();
        Point center2 = other.getCenter();
        double distance = center1.distance(center2);
        return distance < this.getRadius() + other.getRadius();
    }

    /**
     * Gets the collision radius of this entity.
     *
     * @return the collision radius in pixels
     */
    public int getRadius() {
        return super.getWidth() / 2;
    }

    /**
     * Gets the center point of this entity.
     *
     * @return a Point representing the center coordinates
     */
    public Point getCenter() {
        return new Point(super.getX() + super.getWidth() / 2, super.getY() + super.getHeight() / 2);
    }


    /**
     * @return the starting X coordinate
     */
	public int getStartX() {
		return startX;
	}



    /**
     * @param startX the starting X coordinate to set
     */
	public void setStartX(int startX) {
		this.startX = startX;
	}



    /**
     * @return the starting Y coordinate
     */
	public int getStartY() {
		return startY;
	}



    /**
     * @param startY the starting Y coordinate to set
     */
	public void setStartY(int startY) {
		this.startY = startY;
	}



    /**
     * @return the next queued direction
     */
	public char getNextDirection() {
		return nextDirection;
	}



    /**
     * @param nextDirection the next direction to queue
     */
	public void setNextDirection(char nextDirection) {
		this.nextDirection = nextDirection;
	}



    /**
     * @return the current movement direction
     */
	public char getDirection() {
		return direction;
	}



    /**
     * @param direction the movement direction to set
     */
	public void setDirection(char direction) {
		this.direction = direction;
	}



    /**
     * @return the current horizontal velocity
     */
	public int getVelocityX() {
		return velocityX;
	}



    /**
     * @param velocityX the horizontal velocity to set
     */
	public void setVelocityX(int velocityX) {
		this.velocityX = velocityX;
	}



/**
     * @return the current vertical velocity
     */
	public int getVelocityY() {
		return velocityY;
	}

    /**
     * @param velocityY the vertical velocity to set
     */
	public void setVelocityY(int velocityY) {
		this.velocityY = velocityY;
	}
	
    /**
     * @return the game map reference
     */
	public Map getMap() {
		return this.map;
	}

    /**
     * @param map the game map to set
     */
	public void setMap(Map map) {
		this.map = map;
	}
}
