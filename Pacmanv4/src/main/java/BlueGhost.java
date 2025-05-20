import java.awt.Image;

public class BlueGhost extends Ghost{

    private final int distanceBeforeFlee = 8 * super.getMap().getTileSize();
    
    private final int cornerX = super.getMap().getTileSize();
    private final int cornerY = (super.getMap().getRows() - 2) * super.getMap().getTileSize();

    BlueGhost(Image image, int x, int y, int width, int height, Map map) {
        super(image, x, y, width, height, map);
    }

   @Override
    protected void decideDirection() {
       super.setFrameCounter(super.getFrameCounter() + 1); 
	   //frameCounter++;
        
        if (super.getFrameCounter() == super.getTargetUpdateInterval()) {
            double distanceToPacman = calculateDistance(super.getX(), super.getY(), super.getMap().getPacman().getX(), super.getMap().getPacman().getY());
            
            if (distanceToPacman < distanceBeforeFlee) {
                moveToCorner();
            } 
            else {
                chaseTarget();
            }
            super.setFrameCounter(0);
        }
    }

    @Override
    protected void handleWallCollision() {
        double distanceToPacman = calculateDistance(super.getX(), super.getY(), super.getMap().getPacman().getX(), super.getMap().getPacman().getY());
        
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
        int pacmanX = super.getMap().getPacman().getX();
        int pacmanY = super.getMap().getPacman().getY();
        
        char bestDirection = findBestDirection(pacmanX, pacmanY);
        this.updateDirection(bestDirection);
    }
}

