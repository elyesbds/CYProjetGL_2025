package map;

import java.awt.Image;
import entities.Entity;

/**
 * Represents a wall entity in the game map.
 * Walls are static obstacles that block player and ghost movement.
 * Extends the base Entity class to inherit common rendering and collision properties.
 */
public class Wall extends Entity {

    /**
     * Constructs a new Wall entity with specified parameters.
     *
     * @param image  the image representing the wall's visual appearance
     * @param x      the x-coordinate of the wall's position in pixels
     * @param y      the y-coordinate of the wall's position in pixels
     * @param width  the width of the wall in pixels
     * @param height the height of the wall in pixels
     * @param map    the game map reference (currently unused in base implementation)
     */
    Wall(Image image, int x, int y, int width, int height, Map map) {
        super(image, x, y, width, height);
    }
}
