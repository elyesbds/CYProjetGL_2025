import java.awt.Image;

public class PinkGhost extends Ghost{

    private int anticipatedTiles = 4;

    PinkGhost(Image image, int x, int y, int width, int height, Map map) {
        super(image, x, y, width, height, map);
    }

    @Override
    protected void decideDirection(){
    	super.setFrameCounter(super.getFrameCounter() + 1);
        if (super.getFrameCounter() == super.getTargetUpdateInterval()){
            ambushTarget();
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
            }
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
