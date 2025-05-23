package map;
import java.awt.*;

import entities.Entity;
/***
 * Class representing a food, extending a non mobile Entity.
 * A Food object has a type and a score he gives when ate.
 */

public class Food extends Entity{

    private String type;
    private int score;
    /***
     * Constructs a Food object with an image, a x and y position, width, height his type and score given.
     * @param image Visual representation of the food
     * @param x X coordinate
     * @param y Y coordinate
     * @param width Width of the food
     * @param height Height of the food
     * @param type Type of the food
     * @param score Score given when ate
     */
    Food(Image image, int x, int y, int width, int height, String type,int score){
        super(image, x, y, width, height);
        this.type = type;
        this.score = score;
    }
    /**
     * Gets the type of the food
     * @return type The type of the food
     */
    public String getFoodType(){
        return this.type;
    }
    /***
     * Gets the score of the food
     * @return score The score given to the player who ate this food
     */
    public int getScore() {
    	return this.score;
    }
}
