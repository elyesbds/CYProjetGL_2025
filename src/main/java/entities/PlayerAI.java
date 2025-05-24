package entities;

import neural.*;
import map.*;
import entities.ghosts.*;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 * {@code PlayerAI} is an extension of the {@link Player} class that uses a {@link NeuralNet}
 * to control Pac-Man's decisions in a game. The neural network takes environmental inputs
 * such as food position and ghost data, and outputs direction preferences.
 *
 * <p>This class includes logic for movement, collision avoidance, grid alignment,
 * and emergency evasion from nearby threats.</p>
 *
 * <p>The AI reacts dynamically to the game state, choosing directions using neural network output
 * and fallback strategies if the preferred direction is blocked.</p>
 * 
 * @see NeuralNet
 * @see entities.ghosts.Ghost
 * @see map.Map
 */
public class PlayerAI extends Player {

    /** The neural network brain controlling this AI player. */
    private NeuralNet brain;

    /** Flag indicating whether food was eaten during the current move. */
    private boolean ateFoodThisStep = false;

    /** Last recorded grid position, used to avoid backtracking. */
    private Point lastPos = null;

    /**
     * Constructs an AI-controlled player.
     *
     * @param image  the player's sprite
     * @param x      starting x position
     * @param y      starting y position
     * @param width  player width
     * @param height player height
     * @param map    game map reference
     * @param net    neural network controlling behavior
     */
    public PlayerAI(Image image, int x, int y, int width, int height, Map map, NeuralNet net) {
        super(image, x, y, width, height, map);
        this.brain = net;
    }

    /**
     * Determines and performs the AI player's movement.
     * The decision is made based on neural network output unless emergency evasion is needed.
     */
    @Override
    public void move() {
        this.ateFoodThisStep = false;

        if (evadeDanger()) {
            super.move();
            return; // Evasion in progress, skip AI control
        }

        Point currentPos = new Point(getX(), getY());
        int oldScore = this.getScore();
        char current = this.getDirection();

        if (isAlignedWithGrid()) {
            char next = getNextDirection(current, lastPos);
            this.setDirection(next);

            switch (next) {
                case 'U':
                    setVelocityY(-getSpeed());
                    setVelocityX(0);
                    break;
                case 'D':
                    setVelocityY(getSpeed());
                    setVelocityX(0);
                    break;
                case 'L':
                    setVelocityX(-getSpeed());
                    setVelocityY(0);
                    break;
                case 'R':
                    setVelocityX(getSpeed());
                    setVelocityY(0);
                    break;
            }
        }

        super.move();
        this.ateFoodThisStep = this.getScore() > oldScore;
        lastPos = currentPos;
    }

    /**
     * Chooses the next direction based on neural network output and game state.
     *
     * @param currentDirection current movement direction
     * @param lastPos          last grid position to avoid backtracking
     * @return best next direction ('U', 'D', 'L', 'R')
     */
    public char getNextDirection(char currentDirection, Point lastPos) {
        List<Double> inputs = new ArrayList<>();
        Point2D pos = new Point2D.Double(getX(), getY());

        // Nearest food and energizer inputs
        Food food = getMap().getNearestFood(pos, "default");
        Food energizer = getMap().getNearestFood(pos, "big");
        if (energizer == null) energizer = food;

        inputs.add((double) food.getX() / getMap().getWidth());
        inputs.add((double) food.getY() / getMap().getHeight());
        inputs.add((double) energizer.getX() / getMap().getWidth());
        inputs.add((double) energizer.getY() / getMap().getHeight());

        // Ghosts information
        double maxDist = Math.sqrt(Math.pow(getMap().getWidth(), 2) + Math.pow(getMap().getHeight(), 2));
        for (Ghost ghost : getMap().getGhosts()) {
            double dist = pos.distance(new Point2D.Double(ghost.getX(), ghost.getY()));
            inputs.add(dist / maxDist);
            inputs.add(ghost.isScared() ? 1.0 : 0.0);
        }

        // Neural output
        List<Double> outputs = brain.execute(inputs);
        char[] directions = {'L', 'R', 'U', 'D'};
        Point[] adjacentPoints = new Point[] {
            new Point(getX() - getMap().getTileSize(), getY()),
            new Point(getX() + getMap().getTileSize(), getY()),
            new Point(getX(), getY() - getMap().getTileSize()),
            new Point(getX(), getY() + getMap().getTileSize())
        };

        char bestDirection = currentDirection;
        double bestScore = Double.NEGATIVE_INFINITY;
        boolean foundValid = false;

        for (int i = 0; i < outputs.size(); i++) {
            char candidate = directions[i];
            Point test = adjacentPoints[i];

            if (!isOppositeDirection(currentDirection, candidate)
                    && !getMap().hasWall(test.x, test.y)
                    && (lastPos == null || !test.equals(lastPos))) {
                double score = outputs.get(i);
                if (score > bestScore) {
                    bestScore = score;
                    bestDirection = candidate;
                    foundValid = true;
                }
            }
        }

        // Fallback: choose any unblocked direction
        if (!foundValid) {
            for (int i = 0; i < outputs.size(); i++) {
                Point test = snapToGrid(adjacentPoints[i]);
                if (!getMap().hasWall(test.x, test.y)) {
                    bestDirection = directions[i];
                    break;
                }
            }
        }

        return bestDirection;
    }

