package entities;

import neural.*;
import map.*;
import entities.ghosts.*;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
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
    private final LinkedList<Point> lastPositions = new LinkedList<>();
    private static final int LOOP_MEMORY = 6; // nombre de mouvements à mémoriser
    
    // Nouveaux attributs pour l'exploration
    private final HashSet<Point> visitedAreas = new HashSet<>();
    private static final int EXPLORATION_RADIUS = 3; // Rayon pour marquer une zone comme visitée
    private int stepsWithoutExploration = 0;
    private static final int MAX_STEPS_WITHOUT_EXPLORATION = 200;

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

        // Marquer la zone actuelle comme visitée
        Point currentGridPos = new Point(getX() / getMap().getTileSize(), getY() / getMap().getTileSize());
        visitedAreas.add(currentGridPos);

        // Nearest food and energizer inputs (4 inputs)
        Food food = getMap().getNearestFood(pos, "default");
        Food energizer = getMap().getNearestFood(pos, "big");
        if (energizer == null) energizer = food;

        inputs.add((double) food.getX() / getMap().getWidth());
        inputs.add((double) food.getY() / getMap().getHeight());
        inputs.add((double) energizer.getX() / getMap().getWidth());
        inputs.add((double) energizer.getY() / getMap().getHeight());

        // Inputs pour exactement 4 fantômes (12 inputs: 3 par fantôme)
        double maxDist = Math.sqrt(Math.pow(getMap().getWidth(), 2) + Math.pow(getMap().getHeight(), 2));
        List<Ghost> ghostList = new ArrayList<>(getMap().getGhosts());
        for (int i = 0; i < 4; i++) {
            if (i < ghostList.size()) {
                Ghost ghost = ghostList.get(i);
                double distance = Math.sqrt(Math.pow(ghost.getX() - getX(), 2) + Math.pow(ghost.getY() - getY(), 2));
                double normalizedDistance = Math.min(distance / maxDist, 1.0);
                
                // Position relative normalisée
                double dx = Math.max(-1.0, Math.min(1.0, (ghost.getX() - getX()) / (maxDist / 2)));
                double dy = Math.max(-1.0, Math.min(1.0, (ghost.getY() - getY()) / (maxDist / 2)));
                
                inputs.add(dx);
                inputs.add(dy);
                inputs.add(ghost.isScared() ? 1.0 : 0.0);
            } else {
                // Fantôme fictif si moins de 4 fantômes
                inputs.add(0.0);
                inputs.add(0.0);
                inputs.add(0.0);
            }
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

        // Mémoriser les positions pour éviter les boucles
        Point currentPos = new Point(getX(), getY());
        lastPositions.add(currentPos);
        if (lastPositions.size() > LOOP_MEMORY) {
            lastPositions.removeFirst();
        }

        char bestDirection = currentDirection;
        double bestScore = Double.NEGATIVE_INFINITY;
        boolean foundValid = false;
        boolean exploredThisStep = false;

        for (int i = 0; i < outputs.size(); i++) {
            char candidate = directions[i];
            Point test = adjacentPoints[i];

            if ( /*!isOppositeDirection(currentDirection, candidate)*/
                    /*&& */!getMap().hasWall(test.x, test.y)) {
                
                double score = outputs.get(i);
                
                // Pénalité anti-boucle
                long loopCount = lastPositions.stream().filter(p -> p.equals(test)).count();
                if (loopCount > 0) {
                    score -= loopCount * 2.0;
                }
                
                // Éviter de revenir à la position précédente
                if (lastPos != null && test.equals(lastPos)) {
                    score -= 1.5;
                }
                
                // BONUS D'EXPLORATION - Récompenser les zones non visitées
                Point testGridPos = new Point(test.x / getMap().getTileSize(), test.y / getMap().getTileSize());
                boolean isNewArea = true;
                for (Point visited : visitedAreas) {
                    double dist = Math.sqrt(Math.pow(visited.x - testGridPos.x, 2) + Math.pow(visited.y - testGridPos.y, 2));
                    if (dist <= EXPLORATION_RADIUS) {
                        isNewArea = false;
                        break;
                    }
                }
                
                if (isNewArea) {
                    score += 3.0; // Gros bonus pour exploration
                    exploredThisStep = true;
                } else {
                    // Pénalité pour rester dans des zones déjà explorées
                    score -= 1.0;
                }
                
                // Bonus supplémentaire si on n'a pas exploré depuis longtemps
                if (stepsWithoutExploration > MAX_STEPS_WITHOUT_EXPLORATION / 2) {
                    if (isNewArea) {
                        score += 5.0; // Bonus encore plus important
                    }
                }
                
                // PÉNALITÉ TRÈS SÉVÈRE pour fantômes proches non effrayés
                for (Ghost ghost : getMap().getGhosts()) {
                    if (!ghost.isScared()) {
                        double ghostDist = Math.sqrt(Math.pow(ghost.getX() - test.x, 2) + Math.pow(ghost.getY() - test.y, 2));
                        double tileSize = getMap().getTileSize();
                        
                        if (ghostDist < tileSize * 1.5) {
                            score -= 10.0; // Pénalité énorme pour collision imminente
                        } else if (ghostDist < tileSize * 3) {
                            score -= 5.0; // Forte pénalité pour proximité dangereuse
                        }
                    }
                }
                
                // Bonus pour se diriger vers la nourriture la plus proche
                if (food != null) {
                    double currentDistToFood = Math.sqrt(Math.pow(food.getX() - getX(), 2) + Math.pow(food.getY() - getY(), 2));
                    double newDistToFood = Math.sqrt(Math.pow(food.getX() - test.x, 2) + Math.pow(food.getY() - test.y, 2));
                    if (newDistToFood < currentDistToFood) {
                        score += 1.0; // Bonus pour se rapprocher de la nourriture
                    }
                }
                
                if (score > bestScore) {
                    bestScore = score;
                    bestDirection = candidate;
                    foundValid = true;
                }
            }
        }

        // Mettre à jour le compteur d'exploration
        if (exploredThisStep) {
            stepsWithoutExploration = 0;
        } else {
            stepsWithoutExploration++;
        }

        // Fallback plus robuste
        if (!foundValid) {
            for (int i = 0; i < directions.length; i++) {
                Point test = adjacentPoints[i];
                if (!getMap().hasWall(test.x, test.y) /*&& */
                    /*!isOppositeDirection(currentDirection, directions[i])*/) {
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
     * Returns the set of visited areas for exploration tracking.
     */
    public HashSet<Point> getVisitedAreas() {
        return visitedAreas;
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