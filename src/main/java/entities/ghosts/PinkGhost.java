package entities.ghosts;

import java.awt.Image;
import map.Map;

/**
 * Represents a pink ghost entity in a Pac-Man style game that exhibits
 * sophisticated ambush behavior by predicting and intercepting Pac-Man's movement.
 * 
 * <p>The PinkGhost extends the base Ghost class and implements an advanced AI
 * strategy focused on anticipatory positioning. Key behaviors include:
 * <ul>
 * <li>Predictive targeting by calculating Pac-Man's future position</li>
 * <li>Ambush tactics that intercept rather than directly chase</li>
 * <li>Special chase behavior when positioned at tunnel level</li>
 * <li>Strategic positioning ahead of Pac-Man's movement direction</li>
 * </ul>
 * 
 * <p>This ghost type adds tactical depth to the game by creating a more
 * intelligent adversary that requires players to be mindful of their movement
 * patterns and change directions unpredictably to avoid being trapped.
 * 
 * @author Equipe 1  
 * @version 6.0
 * @since 1.0
 */
public class PinkGhost extends Ghost {

    /**
     * The number of tiles ahead of Pac-Man's current position to target for ambush tactics.
     * This value determines how far ahead the ghost attempts to intercept Pac-Man,
     * creating a balance between predictive accuracy and strategic positioning.
     */
    private int anticipatedTiles = 4;

    /**
     * Constructs a new PinkGhost with the specified visual and positional properties.
     * 
     * <p>The pink ghost initializes with standard ghost properties and immediately
     * begins using its ambush AI strategy upon creation.
     * 
     * @param image the visual representation of the ghost
     * @param x the initial X-coordinate position on the map
     * @param y the initial Y-coordinate position on the map
     * @param width the width of the ghost sprite in pixels
     * @param height the height of the ghost sprite in pixels
     * @param map the game map containing layout and game state information
     * @throws IllegalArgumentException if any parameter is null or if dimensions are non-positive
     */
    public PinkGhost(Image image, int x, int y, int width, int height, Map map) {
        super(image, x, y, width, height, map);
    }

    /**
     * Determines the ghost's movement direction using ambush tactics or direct chase.
     * 
     * <p>This method implements the core ambush AI logic through conditional behavior:
     * <ol>
     * <li>Increments the frame counter for timing control</li>
     * <li>Checks if at tunnel level (Y = 9 * tileSize) and target update interval reached</li>
     * <li>Uses direct chase behavior when at tunnel level for optimal navigation</li>
     * <li>Uses ambush tactics for all other positions and timing conditions</li>
     * <li>Resets frame counter after decision making</li>
     * </ol>
     * 
     * <p>The special case for tunnel level ensures the ghost can navigate
     * horizontal passages effectively while maintaining its ambush strategy
     * in open areas of the map.
     * 
     * @see #ambushTarget()
     * @see Ghost#chaseTarget()
     */
    @Override
    protected void decideDirection() {
        super.setFrameCounter(super.getFrameCounter() + 1);
        if (super.getFrameCounter() == super.getTargetUpdateInterval() 
            && super.getY() == super.getMap().getTileSize() * 9) {
            chaseTarget();
            super.setFrameCounter(0);
        } else {
            ambushTarget();
            super.setFrameCounter(0);
        }
    }

    /**
     * Handles collision with walls by immediately switching to ambush tactics.
     * 
     * <p>When the ghost encounters a wall obstacle, this method redirects
     * the ghost to use its ambush strategy to find an alternative path
     * toward the predicted intercept position. This ensures wall collisions
     * don't interrupt the strategic AI behavior.
     * 
     * @see #ambushTarget()
     */
    @Override
    protected void handleWallCollision() {
        ambushTarget();
    }

    /**
     * Calculates and moves toward an anticipated intercept position ahead of Pac-Man.
     * 
     * <p>This method implements the core ambush logic by:
     * <ol>
     * <li>Obtaining Pac-Man's current position and direction</li>
     * <li>Calculating a target position ahead of Pac-Man based on movement direction</li>
     * <li>Applying direction-specific offset calculations</li>
     * <li>Finding the optimal path toward the calculated intercept position</li>
     * <li>Updating the ghost's movement direction accordingly</li>
     * </ol>
     * 
     * <p>Direction-specific calculations:
     * <ul>
     * <li><strong>Up:</strong> Targets position ahead and to the left (classic Pac-Man behavior)</li>
     * <li><strong>Down:</strong> Targets position directly below Pac-Man's future position</li>
     * <li><strong>Left:</strong> Targets position to the left of Pac-Man's future position</li>
     * <li><strong>Right:</strong> Targets position to the right of Pac-Man's future position</li>
     * <li><strong>Default:</strong> Targets position to the right when direction is uncertain</li>
     * </ul>
     * 
     * <p>The upward movement includes both vertical and horizontal offsets,
     * replicating the classic Pac-Man pink ghost behavior that made it particularly
     * challenging to evade.
     * 
     * @see #anticipatedTiles
     * @see Ghost#findBestDirection(int, int)
     */
    private void ambushTarget() {
        int pacmanX = super.getMap().getPacman().getX();
        int pacmanY = super.getMap().getPacman().getY();

        char pacmanDir = super.getMap().getPacman().getDirection();

        switch (pacmanDir) {
            case 'U':
                pacmanY -= anticipatedTiles * super.getMap().getTileSize();
                pacmanX -= anticipatedTiles * super.getMap().getTileSize();
                break;
            case 'D':
                pacmanY += anticipatedTiles * super.getMap().getTileSize();
                break;
            case 'L':
                pacmanX -= anticipatedTiles * super.getMap().getTileSize();
                break;
            case 'R':
                pacmanX += anticipatedTiles * super.getMap().getTileSize();
                break;
            default:
                pacmanX += anticipatedTiles * super.getMap().getTileSize();
                break;
        }

        char bestDir = findBestDirection(pacmanX, pacmanY);
        this.updateDirection(bestDir);
    }
}
