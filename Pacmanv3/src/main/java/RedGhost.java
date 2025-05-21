import java.awt.Image;

public class RedGhost extends Ghost{


    RedGhost(Image image, int x, int y, int width, int height, Map map) {
        super(image, x, y, width, height, map);
    }

    @Override
    protected void decideDirection(){
        frameCounter ++;

        if (frameCounter == targetUpdateInterval){
            chaseTarget();
            frameCounter = 0;
        }
    }

    @Override
    protected void handleWallCollision(){
        chaseTarget();
    }

    private void chaseTarget(){
        int pacmanX = map.pacman.x;
        int pacmanY = map.pacman.y;

        char bestDirection = findBestDirection(pacmanX, pacmanY);
        this.updateDirection(bestDirection);
    }
}
