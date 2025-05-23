package entities.ghosts;
import java.awt.Image;

import map.Map;

public class OrangeGhost extends Ghost{

    public OrangeGhost(Image image, int x, int y, int width, int height, Map map) {
        super(image, x, y, width, height, map);
        char newDirection = super.getDirections()[random.nextInt(4)];
        this.updateDirection(newDirection);
    }

    @Override
    protected void decideDirection(){
    	
    	super.setFrameCounter(super.getFrameCounter() + 1);

    	if (super.getFrameCounter() == super.getChangeDirectionInterval()){
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
