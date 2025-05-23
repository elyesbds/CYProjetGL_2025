package map;
import java.awt.Image;

import entities.Entity;

public class Wall extends Entity{

    Wall(Image image, int x, int y, int width, int height, Map map){
        super(image, x, y, width, height);
    }
}
