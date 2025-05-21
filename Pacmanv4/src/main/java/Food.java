import java.awt.*;

public class Food extends Entity{

    private String type;
    private int score;

    Food(Image image, int x, int y, int width, int height, String type,int score){
        super(image, x, y, width, height);
        this.type = type;
        this.score = score;
    }

    public String getFoodType(){
        return this.type;
    }
    public int getScore() {
    	return this.score;
    }
}
