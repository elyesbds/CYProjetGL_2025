import java.awt.*;

public class Entity {

    //private final Map map;

    private int x;
    private int y;
    private int width;
    private int height;
    private Image image;


    Entity(Image image, int x, int y, int width, int height){
        this.image = image;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        //this.startX = x;
        //this.startY = y;
        //this.map = map;
    }

    public void setImage(Image img){
        this.image = img;
    }

    


    

    public boolean collision(Entity a, Entity b){
        return  a.x < b.x + b.width   &&
                a.x + a.width > b.x   &&
                a.y < b.y + b.height  &&
                a.y + a.height > b.y;
    }

    

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	

	public Image getImage() {
		return image;
	}
}
