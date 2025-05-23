package entities.ghosts;

import java.awt.Image;

import map.Map;

/**
 * The PinkGhost class represents a specific type of ghost in the game.
 * This ghost attempts to ambush the player by anticipating their movement direction
 * and positioning itself accordingly. It extends the base Ghost class and implements
 * unique chasing behavior.
 */
public class PinkGhost extends Ghost {

    /** The number of tiles the ghost anticipates ahead of Pacman's current direction. */
    private int anticipatedTiles = 4;

    /**
     * Constructs a new PinkGhost with the specified parameters.
     *
     * @param image   the Image object representing the ghost's visual appearance
     * @param x       the initial x-coordinate of the ghost
     * @param y       the initial y-coordinate of the ghost
     * @param width   the width of the ghost's bounding box
     * @param height  the height of the ghost's bounding box
     * @param map     the game map reference
     */
    public PinkGhost(Image image, int x, int y, int width, int height, Map map) {
        super(image, x, y, width, height, map);
    }

    /**
     * Determines the ghost's movement direction based on game state.
     * The ghost alternates between chasing and ambushing behaviors depending
     * on its vertical position on the map.
     */
    @Override
    protected void decideDirection() {
        super.setFrameCounter(super.getFrameCounter() + 1);
        if (super.getFrameCounter() == super.getTargetUpdateInterval() && super.getY() == super.getMap().getTileSize() * 9) {
            chaseTarget();
            super.setFrameCounter(0);
        } else {
            ambushTarget();
            super.setFrameCounter(0);
        }
    }

    /**
     * Handles collision with walls by initiating an ambush behavior.
     * When the ghost hits a wall, it recalculates its path to ambush Pacman.
     */
    @Override
    protected void handleWallCollision() {
        ambushTarget();
    }

    /**
     * Calculates an ambush position based on Pacman's current direction
     * and moves toward that position. The ambush point is calculated by
     * anticipating Pacman's movement several tiles ahead in its current direction.
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
