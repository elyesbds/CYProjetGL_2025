import java.awt.*;

public class Food extends Entity{

    public String type;

    Food(Image image, int x, int y, int width, int height, String type){
        super(image, x, y, width, height);
        this.type = type;
    }

    public String getFoodType(){
        return this.type;
    }
}
