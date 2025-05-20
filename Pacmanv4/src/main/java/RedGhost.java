import java.awt.Image;

public class RedGhost extends Ghost{


    RedGhost(Image image, int x, int y, int width, int height, Map map) {
        super(image, x, y, width, height, map);
    }

    @Override
    protected void decideDirection(){
    	super.setFrameCounter(super.getFrameCounter() + 1);
        //frameCounter ++;

        if (super.getFrameCounter() == super.getTargetUpdateInterval()){
            chaseTarget();
            super.setFrameCounter(0);
            
        }
    }

    @Override
    protected void handleWallCollision(){
        chaseTarget();
    }

    private void chaseTarget(){
        int pacmanX = super.getMap().getPacman().getX();
        int pacmanY = super.getMap().getPacman().getY();

        char bestDirection = findBestDirection(pacmanX, pacmanY);
        this.updateDirection(bestDirection);
    }
}
