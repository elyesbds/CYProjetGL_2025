package entities.ghosts;

import java.awt.Image;
import map.Map;

/**
 * Represents a red ghost entity in a Pac-Man style game that exhibits
 * direct and relentless pursuit behavior toward Pac-Man.
 * 
 * <p>The RedGhost extends the base Ghost class and implements the most
 * straightforward and aggressive AI strategy. Key behaviors include:
 * <ul>
 * <li>Direct chase targeting toward Pac-Man's current position</li>
 * <li>Consistent pursuit without special movement patterns</li>
 * <li>Relentless tracking that maintains pressure on the player</li>
 * <li>Simple but effective pathfinding toward the target</li>
 * </ul>
 * 
 * <p>This ghost type serves as the primary antagonist in the game, providing
 * constant pressure through its unwavering pursuit. The simplicity of its
 * behavior makes it predictable but dangerous, as players must continuously
 * move to avoid being caught by its direct approach.
 * 
 * <p>The red ghost typically represents the leader or most aggressive ghost
 * in classic Pac-Man games, making it an essential component for maintaining
 * game tension and challenge.
 * 
 * @author Equipe 1
 * @version 6.0
 * @since 1.0
 */
public class RedGhost extends Ghost {

    /**
     * Constructs a new RedGhost with the specified visual and positional properties.
     * 
     * <p>The red ghost initializes with standard ghost properties and immediately
     * begins using its direct chase AI strategy upon creation. No special
     * initialization behavior is required beyond the base ghost setup.
     * 
     * @param image the visual representation of the ghost
     * @param x the initial X-coordinate position on the map
     * @param y the initial Y-coordinate position on the map
     * @param width the width of the ghost sprite in pixels
     * @param height the height of the ghost sprite in pixels
     * @param map the game map containing layout and game state information
     * @throws IllegalArgumentException if any parameter is null or if dimensions are non-positive
     */
    public RedGhost(Image image, int x, int y, int width, int height, Map map) {
        super(image, x, y, width, height, map);
    }

    /**
     * Determines the ghost's movement direction using direct chase targeting.
     * 
     * <p>This method implements the core direct chase AI logic through a simple process:
     * <ol>
     * <li>Increments the frame counter for timing control</li>
     * <li>Checks if the target update interval has been reached</li>
     * <li>Executes direct chase behavior toward Pac-Man's current position</li>
     * <li>Resets the frame counter after updating direction</li>
     * </ol>
     * 
     * <p>The timing mechanism ensures smooth movement while providing regular
     * direction updates to maintain accurate pursuit of the target. The
     * straightforward logic makes this ghost predictable but relentlessly effective.
     * 
     * @see Ghost#chaseTarget()
     * @see Ghost#getTargetUpdateInterval()
     */
    @Override
    protected void decideDirection() {
        super.setFrameCounter(super.getFrameCounter() + 1);

        if (super.getFrameCounter() >= super.getTargetUpdateInterval()) {
            chaseTarget();
            super.setFrameCounter(0);
        }
    }

    /**
     * Handles collision with walls by continuing the direct chase strategy.
     * 
     * <p>When the ghost encounters a wall obstacle, this method immediately
     * attempts to find an alternative path toward Pac-Man using the same
     * direct chase logic. This ensures that wall collisions don't interrupt
     * the pursuit behavior and the ghost continues seeking the optimal
     * route to its target.
     * 
     * <p>The consistent use of chase targeting maintains the ghost's
     * aggressive and predictable behavior pattern even when navigating
     * around obstacles in the maze.
     * 
     * @see Ghost#chaseTarget()
     */
    @Override
    protected void handleWallCollision() {
        super.chaseTarget();
    }
}
