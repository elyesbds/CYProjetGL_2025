import java.awt.*;
import java.util.Random;

import javax.swing.ImageIcon;

public abstract class Ghost extends MobileEntity implements Mobile{

    private char[] directions = {'U', 'D', 'L', 'R'};
    Random random = new Random();
    private Image realImage;
    private Image scaredGhostImage; 

    private boolean isScared;
    private int frameCounter = 0;
    private int scaredTimer = 0;
    private final int endScaredTimer = 300;
    private final int targetUpdateInterval = 1;
    private final int changeDirectionInterval = 60;

    Ghost(Image image, int x, int y, int width, int height, Map map){
        super(image, x, y, width, height, map);
        this.isScared = false;
        this.realImage = image;
        
        scaredGhostImage = new ImageIcon(getClass().getResource("/scaredGhost.png")).getImage();
    }

    public boolean isInTunnel() {
    int tunnelY = 9 * super.getMap().getTileSize(); 
    return (super.getY() >= tunnelY && super.getY() < tunnelY + super.getMap().getTileSize()) &&
           (super.getX() < 3 * super.getMap().getTileSize() || super.getX() > (super.getMap().getCols() - 3) * super.getMap().getTileSize());
}

    @Override
    public final void move(){
        if(this.isScared){
            scaredMove();
        }
        else{
            decideDirection();
        }

        double speedMultiplier = this.isInTunnel() ? 0.75 : 1.0;
        
        super.setX((int) (super.getX()+super.getVelocityX()*speedMultiplier)); //forcer l'entier
        super.setY((int) (super.getY()+super.getVelocityY()*speedMultiplier));

        if(super.getX() <= 0){
        	super.setX(super.getMap().getCols()*super.getMap().getTileSize() - super.getMap().getTileSize());
            //this.x = map.cols*map.tileSize - map.tileSize;
        }else if(super.getX() >= (super.getMap().getCols()*super.getMap().getTileSize() - super.getMap().getTileSize())){
            super.setX(0);
        }

        for (Wall wall : super.getMap().getWalls()){
            if(collision(this, wall)){
            	super.setX((int) (super.getVelocityX() * speedMultiplier));
            	super.setY((int) (super.getVelocityY() * speedMultiplier));
                
                handleWallCollision();
                break;
            }
        }

        if(collision(this, super.getMap().getPacman())){
            if(this.isScared){
            	super.setX(super.getStartX());
            	super.setY(super.getStartY());
                super.getMap().getPacman().setScore(super.getMap().getPacman().getScore()+50);
            }
            else{
                super.getMap().getPacman().setLives(super.getMap().getPacman().getLives()-1);
            if(super.getMap().getPacman().getLives() == 0){
                super.getMap().getPacman().setGameOver(true);
                return;
            }
            super.getMap().resetPositions();
            } 
        }
    }

    protected abstract void decideDirection();
    protected abstract void handleWallCollision();

    protected boolean isOppositeDirection(char dir1, char dir2) {
        return (dir1 == 'U' && dir2 == 'D') ||
               (dir1 == 'D' && dir2 == 'U') ||
               (dir1 == 'L' && dir2 == 'R') ||
               (dir1 == 'R' && dir2 == 'L');
    }

    protected double calculateDistance(int x1, int y1, int x2, int y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    protected char getOppositeDirection(char dir){
        switch(dir){
            case 'U':
                return 'D';
            case 'D':
                return 'U';
            case 'L':
                return 'R';
            case 'R':
                return 'L';
            default:
                return 'n';
        }
    }

    protected void scaredMove(){
        frameCounter++;
            scaredTimer ++;

            if (frameCounter == changeDirectionInterval){
                char newDirection = directions[random.nextInt(4)];
                this.updateDirection(newDirection);
                frameCounter = 0;
            }else if(super.getY() == super.getMap().getTileSize()*9 && super.getDirection() != 'U' && super.getDirection() != 'D'){
                this.updateDirection('U');
            }
            if(scaredTimer == endScaredTimer){
                this.isScared = false;
                this.setImage(this.realImage);
            }
    }

    protected char findBestDirection(int targetX, int targetY){
        char bestDir = ' ';
        double shortestDistance = Double.MAX_VALUE;

        for(char dir : directions){
            if(isOppositeDirection(dir, super.getDirection())){
                continue;
            }

            int newX = super.getX();
            int newY = super.getY();

            if (dir == 'U'){
                newY -= super.getMap().getTileSize()/16;
            }
            else if (dir == 'D'){
                newY += super.getMap().getTileSize()/16;
            }
            else if (dir == 'L'){
                newX -= super.getMap().getTileSize()/16;
            }
            else if (dir == 'R'){
                newX += super.getMap().getTileSize()/16;
            }

            boolean collides = false;
            for (Wall wall : super.getMap().getWalls()) {
                if (collision(new Entity(null, newX, newY, super.getWidth(), super.getHeight()), wall)) {
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

	public char[] getDirections() {
		return directions;
	}

	public void setDirections(char[] directions) {
		this.directions = directions;
	}

	public Random getRandom() {
		return random;
	}

	public void setRandom(Random random) {
		this.random = random;
	}

	public Image getRealImage() {
		return realImage;
	}

	public void setRealImage(Image realImage) {
		this.realImage = realImage;
	}

	public Image getScaredGhostImage() {
		return scaredGhostImage;
	}

	public void setScaredGhostImage(Image scaredGhostImage) {
		this.scaredGhostImage = scaredGhostImage;
	}

	public boolean isScared() {
		return isScared;
	}

	public void setScared(boolean isScared) {
		this.isScared = isScared;
	}

	public int getFrameCounter() {
		return frameCounter;
	}

	public void setFrameCounter(int frameCounter) {
		this.frameCounter = frameCounter;
	}

	public int getScaredTimer() {
		return scaredTimer;
	}

	public void setScaredTimer(int scaredTimer) {
		this.scaredTimer = scaredTimer;
	}

	public int getEndScaredTimer() {
		return endScaredTimer;
	}

	public int getTargetUpdateInterval() {
		return targetUpdateInterval;
	}

	public int getChangeDirectionInterval() {
		return changeDirectionInterval;
	}
}
