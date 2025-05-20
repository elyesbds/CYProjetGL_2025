import java.awt.Image;

public class OrangeGhost extends Ghost{

    OrangeGhost(Image image, int x, int y, int width, int height, Map map) {
        super(image, x, y, width, height, map);
        char newDirection = super.getDirections()[random.nextInt(4)];
        this.updateDirection(newDirection);
    }

    @Override
    protected void decideDirection(){
    	
    	super.setFrameCounter(super.getFrameCounter() + 1);

    	if (super.getFrameCounter() == super.getTargetUpdateInterval()){
    		char newDirection = super.getDirections()[random.nextInt(4)];
            this.updateDirection(newDirection);
            super.setFrameCounter(0);
            
        }else if(super.getY() == super.getMap().getTileSize()*9 && super.getDirection() != 'U' && super.getDirection() != 'D'){
            this.updateDirection('U');
        }

    }

    @Override
    protected void handleWallCollision(){
    	char newDirection = super.getDirections()[random.nextInt(4)];
        this.updateDirection(newDirection);
    }
}
