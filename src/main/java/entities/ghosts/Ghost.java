package entities.ghosts;

import java.awt.Image;
import java.util.Random;
import javax.swing.ImageIcon;
import entities.Entity;
import entities.MobileEntity;
import map.Map;
import map.Wall;

/**
 * Abstract base-class for every ghost in the Pac-Man game.
 * <p>
 * It handles common behaviour such as:
 * <ul>
 *   <li>movement and velocity update,</li>
 *   <li>frightened / eaten states with the corresponding sprites,</li>
 *   <li>collision detection against walls and Pac-Man,</li>
 *   <li>basic path-finding helpers shared by all concrete ghosts.</li>
 * </ul>
 * Concrete subclasses (Blinky, Pinky, Inky, Clyde…) only need to
 * implement {@link #decideDirection()} and {@link #handleWallCollision()}
 * to provide their individual AI.
 * </p>
 *
 * @author  Equipe 1
 * @version 6.0
 */
public abstract class Ghost extends MobileEntity {

    /* ------------------------------------------------------- */
    /*                      CONSTANTS / FIELDS                 */
    /* ------------------------------------------------------- */

    /** Allowed movement directions. */
    private char[] directions = { 'U', 'D', 'L', 'R' };

    /** Shared pseudo-random generator (used for frightened move). */
    private Random random = new Random();

    /* --- Sprites --- */
    private Image realImage;                 // normal sprite
    private Image scaredGhostImage;          // blue frightened ghost
    private Image whiteScaredGhostImage;     // flashing white sprite
    private Image eatenGhostImage;           // returning-eyes sprite

    /* --- State flags --- */
    private boolean isScared;
    private boolean isEaten;

    /* --- Timers / Counters --- */
    private int frameCounter       = 0;
    private int scaredTimer        = 0;
    private int respawnTimer       = 0;
    private final int respawnDelay = 180;
    private final int endScaredTimer;
    private final int targetUpdateInterval   = 1;
    private final int changeDirectionInterval = 60;

    /* ------------------------------------------------------- */
    /*                       CONSTRUCTOR                       */
    /* ------------------------------------------------------- */

    /**
     * Creates a new ghost.
     *
     * @param image   normal sprite (non-frightened)
     * @param x       initial x-coordinate (pixels)
     * @param y       initial y-coordinate (pixels)
     * @param width   sprite width
     * @param height  sprite height
     * @param map     reference to the current game map
     */
    Ghost(Image image, int x, int y, int width, int height, Map map) {
        super(image, x, y, width, height, map);
        this.isScared  = false;
        this.realImage = image;

        /* Frightened duration grows shorter after level 5. */
        endScaredTimer = super.getMap().getLevel() >= 5 ? 200 : 300;

        /* Load additional sprites from resources. */
        scaredGhostImage      = new ImageIcon(getClass().getResource("/scaredGhost.png")).getImage();
        eatenGhostImage       = new ImageIcon(getClass().getResource("/ghostEyes.png")).getImage();
        whiteScaredGhostImage = new ImageIcon(getClass().getResource("/whiteScaredGhost.png")).getImage();
    }

    /* ------------------------------------------------------- */
    /*                    HIGH-LEVEL BEHAVIOUR                 */
    /* ------------------------------------------------------- */

    /**
     * Default chasing : aim directly at Pac-Man’s current tile
     * using a simple greedy step (subclasses may override).
     */
    public void chaseTarget() {
        int pacmanX = super.getMap().getPacman().getX();
        int pacmanY = super.getMap().getPacman().getY();

        char bestDirection = findBestDirection(pacmanX, pacmanY);
        this.updateDirection(bestDirection);
    }

