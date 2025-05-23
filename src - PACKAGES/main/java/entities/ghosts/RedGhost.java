package entities.ghosts;
import java.awt.Image;

import map.Map;

public class RedGhost extends Ghost{


    public RedGhost(Image image, int x, int y, int width, int height, Map map) {
        super(image, x, y, width, height, map);
    }

    @Override
    protected void decideDirection(){
    	super.setFrameCounter(super.getFrameCounter() + 1);

        if (super.getFrameCounter() >= super.getTargetUpdateInterval()){
            chaseTarget();
            super.setFrameCounter(0);
        }
    }

    @Override
    protected void handleWallCollision(){
        super.chaseTarget();
    }

}