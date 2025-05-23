package entities.ghosts;

import java.awt.Image;

import map.Map;

/**
 * The RedGhost class represents the red ghost in the game, typically known for its
 * aggressive chasing behavior. This ghost directly targets Pacman's current position
 * without any strategic offset, making it the most straightforward pursuer.
 * It extends the base Ghost class and implements its specific movement patterns.
 */
public class RedGhost extends Ghost {

    /**
     * Constructs a new RedGhost with the specified parameters.
     *
     * @param image   the Image object representing the ghost's visual appearance
     * @param x       the initial x-coordinate of the ghost in pixels
     * @param y       the initial y-coordinate of the ghost in pixels
     * @param width   the width of the ghost's bounding box in pixels
     * @param height  the height of the ghost's bounding box in pixels
     * @param map     the game map reference used for pathfinding and collision detection
     */
    public RedGhost(Image image, int x, int y, int width, int height, Map map) {
        super(image, x, y, width, height, map);
    }

    /**
     * Determines the ghost's movement direction based on game state.
     * The red ghost directly chases Pacman's current position at regular intervals
     * defined by the target update interval. This creates persistent pursuit behavior.
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
     * Handles collision with walls by recalculating the path to Pacman.
     * When the ghost hits a wall, it immediately re-evaluates the shortest path
     * to Pacman's current position and continues pursuit.
     */
    @Override
    protected void handleWallCollision() {
        super.chaseTarget();
    }
}
