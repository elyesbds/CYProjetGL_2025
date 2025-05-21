import java.awt.*;

public class Entity {

    public final Map map;

    public int x;
    public int y;
    public int width;
    public int height;
    Image image;
    
    protected Point center;
    protected int radius;

    public int startX;
    public int startY;
    public char nextDirection = ' ';
    public char direction;
    public int velocityX;
    public int velocityY;

    Entity(Image image, int x, int y, int width, int height, Map map){
        this.image = image;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.startX = x;
        this.startY = y;
        this.map = map;
    }

    public int getRadius() {
        return width / 2;
    }

    public Point getCenter() {
        return new Point(x + width / 2, y + height / 2);
    }

    public void setImage(Image img){
        this.image = img;
    }

    public void updateDirection(char direction){
        int tempX = this.x;
        int tempY = this.y;

        char prevDirection = this.direction;
        this.direction = direction;
        updateVelocity();

        tempX += this.velocityX;
        tempY += this.velocityY;

        boolean collides = false;
        for(Wall wall : map.walls){
            if(collision(new Entity(null, tempX, tempY, this.width, this.height, map), wall)){
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


    public void updateNextDirection(char direction){
        this.nextDirection = direction;
    }

    public void updateVelocity(){
        if (this.direction == 'U'){
            this.velocityX = 0;
            this.velocityY = -map.tileSize/16;
        }
        else if (this.direction == 'D'){
            this.velocityX = 0;
            this.velocityY = map.tileSize/16;
        }
        else if (this.direction == 'L'){
            this.velocityX = -map.tileSize/16;
            this.velocityY = 0;
        }
        else if (this.direction == 'R'){
            this.velocityX = map.tileSize/16;
            this.velocityY = 0;
        }
    }

    public boolean collision(Entity a, Entity b){
        return  a.x < b.x + b.width   &&
                a.x + a.width > b.x   &&
                a.y < b.y + b.height  &&
                a.y + a.height > b.y;
    }

    public boolean isColliding(Entity other) {
        Point center1 = this.getCenter();
        Point center2 = other.getCenter();
        double distance = center1.distance(center2);
        return distance < this.getRadius() + other.getRadius();
    }

    public void reset(){
        this.x = this.startX;
        this.y = this.startY;
    }
}
