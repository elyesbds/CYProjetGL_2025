package entities.ghosts;

import java.awt.Image;
import map.Map;

/**
 * Represents a blue ghost entity in a Pac-Man style game that exhibits unique
 * fleeing behavior when close to Pac-Man and aggressive chasing when distant.
 * 
 * <p>The BlueGhost extends the base Ghost class and implements a strategic AI
 * that switches between two distinct behaviors based on its distance from Pac-Man:
 * <ul>
 * <li>When within fleeing distance: retreats to a corner position</li>
 * <li>When beyond fleeing distance: actively chases Pac-Man</li>
 * </ul>
 * 
 * <p>This ghost type adds tactical depth to the game by creating a defensive
 * ghost that becomes more dangerous when Pac-Man maintains distance.
 * 
 * @author Equipe 1
 * @version 6.0
 * @since 1.0
 */
public class BlueGhost extends Ghost {

    /**
     * The minimum distance from Pac-Man (in pixels) that triggers fleeing behavior.
     * When Pac-Man is closer than this distance, the ghost will retreat to its corner.
     * Calculated as 8 times the map's tile size for consistent scaling.
     */
    private final int distanceBeforeFlee = 8 * super.getMap().getTileSize();
    
    /**
     * The X-coordinate of the corner position where this ghost retreats when fleeing.
     * Set to one tile size from the left edge of the map.
     */
    private final int cornerX = super.getMap().getTileSize();
    
    /**
     * The Y-coordinate of the corner position where this ghost retreats when fleeing.
     * Set to two tiles from the bottom of the map for strategic positioning.
     */
    private final int cornerY = (super.getMap().getRows() - 2) * super.getMap().getTileSize();

    /**
     * Constructs a new BlueGhost with the specified visual and positional properties.
     * 
     * @param image the visual representation of the ghost
     * @param x the initial X-coordinate position on the map
     * @param y the initial Y-coordinate position on the map
     * @param width the width of the ghost sprite in pixels
     * @param height the height of the ghost sprite in pixels
     * @param map the game map containing layout and game state information
     * @throws IllegalArgumentException if any parameter is null or if dimensions are non-positive
     */
    public BlueGhost(Image image, int x, int y, int width, int height, Map map) {
        super(image, x, y, width, height, map);
    }

   /**
     * Determines the ghost's movement direction based on distance-dependent behavior.
     * 
     * <p>This method implements the core AI logic by:
     * <ol>
     * <li>Incrementing the frame counter for timing control</li>
     * <li>Checking if enough frames have passed for a direction update</li>
     * <li>Calculating distance to Pac-Man</li>
     * <li>Choosing between fleeing to corner or chasing based on distance</li>
     * <li>Resetting the frame counter after decision making</li>
     * </ol>
     * 
     * <p>The decision is made every {@code targetUpdateInterval} frames to ensure
     * smooth movement while maintaining responsive AI behavior.
     * 
     * @see #moveToCorner()
     * @see Ghost#chaseTarget()
     */
   @Override
    protected void decideDirection() {
       super.setFrameCounter(super.getFrameCounter() + 1); 
        
        if (super.getFrameCounter() >= super.getTargetUpdateInterval()) {
            double distanceToPacman = calculateDistance(super.getX(), super.getY(), super.getMap().getPacman().getX(), super.getMap().getPacman().getY());
            
            if (distanceToPacman < distanceBeforeFlee) {
                moveToCorner();
            } 
            else {
                chaseTarget();
            }
            super.setFrameCounter(0);
        }
    }

    /**
     * Handles collision with walls by reassessing movement strategy.
     * 
     * <p>When the ghost encounters a wall obstacle, this method:
     * <ul>
     * <li>Recalculates the distance to Pac-Man</li>
     * <li>Determines whether to continue fleeing or switch to chasing</li>
     * <li>Updates the movement direction accordingly</li>
     * </ul>
     * 
     * <p>This ensures that wall collisions don't interrupt the ghost's strategic
     * behavior and that it continues to follow its distance-based AI logic.
     */
    @Override
    protected void handleWallCollision() {
        double distanceToPacman = calculateDistance(super.getX(), super.getY(), super.getMap().getPacman().getX(), super.getMap().getPacman().getY());
        
        if (distanceToPacman < distanceBeforeFlee) {
            moveToCorner();
        } else {
            super.chaseTarget();
        }
    }

    /**
     * Directs the ghost to move toward its designated corner position.
     * 
     * <p>This private method handles the fleeing behavior by:
     * <ol>
     * <li>Finding the optimal direction toward the corner coordinates</li>
     * <li>Updating the ghost's movement direction</li>
     * </ol>
     * 
     * <p>The corner position is strategically chosen to provide the ghost with
     * a safe retreat location that doesn't interfere with other game elements.
     * 
     * @see #cornerX
     * @see #cornerY
     */
    private void moveToCorner() {
        char bestDirection = findBestDirection(cornerX, cornerY);
        this.updateDirection(bestDirection);
    }
}
