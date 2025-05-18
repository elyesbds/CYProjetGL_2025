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

    private char findBestDirection(int targetX, int targetY) {
        char bestDir = ' ';
        double shortestDistance = Double.MAX_VALUE;
        
        for (char dir : directions) {
            if (isOppositeDirection(dir, this.direction)) {
                continue;
            }
            
            int newX = this.x;
            int newY = this.y;
            
            if (dir == 'U') {
                newY -= map.tileSize/16;
            } else if (dir == 'D') {
                newY += map.tileSize/16;
            } else if (dir == 'L') {
                newX -= map.tileSize/16;
            } else if (dir == 'R') {
                newX += map.tileSize/16;
            }
            
            boolean collides = false;
            for (Wall wall : map.walls) {
                if (collision(new Entity(null, newX, newY, this.width, this.height, map), wall)) {
                    collides = true;
                    break;
                }
            }
            
            if (!collides) {
                double distance = Math.sqrt(Math.pow(newX - targetX, 2) + Math.pow(newY - targetY, 2));
                
                if (distance < shortestDistance) {
                    shortestDistance = distance;
                    bestDir = dir;
                }
            }
        }
        
        return bestDir;
    }

}

