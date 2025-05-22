import java.awt.*;

public abstract class MobileEntity extends Entity {
	private int startX;
    private int startY;
    private char nextDirection = ' ';
    private char direction;
    private int velocityX;
    private int velocityY;

    private Point center;
    private int radius;
    
    private Map map; // les fantomes et pacman ont besoin de connaitre la map
    
    public MobileEntity(Image img, int x, int y, int width, int height, Map map) {
    	super(img,x,y,width,height);
    	this.startX = x;
    	this.startY = y;
    	this.map = map;
    }

    public void updateNextDirection(char direction){
        this.nextDirection = direction;
    }
    
    public void reset(){
        super.setX(this.startX);
        super.setY(this.startY);
    }

    public abstract void updateVelocity();
    
    public void updateDirection(char direction){
        int tempX = super.getX();
        int tempY = super.getY();

        char prevDirection = this.direction;
        this.direction = direction;
        updateVelocity();

        tempX += this.velocityX;
        tempY += this.velocityY;

        boolean collides = false;
        for(Wall wall : map.getWalls()){
            if(collision(new Entity(null, tempX, tempY, super.getWidth(), super.getHeight()), wall)){
                collides = true;
                break;
            }
        }
        if(collides){
            this.direction = prevDirection;
            updateVelocity();
        } else {
        }
    }

    public boolean isColliding(MobileEntity other) {
        Point center1 = this.getCenter();
        Point center2 = other.getCenter();
        double distance = center1.distance(center2);
        return distance < this.getRadius() + other.getRadius();
    }

    public int getRadius() {
        return super.getWidth() / 2;
    }

    public Point getCenter() {
        return new Point(super.getX() + super.getWidth() / 2, super.getY() + super.getHeight() / 2);
    }


	public int getStartX() {
		return startX;
	}




	public void setStartX(int startX) {
		this.startX = startX;
	}




	public int getStartY() {
		return startY;
	}




	public void setStartY(int startY) {
		this.startY = startY;
	}




	public char getNextDirection() {
		return nextDirection;
	}




	public void setNextDirection(char nextDirection) {
		this.nextDirection = nextDirection;
	}




	public char getDirection() {
		return direction;
	}




	public void setDirection(char direction) {
		this.direction = direction;
	}




	public int getVelocityX() {
		return velocityX;
	}




	public void setVelocityX(int velocityX) {
		this.velocityX = velocityX;
	}




	public int getVelocityY() {
		return velocityY;
	}




	public void setVelocityY(int velocityY) {
		this.velocityY = velocityY;
	}
	
	public Map getMap() {
		return this.map;
	}
	public void setMap(Map map) {
		this.map = map;
	}
}
