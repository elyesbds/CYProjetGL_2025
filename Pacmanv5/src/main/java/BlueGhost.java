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
        
        if (super.getFrameCounter() >= super.getTargetUpdateInterval()) {
            double distanceToPacman = calculateDistance(super.getX(), super.getY(), super.getMap().getPacman().getX(), super.getMap().getPacman().getY());
            
            if (distanceToPacman < distanceBeforeFlee) {
                moveToCorner();
            } 
            else {
                chaseTarget();
            }
            super.setFrameCounter(0);
        }
        if(super.getY() == super.getMap().getTileSize() * 9 && 
           (super.getDirection() == 'L' || super.getDirection() == 'R')) {
            // VÃ©rifier s'il y a un mur au-dessus
            boolean wallAbove = false;
            for (Wall wall : super.getMap().getWalls()) {
                if (collision(new Entity(null, super.getX(), super.getY() - super.getMap().getTileSize()/8, 
                                        super.getWidth(), super.getHeight()), wall)) {
                    wallAbove = true;
                    break;
                }
            }
            
            // S'il n'y a pas de mur au-dessus, monter pour sortir du tunnel
            if (!wallAbove) {
                this.updateDirection('U');
            }}
    }

    @Override
    protected void handleWallCollision() {
        double distanceToPacman = calculateDistance(super.getX(), super.getY(), super.getMap().getPacman().getX(), super.getMap().getPacman().getY());
        
        if (distanceToPacman < distanceBeforeFlee) {
            moveToCorner();
        } else {
            super.chaseTarget();
        }
    }

    private void moveToCorner() {
        char bestDirection = findBestDirection(cornerX, cornerY);
        this.updateDirection(bestDirection);
    }


}