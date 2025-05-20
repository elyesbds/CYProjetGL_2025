import java.awt.Image;

public class BlueGhost extends Ghost{

    private final int distanceBeforeFlee = 8 * map.tileSize;
    
    private final int cornerX = map.tileSize;
    private final int cornerY = (map.rows - 2) * map.tileSize;

    BlueGhost(Image image, int x, int y, int width, int height, Map map) {
        super(image, x, y, width, height, map);
    }

   @Override
    protected void decideDirection() {
        frameCounter++;
        
        if (frameCounter == targetUpdateInterval) {
            double distanceToPacman = calculateDistance(this.x, this.y, map.pacman.x, map.pacman.y);
            
            if (distanceToPacman < distanceBeforeFlee) {
                moveToCorner();
            } 
            else {
                chaseTarget();
            }
            
            frameCounter = 0;
        }
    }

    @Override
    protected void handleWallCollision() {
        double distanceToPacman = calculateDistance(this.x, this.y, map.pacman.x, map.pacman.y);
        
        if (distanceToPacman < distanceBeforeFlee) {
            moveToCorner();
        } else {
            chaseTarget();
        }
    }

    private void moveToCorner() {
        char bestDirection = findBestDirection(cornerX, cornerY);
        this.updateDirection(bestDirection);
    }

    private void chaseTarget() {
        int pacmanX = map.pacman.x;
        int pacmanY = map.pacman.y;
        
        char bestDirection = findBestDirection(pacmanX, pacmanY);
        this.updateDirection(bestDirection);
    }
}

