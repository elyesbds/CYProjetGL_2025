package entities.ghosts;
import java.awt.Image;

import map.Map;

public class PinkGhost extends Ghost{

    private int anticipatedTiles = 4;

    public PinkGhost(Image image, int x, int y, int width, int height, Map map) {
        super(image, x, y, width, height, map);
    }

    @Override
    protected void decideDirection(){
    	super.setFrameCounter(super.getFrameCounter() + 1);
        if (super.getFrameCounter() == super.getTargetUpdateInterval() && super.getY() == super.getMap().getTileSize()*9){
            chaseTarget();
            super.setFrameCounter(0);
        }else{
            ambushTarget();
            super.setFrameCounter(0);
        }
    }

    @Override
    protected void handleWallCollision(){
        ambushTarget();
    }

    private void ambushTarget(){
        int pacmanX = super.getMap().getPacman().getX();
        int pacmanY = super.getMap().getPacman().getY();

        char pacmanDir = super.getMap().getPacman().getDirection();

        switch (pacmanDir){
            case 'U':
                pacmanY -= anticipatedTiles * super.getMap().getTileSize();
                pacmanX -= anticipatedTiles * super.getMap().getTileSize();
                break;
            case 'D':
                pacmanY += anticipatedTiles * super.getMap().getTileSize();
                break;
            case 'L':
                pacmanX -= anticipatedTiles * super.getMap().getTileSize();
                break;
            case 'R':
                pacmanX += anticipatedTiles * super.getMap().getTileSize();
                break;
            default:
                pacmanX += anticipatedTiles * super.getMap().getTileSize();
                break;
        }

        char bestDir = findBestDirection(pacmanX, pacmanY);
        this.updateDirection(bestDir);
    }
}
