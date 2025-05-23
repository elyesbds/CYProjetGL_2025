package entities.ghosts;

import java.awt.Image;
import map.Map;

/**
 * Represents an orange ghost entity in a Pac-Man style game that exhibits
 * unpredictable, random movement behavior with special tunnel navigation logic.
 * 
 * <p>The OrangeGhost extends the base Ghost class and implements a chaotic AI
 * strategy that makes its movement patterns difficult to predict. Key behaviors include:
 * <ul>
 * <li>Random direction changes at regular intervals</li>
 * <li>Random direction selection upon wall collisions</li>
 * <li>Special upward movement when positioned at tunnel level</li>
 * </ul>
 * 
 * <p>This ghost type adds unpredictability to the game, forcing players to
 * adapt to erratic movement patterns rather than relying on predictable AI behavior.
 * The random nature makes it both less strategic but potentially more dangerous
 * due to its unpredictable pathing.
 * 
 * @author Equipe 1
 * @version 6.0
 * @since 1.0
 */
public class OrangeGhost extends Ghost {

    /**
     * Constructs a new OrangeGhost with the specified visual and positional properties.
     * 
     * <p>Upon construction, the ghost immediately selects a random initial direction
     * to begin its erratic movement pattern. This ensures the ghost starts moving
     * unpredictably from the moment it's created.
     * 
     * @param image the visual representation of the ghost
     * @param x the initial X-coordinate position on the map
     * @param y the initial Y-coordinate position on the map
     * @param width the width of the ghost sprite in pixels
     * @param height the height of the ghost sprite in pixels
     * @param map the game map containing layout and game state information
     * @throws IllegalArgumentException if any parameter is null or if dimensions are non-positive
     */
    public OrangeGhost(Image image, int x, int y, int width, int height, Map map) {
        super(image, x, y, width, height, map);
        char newDirection = super.getDirections()[random.nextInt(4)];
        this.updateDirection(newDirection);
    }

    /**
     * Determines the ghost's movement direction using random selection with special cases.
     * 
     * <p>This method implements the core random AI logic through a multi-step process:
     * <ol>
     * <li>Increments the frame counter for timing control</li>
     * <li>Checks if the direction change interval has been reached</li>
     * <li>Selects a new random direction and resets the counter if interval reached</li>
     * <li>Applies special tunnel navigation logic when at the tunnel level</li>
     * </ol>
     * 
     * <p>The special case handling ensures the ghost moves upward when positioned
     * at tunnel level (Y = 9 * tileSize) and not already moving vertically,
     * preventing the ghost from getting stuck in horizontal tunnel areas.
     * 
     * @see Ghost#getChangeDirectionInterval()
     * @see Ghost#getDirections()
     * @see Ghost#updateDirection(char)
     */
    @Override
    protected void decideDirection() {
        super.setFrameCounter(super.getFrameCounter() + 1);

        if (super.getFrameCounter() == super.getChangeDirectionInterval()) {
            char newDirection = super.getDirections()[random.nextInt(4)];
            this.updateDirection(newDirection);
            super.setFrameCounter(0);
        } else if (super.getY() == super.getMap().getTileSize() * 9 
                   && super.getDirection() != 'U' 
                   && super.getDirection() != 'D') {
            this.updateDirection('U');
        }
    }

    /**
     * Handles collision with walls by selecting a new random direction.
     * 
     * <p>When the ghost encounters a wall obstacle, this method immediately
     * chooses a new random direction from the available movement options.
     * This approach maintains the ghost's unpredictable behavior even when
     * navigating around obstacles.
     * 
     * <p>The random selection ensures that wall collisions don't create
     * predictable movement patterns, preserving the chaotic nature of
     * the orange ghost's AI strategy.
     * 
     * @see Ghost#getDirections()
     * @see Ghost#updateDirection(char)
     */
    @Override
    protected void handleWallCollision() {
        char newDirection = super.getDirections()[random.nextInt(4)];
        this.updateDirection(newDirection);
    }
}