    /**
     * Checks if two directions are opposite.
     */
    private boolean isOppositeDirection(char d1, char d2) {
        return (d1 == 'U' && d2 == 'D') || (d1 == 'D' && d2 == 'U') ||
               (d1 == 'L' && d2 == 'R') || (d1 == 'R' && d2 == 'L');
    }

    /**
     * Returns the neural network used by this player.
     */
    public NeuralNet getNeuralNet() {
        return brain;
    }

    /**
     * Returns true if the player ate food in the current step.
     */
    public boolean ateFoodThisStep() {
        return ateFoodThisStep;
    }

    /**
     * Returns true if this player has collided with any wall.
     */
    public boolean hasCollision() {
        for (Wall wall : super.getMap().getWalls()) {
            if (collision(this, wall)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Attempts to flee if a nearby ghost is too close and not scared.
     * Returns true if an evasive maneuver was executed.
     */
    private boolean evadeDanger() {
        int px = this.getX();
        int py = this.getY();
        int tileSize = getMap().getTileSize();
        double minDist = Double.MAX_VALUE;
        Ghost closestThreat = null;

        for (Ghost g : getMap().getGhosts()) {
            if (!g.isScared()) {
                double dx = g.getX() - px;
                double dy = g.getY() - py;
                double dist = Math.sqrt(dx * dx + dy * dy);
                if (dist < minDist) {
                    minDist = dist;
                    closestThreat = g;
                }
            }
        }

        if (closestThreat != null && minDist < tileSize * 2) {
            char[] directions = {'U', 'D', 'L', 'R'};
            double maxDistance = -1;
            char bestDir = this.getDirection();

            for (char dir : directions) {
                int newX = px, newY = py;
                switch (dir) {
                    case 'U': newY -= tileSize; break;
                    case 'D': newY += tileSize; break;
                    case 'L': newX -= tileSize; break;
                    case 'R': newX += tileSize; break;
                }

                int tileX = newX / tileSize;
                int tileY = newY / tileSize;
                if (getMap().hasWall(tileX, tileY)) continue;

                double dx = closestThreat.getX() - newX;
                double dy = closestThreat.getY() - newY;
                double dist = Math.sqrt(dx * dx + dy * dy);

                if (dist > maxDistance) {
                    maxDistance = dist;
                    bestDir = dir;
                }
            }

            this.updateDirection(bestDir);
            return true;
        }

        return false;
    }

    /**
     * Checks if a point is accessible (not blocked by a wall).
     */
    private boolean isAccessible(Point p) {
        int tileX = p.x / getMap().getTileSize();
        int tileY = p.y / getMap().getTileSize();
        return !getMap().hasWall(tileX, tileY);
    }

    /**
     * Snaps a point to the nearest tile grid location.
     */
    private Point snapToGrid(Point p) {
        int size = getMap().getTileSize();
        return new Point((p.x / size) * size, (p.y / size) * size);
    }

    /**
     * Checks whether the player is aligned with the tile grid.
     */
    private boolean isAlignedWithGrid() {
        int tileSize = getMap().getTileSize();
        return getX() % tileSize == 0 && getY() % tileSize == 0;
    }
}
