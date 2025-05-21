import java.awt.*;
import java.util.Random;
import javax.swing.ImageIcon;

/**
 * Abstract class representing a ghost enemy in the game.
 * This class provides base functionality for all ghost types
 * including movement logic, scared behavior, and collision handling.
 */
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

    /**
     * Constructs a new Ghost with specified image, position, dimensions, and map.
     *
     * @param image  The visual representation of this ghost
     * @param x      The x-coordinate of this ghost's starting position
     * @param y      The y-coordinate of this ghost's starting position
     * @param width  The width of this ghost
     * @param height The height of this ghost
     * @param map    The game map containing all game entities
     */
    Ghost(Image image, int x, int y, int width, int height, Map map){
        super(image, x, y, width, height, map);
        this.isScared = false;
        this.realImage = image;
        
        scaredGhostImage = new ImageIcon(getClass().getResource("/scaredGhost.png")).getImage();
    }

    /**
     * Determines if the ghost is currently in a tunnel section of the map.
     * Ghosts move slower in tunnels.
     *
     * @return true if the ghost is in a tunnel, false otherwise
     */
    public boolean isInTunnel() {
        int tunnelY = 9 * super.getMap().getTileSize(); 
        return (super.getY() >= tunnelY && super.getY() < tunnelY + super.getMap().getTileSize()) &&
               (super.getX() < 3 * super.getMap().getTileSize() || super.getX() > (super.getMap().getCols() - 3) * super.getMap().getTileSize());
    }

    /**
     * Updates the ghost's position and handles collisions with walls and the player.
     * Behavior differs based on whether the ghost is in scared mode or not.
     */
    @Override
    public final void move(){
        if(this.isScared){
            scaredMove();
        }
        else{
            decideDirection();
        }

        double speedMultiplier = this.isInTunnel() ? 0.75 : 1.0;
        
        int oldX = super.getX();
        int oldY = super.getY();
        
        super.setX((int) (super.getX()+super.getVelocityX()*speedMultiplier));
        super.setY((int) (super.getY()+super.getVelocityY()*speedMultiplier));

        if(super.getX() <= 0){
            super.setX(super.getMap().getCols()*super.getMap().getTileSize() - super.getMap().getTileSize());
        }else if(super.getX() >= (super.getMap().getCols()*super.getMap().getTileSize() - super.getMap().getTileSize())){
            super.setX(0);
        }

        boolean hasCollision = false;
        for (Wall wall : super.getMap().getWalls()){
            if(collision(this, wall)){
                hasCollision = true;
                break;
            }
        }
        
        if(hasCollision){
            // Restore previous coordinates in case of collision
            super.setX(oldX);
            super.setY(oldY);
            
            // Handle collision based on ghost state
            if(this.isScared){
                char newDirection = directions[random.nextInt(4)];
                this.updateDirection(newDirection);
            } else {
                handleWallCollision();
            }
        }

        if(collision(this, super.getMap().getPacman())){
            if(this.isScared){
                System.out.println("Collision fantome pacman en mode peur");
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

    /**
     * Abstract method to be implemented by ghost subclasses.
     * Determines the movement direction based on the ghost's unique behavior.
     */
    protected abstract void decideDirection();
    
    /**
     * Abstract method to be implemented by ghost subclasses.
     * Handles wall collisions based on the ghost's unique behavior.
     */
    protected abstract void handleWallCollision();

    /**
     * Determines if two directions are opposites of each other.
     *
     * @param dir1 The first direction
     * @param dir2 The second direction
     * @return true if the directions are opposites, false otherwise
     */
    protected boolean isOppositeDirection(char dir1, char dir2) {
        return (dir1 == 'U' && dir2 == 'D') ||
               (dir1 == 'D' && dir2 == 'U') ||
               (dir1 == 'L' && dir2 == 'R') ||
               (dir1 == 'R' && dir2 == 'L');
    }

    /**
     * Calculates the Euclidean distance between two points.
     *
     * @param x1 The x-coordinate of the first point
     * @param y1 The y-coordinate of the first point
     * @param x2 The x-coordinate of the second point
     * @param y2 The y-coordinate of the second point
     * @return The Euclidean distance between the points
     */
    protected double calculateDistance(int x1, int y1, int x2, int y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    /**
     * Returns the opposite direction of the given direction.
     *
     * @param dir The direction to get the opposite of
     * @return The opposite direction, or 'n' if the input is invalid
     */
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

    /**
     * Handles movement when the ghost is in scared mode.
     * Ghosts move randomly when scared.
     */
    protected void scaredMove(){
        frameCounter++;
        scaredTimer++;

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

    /**
     * Finds the best direction to move towards a target position.
     *
     * @param targetX The x-coordinate of the target position
     * @param targetY The y-coordinate of the target position
     * @return The best direction to move ('U', 'D', 'L', 'R') or ' ' if no valid direction
     */
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

    /**
     * Gets the array of possible movement directions.
     *
     * @return The array of directions ('U', 'D', 'L', 'R')
     */
    public char[] getDirections() {
        return directions;
    }

    /**
     * Sets the array of possible movement directions.
     *
     * @param directions The new array of directions
     */
    public void setDirections(char[] directions) {
        this.directions = directions;
    }

    /**
     * Gets the random number generator used by this ghost.
     *
     * @return The Random instance
     */
    public Random getRandom() {
        return random;
    }

    /**
     * Sets the random number generator used by this ghost.
     *
     * @param random The new Random instance
     */
    public void setRandom(Random random) {
        this.random = random;
    }

    /**
     * Gets the normal image of this ghost (non-scared state).
     *
     * @return The normal image
     */
    public Image getRealImage() {
        return realImage;
    }

    /**
     * Sets the normal image of this ghost.
     *
     * @param realImage The new normal image
     */
    public void setRealImage(Image realImage) {
        this.realImage = realImage;
    }

    /**
     * Gets the scared image of this ghost.
     *
     * @return The scared image
     */
    public Image getScaredGhostImage() {
        return scaredGhostImage;
    }

    /**
     * Sets the scared image of this ghost.
     *
     * @param scaredGhostImage The new scared image
     */
    public void setScaredGhostImage(Image scaredGhostImage) {
        this.scaredGhostImage = scaredGhostImage;
    }

    /**
     * Checks if this ghost is in scared mode.
     *
     * @return true if scared, false otherwise
     */
    public boolean isScared() {
        return isScared;
    }

    /**
     * Sets the scared state of this ghost.
     * When scared, ghosts use different images and movement patterns.
     *
     * @param isScared true to make the ghost scared, false otherwise
     */
    public void setScared(boolean isScared) {
        this.isScared = isScared;
        if (isScared) {
            this.scaredTimer = 0;
            this.frameCounter = 0;
            this.setImage(this.scaredGhostImage);
            this.updateDirection(this.getOppositeDirection(this.getDirection()));
            System.out.println("Ghost entered scared mode");
        } 
        else {
            this.setImage(this.realImage);
        }
    }

    /**
     * Gets the frame counter for movement timing.
     *
     * @return The current frame counter value
     */
    public int getFrameCounter() {
        return frameCounter;
    }

    /**
     * Sets the frame counter for movement timing.
     *
     * @param frameCounter The new frame counter value
     */
    public void setFrameCounter(int frameCounter) {
        this.frameCounter = frameCounter;
    }

    /**
     * Gets the scared timer counting how long the ghost has been in scared mode.
     *
     * @return The current scared timer value
     */
    public int getScaredTimer() {
        return scaredTimer;
    }

    /**
     * Sets the scared timer.
     *
     * @param scaredTimer The new scared timer value
     */
    public void setScaredTimer(int scaredTimer) {
        this.scaredTimer = scaredTimer;
    }

    /**
     * Gets the maximum duration for which a ghost can remain scared.
     *
     * @return The maximum scared timer value
     */
    public int getEndScaredTimer() {
        return endScaredTimer;
    }

    /**
     * Gets the interval for updating target positions.
     *
     * @return The target update interval
     */
    public int getTargetUpdateInterval() {
        return targetUpdateInterval;
    }

    /**
     * Gets the interval for changing direction in scared mode.
     *
     * @return The direction change interval
     */
    public int getChangeDirectionInterval() {
        return changeDirectionInterval;
    }
}
