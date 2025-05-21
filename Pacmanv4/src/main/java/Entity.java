
import java.awt.*;

/**
 * Represents a base entity in the game with position, size, and image properties.
 * This class is the parent class for all game objects including walls, food, players, and ghosts.
 */
public class Entity {

    private int x;
    private int y;
    private int width;
    private int height;
    private Image image;

    /**
     * Constructs a new Entity with the specified image, position, and dimensions.
     *
     * @param image  The visual representation of this entity
     * @param x      The x-coordinate of this entity's position
     * @param y      The y-coordinate of this entity's position
     * @param width  The width of this entity
     * @param height The height of this entity
     */
    Entity(Image image, int x, int y, int width, int height){
        this.image = image;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Sets a new image for this entity.
     *
     * @param img The new image to set
     */
    public void setImage(Image img){
        this.image = img;
    }

    /**
     * Determines if two entities are colliding based on their rectangular bounds.
     *
     * @param a The first entity
     * @param b The second entity
     * @return true if the entities are colliding, false otherwise
     */
    public boolean collision(Entity a, Entity b){
        return  a.x < b.x + b.width   &&
                a.x + a.width > b.x   &&
                a.y < b.y + b.height  &&
                a.y + a.height > b.y;
    }

    /**
     * Gets the current x-coordinate of this entity.
     *
     * @return The x-coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Sets the x-coordinate of this entity.
     *
     * @param x The new x-coordinate
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Gets the current y-coordinate of this entity.
     *
     * @return The y-coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Sets the y-coordinate of this entity.
     *
     * @param y The new y-coordinate
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Gets the width of this entity.
     *
     * @return The width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Sets the width of this entity.
     *
     * @param width The new width
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Gets the height of this entity.
     *
     * @return The height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Sets the height of this entity.
     *
     * @param height The new height
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Gets the current image of this entity.
     *
     * @return The image
     */
    public Image getImage() {
        return image;
    }
}
