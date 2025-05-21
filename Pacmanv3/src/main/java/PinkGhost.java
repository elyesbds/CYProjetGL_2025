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

    @Override
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
}
