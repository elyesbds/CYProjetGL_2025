import java.awt.Image;

public class OrangeGhost extends Ghost{

    OrangeGhost(Image image, int x, int y, int width, int height, Map map) {
        super(image, x, y, width, height, map);
        char newDirection = directions[random.nextInt(4)];
        this.updateDirection(newDirection);
    }

    @Override
    protected void decideDirection(){
        frameCounter ++;

        if (frameCounter == changeDirectionInterval){
            char newDirection = directions[random.nextInt(4)];
            this.updateDirection(newDirection);
            frameCounter = 0;
        }else if(this.y == map.tileSize*9 && this.direction != 'U' && this.direction != 'D'){
            this.updateDirection('U');
        }

    }

    @Override
    protected void handleWallCollision(){
        char newDirection = directions[random.nextInt(4)];
        this.updateDirection(newDirection);
    }
}