    /**
     * Core update called every frame by the {@code GameEngine}.
     * <p>
     * - Handles frightened / eaten timers<br>
     * - Executes AI decision<br>
     * - Applies movement with tunnel wrap-around<br>
     * - Detects collisions with walls and Pac-Man
     * </p>
     */
    public final void move() {
        /* --- Eyes returning to the lair --- */
        if (isEaten) {
            respawnTimer++;
            if (respawnTimer >= respawnDelay) {
                isEaten = false;
                setScared(false);
            }
            return;
        }

        /* --- Decide direction depending on state --- */
        if (isScared) {
            scaredMove();
        } else {
            decideDirection();
        }

        /* --- Apply movement (slower when frightened) --- */
        double speedMultiplier = isScared ? 0.6 : 1.0;
        int oldX = super.getX();
        int oldY = super.getY();

        int vx = (int) Math.round(super.getVelocityX() * speedMultiplier);
        int vy = (int) Math.round(super.getVelocityY() * speedMultiplier);
        super.setX(oldX + vx);
        super.setY(oldY + vy);

        /* --- Horizontal wrap-around tunnels --- */
        if (super.getX() <= 0) {
            super.setX(super.getMap().getCols() * super.getMap().getTileSize() - super.getMap().getTileSize());
        } else if (super.getX() >= (super.getMap().getCols() * super.getMap().getTileSize() - super.getMap().getTileSize())) {
            super.setX(0);
        }

        /* --- Wall collision --- */
        boolean collided = false;
        for (Wall wall : super.getMap().getWalls()) {
            if (collision(this, wall)) {
                collided = true;
                break;
            }
        }
        if (collided) {
            /* Restore previous position */
            super.setX(oldX);
            super.setY(oldY);

            /* Behaviour depends on current mode */
            if (isScared) {
                /* Random new direction when frightened */
                char newDir = directions[random.nextInt(4)];
                updateDirection(newDir);
            } else {
                handleWallCollision();
            }
        }

        /* --- Collision with Pac-Man --- */
        if (isColliding(super.getMap().getPacman())) {
            if (isScared) {
                setEaten(true);
                super.getMap().getPacman().setScore(super.getMap().getPacman().getScore() + 50);
            } else {
                super.getMap().getPacman().setLives(super.getMap().getPacman().getLives() - 1);
                setRespawnTimer(respawnDelay);

                if (super.getMap().getPacman().getLives() == 0) {
                    super.getMap().getPacman().setGameOver(true);
                    super.getMap().setLevel(0);
                    return;
                }
                super.getMap().resetPositions();
            }
        }
    }

    /* ------------------------------------------------------- */
    /*       LOW-LEVEL MOVEMENT / VELOCITY CALCULATION         */
    /* ------------------------------------------------------- */

    /**
     * Recomputes velocity according to direction, tile size and level speed.
     * Frightened ghosts automatically slow down (handled in {@link #move()}).
     */
    @Override
    public void updateVelocity() {
        int s           = 14;                                 // base speed divisor
        int speedFactor = super.getMap().getLevel() >= 10 ? s / 2 : s;
        int tileSize    = super.getMap().getTileSize();
        int step        = Math.round(tileSize / speedFactor);

        alignToGrid(step);                                    // keep centred on grid

        switch (super.getDirection()) {
            case 'U' -> { super.setVelocityX(0);   super.setVelocityY(-step); }
            case 'D' -> { super.setVelocityX(0);   super.setVelocityY(step);  }
            case 'L' -> { super.setVelocityX(-step); super.setVelocityY(0);  }
            case 'R' -> { super.setVelocityX(step);  super.setVelocityY(0);  }
        }
    }

    /* ------------------------------------------------------- */
    /*                ABSTRACT AI HOOKS FOR SUBCLASSES         */
    /* ------------------------------------------------------- */

    /** Computer-player AI to pick a new direction each frame (non-frightened). */
    protected abstract void decideDirection();

    /** Fine-grained reaction when bumping into a wall (non-frightened). */
    protected abstract void handleWallCollision();

    /* ------------------------------------------------------- */
    /*                 HELPER METHODS (PUBLIC / PROTECTED)     */
    /* ------------------------------------------------------- */

    /**
     * Returns {@code true} if {@code dir1} is the opposite of {@code dir2}.
     *
     * @param dir1 first direction (U,D,L,R)
     * @param dir2 second direction (U,D,L,R)
     * @return {@code true} if opposite, otherwise {@code false}
     */
    protected boolean isOppositeDirection(char dir1, char dir2) {
        return (dir1 == 'U' && dir2 == 'D') ||
               (dir1 == 'D' && dir2 == 'U') ||
               (dir1 == 'L' && dir2 == 'R') ||
               (dir1 == 'R' && dir2 == 'L');
    }

