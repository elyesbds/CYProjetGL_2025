import java.awt.Image;

public class PinkGhost extends Ghost{

    private int anticipatedTiles = 4;

    PinkGhost(Image image, int x, int y, int width, int height, Map map) {
        super(image, x, y, width, height, map);
    }

    @Override
    protected void decideDirection(){
        frameCounter ++;

        if (frameCounter == targetUpdateInterval){
            ambushTarget();
            frameCounter = 0;
        }
    }

    protected void handleWallCollision(){
        ambushTarget();
    }

    private void ambushTarget(){
        int pacmanX = map.pacman.x;
        int pacmanY = map.pacman.y;

        char pacmanDir = map.pacman.direction;

        switch (pacmanDir){
            case 'U':
                pacmanY -= anticipatedTiles * map.tileSize;
                pacmanX -= anticipatedTiles * map.tileSize;
                break;
            case 'D':
                pacmanY += anticipatedTiles * map.tileSize;
                break;
            case 'L':
                pacmanX -= anticipatedTiles * map.tileSize;
                break;
            case 'R':
                pacmanX += anticipatedTiles * map.tileSize;
                break;
            default:
                pacmanX += anticipatedTiles * map.tileSize;
                break;
        }

        char bestDir = findBestDirection(pacmanX, pacmanY);
        this.updateDirection(bestDir);
    }

    private char findBestDirection(int targetX, int targetY){
        char bestDir = ' ';
        double shortestDistance = Double.MAX_VALUE;

        for(char dir : directions){
            if(isOppositeDirection(dir, this.direction)){
                continue;
            }

            int newX = this.x;
            int newY = this.y;

            if (dir == 'U'){
                newY -= map.tileSize/16;
            }
            else if (dir == 'D'){
                newY += map.tileSize/16;
            }
            else if (dir == 'L'){
                newX -= map.tileSize/16;
            }
            else if (dir == 'R'){
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
                double distance = calculateDistance(newX, newY, targetX, targetY);
                
                if (distance < shortestDistance) {
                    shortestDistance = distance;
                    bestDir = dir;
                }
            }
        }
        return bestDir;
    }

}