    /**
     * Euclidean distance helper.
     *
     * @param x1 first x
     * @param y1 first y
     * @param x2 second x
     * @param y2 second y
     * @return distance in pixels
     */
    protected double calculateDistance(int x1, int y1, int x2, int y2) {
        return Math.hypot(x2 - x1, y2 - y1);
    }

    /**
     * Converts a direction to its opposite.
     *
     * @param dir input direction
     * @return opposite direction or {@code 'n'} if invalid
     */
    protected char getOppositeDirection(char dir) {
        return switch (dir) {
            case 'U' -> 'D';
            case 'D' -> 'U';
            case 'L' -> 'R';
            case 'R' -> 'L';
            default  -> 'n';
        };
    }

    /**
     * Frightened random walk with flashing sprite near the end.
     * Called every frame while {@code isScared == true}.
     */
    protected void scaredMove() {
        frameCounter++;
        scaredTimer++;

        /* Randomly change direction every N frames */
        if (frameCounter == changeDirectionInterval) {
            char newDir = directions[random.nextInt(4)];
            if (!isOppositeDirection(newDir, super.getDirection())) {
                updateDirection(newDir);
            }
            frameCounter = 0;
        } else if (super.getY() == super.getMap().getTileSize() * 9 &&
                   super.getDirection() != 'U' && super.getDirection() != 'D') {
            updateDirection('U');                    // force upward when in tunnel
        }

        /* End of frightened mode */
        if (scaredTimer >= endScaredTimer) {
            setScared(false);
            setImage(realImage);
            frameCounter = 0;
            decideDirection();
        }
        /* Flash white during the last 120 frames */
        else if (scaredTimer >= endScaredTimer - 120) {
            if (scaredTimer % 10 == 0) {
                setImage(getImage() == scaredGhostImage ? whiteScaredGhostImage : scaredGhostImage);
            }
        }
    }

    /**
     * Greedy search for the best immediate direction towards a target tile.
     *
     * @param targetX target x-coordinate (pixels)
     * @param targetY target y-coordinate (pixels)
     * @return chosen direction (U,D,L,R) or space if blocked
     */
    protected char findBestDirection(int targetX, int targetY) {
        char   bestDir         = ' ';
        double shortestDist    = Double.MAX_VALUE;

        for (char dir : directions) {
            if (isOppositeDirection(dir, super.getDirection())) continue;

            /* Simulate one step */
            int newX = super.getX();
            int newY = super.getY();
            int step = super.getMap().getTileSize() / 8;

            switch (dir) {
                case 'U' -> newY -= step;
                case 'D' -> newY += step;
                case 'L' -> newX -= step;
                case 'R' -> newX += step;
            }

            /* Check wall collision for this tentative move */
            boolean collides = false;
            for (Wall wall : super.getMap().getWalls()) {
                if (collision(new Entity(null, newX, newY, super.getWidth(), super.getHeight()), wall)) {
                    collides = true;
                    break;
                }
            }
            if (collides) continue;

            /* Keep the direction yielding the shortest distance */
            double dist = calculateDistance(newX, newY, targetX, targetY);
            if (dist < shortestDist) {
                shortestDist = dist;
                bestDir      = dir;
            }
        }
        return bestDir;
    }

    /* ------------------------------------------------------- */
    /*                 STATE MANAGEMENT (getters/setters)      */
    /* ------------------------------------------------------- */

    public void setRespawnTimer(int timer){
        this.respawnTimer = timer;
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
        if (isScared && isEaten) {
        }
        else if(isScared) {
            this.frameCounter = 0;
            this.setImage(this.scaredGhostImage);
            this.updateDirection(this.getOppositeDirection(this.getDirection()));
        }
        else {
            this.setImage(this.realImage);
            
        }
        this.scaredTimer = 0;
    }

    public void setEaten(boolean isEaten){
        this.isEaten = isEaten;
        if(isEaten){
            this.setImage(this.eatenGhostImage);
            super.setX(super.getStartX());
            super.setY(super.getStartY());

        }
        else{
            this.setImage(this.realImage);
            this.decideDirection();
        }
        this.respawnTimer = 0;
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
