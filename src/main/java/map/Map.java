package map;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.HashSet;
import javax.swing.*;
import entities.Player;
import entities.ghosts.*;

/**
 * Represents the game map containing all game entities and responsible for rendering.
 * This class extends {@link JPanel} to provide the visual representation of the game.
 */
public class Map extends JPanel {

    /** Number of rows in the map. */
    private int rows = 21;

    /** Number of columns in the map. */
    private int cols = 19;

    /** Size of each tile in the map. */
    private int tileSize;

    /** Width of the map in pixels. */
    private int width;

    /** Height of the map in pixels. */
    private int height;

    /** Current level of the game. */
    private int level = 1;

    /** Images for various game entities. */
    private Image wallImage, blueGhostImage, orangeGhostImage, pinkGhostImage, redGhostImage;
    private Image pacmanUpImage, pacmanDownImage, pacmanLeftImage, pacmanRightImage;
    private Image cherryImage, strawberryImage, orangeImage, appleImage, mysteryImage;

    /** Set of walls in the map. */
    private HashSet<Wall> walls;

    /** Set of ghosts in the map. */
    private HashSet<Ghost> ghosts;

    /** Set of food items in the map. */
    private HashSet<Food> foods;

    /** The player (Pac-Man) in the map. */
    private Player pacman;

    /** Tile map representation of the level. */
    private String[] tileMap = {
        "XXXXXXXXXXXXXXXXXXX",
        "X        X        X",
        "X.XX XXX X XXX XX.X",
        "X                 X",
        "X XX X XXXXX X XX X",
        "X    X       X    X",
        "XXXX XXXX XXXX XXXX",
        "OOOX X       X XOOO",
        "XXXX X XXrXX X XXXX",
        "O       bpo       O",
        "XXXX X XXXXX X XXXX",
        "OOOX X       X XOOO",
        "XXXX X XXXXX X XXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X. X     P     X .X",
        "XX X X XXXXX X X XX",
        "X    X   X   X    X",
        "X XXXXXX X XXXXXX X",
        "X                 X",
        "XXXXXXXXXXXXXXXXXXX"
    };

    /**
     * Constructs a new {@code Map} instance.
     * Initializes the map size, loads images, and sets up the game map.
     *
     * @param tileSize the size of each tile in the map
     */
    public Map(int tileSize) {
        this.tileSize = tileSize;
        this.width = cols * tileSize;
        this.height = rows * tileSize;

        setPreferredSize(new Dimension(width, height));
        setBackground(Color.BLACK);

        // Load images for walls, ghosts, Pac-Man, and food
        wallImage = new ImageIcon(getClass().getResource("/wall.png")).getImage();
        cherryImage = new ImageIcon(getClass().getResource("/cherry.png")).getImage();
        strawberryImage = new ImageIcon(getClass().getResource("/strawberry.png")).getImage();
        orangeImage = new ImageIcon(getClass().getResource("/orange.png")).getImage();
        appleImage = new ImageIcon(getClass().getResource("/apple.png")).getImage();
        mysteryImage = new ImageIcon(getClass().getResource("/mystery.png")).getImage();

        blueGhostImage = new ImageIcon(getClass().getResource("/blueGhost.png")).getImage();
        orangeGhostImage = new ImageIcon(getClass().getResource("/orangeGhost.png")).getImage();
        pinkGhostImage = new ImageIcon(getClass().getResource("/pinkGhost.png")).getImage();
        redGhostImage = new ImageIcon(getClass().getResource("/redGhost.png")).getImage();

        pacmanDownImage = new ImageIcon(getClass().getResource("/pacmanDown.png")).getImage();
        pacmanUpImage = new ImageIcon(getClass().getResource("/pacmanUp.png")).getImage();
        pacmanLeftImage = new ImageIcon(getClass().getResource("/pacmanLeft.png")).getImage();
        pacmanRightImage = new ImageIcon(getClass().getResource("/pacmanRight.png")).getImage();

        loadMap();
    }

    /**
     * Constructs a new {@code Map} instance with a specified Pac-Man player.
     *
     * @param tileSize the size of each tile in the map
     * @param pacman   the Pac-Man player
     */
    public Map(int tileSize, Player pacman) {
        this(tileSize);
        this.pacman = pacman;
    }

    /**
     * Loads the game map based on the current level.
     * Creates all game entities (walls, foods, ghosts, Pac-Man) according to the tile map.
     */
    public void loadMap() {
        walls = new HashSet<>();
        foods = new HashSet<>();
        ghosts = new HashSet<>();

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                char tileMapChar = tileMap[r].charAt(c);
                int x = c * tileSize;
                int y = r * tileSize;

                // Handle special food placement based on level
                switch (this.level % 5) {
                    case 0:
                        if (r == 1 && c == 16) tileMapChar = 'c';
                        break;
                    case 1:
                        if (r == 3 && c == 2) tileMapChar = 's';
                        break;
                    case 2:
                        if (r == 19 && c == 16) tileMapChar = 'v';
                        break;
                    case 3:
                        if (r == 15 && c == 1) tileMapChar = 'a';
                        break;
                    case 4:
                        if (r == 9 && c == 7) tileMapChar = 'm';
                        break;
                }

                // Create entities based on tile map characters
                switch (tileMapChar) {
                    case 'X':
                        walls.add(new Wall(wallImage, x, y, tileSize, tileSize, this));
                        break;
                    case 'b':
                        ghosts.add(new BlueGhost(blueGhostImage, x, y, tileSize, tileSize, this));
                        break;
                    case 'o':
                        ghosts.add(new OrangeGhost(orangeGhostImage, x, y, tileSize, tileSize, this));
                        break;
                    case 'p':
                        ghosts.add(new PinkGhost(pinkGhostImage, x, y, tileSize, tileSize, this));
                        break;
                    case 'r':
                        ghosts.add(new RedGhost(redGhostImage, x, y, tileSize, tileSize, this));
                        break;
                    case 'c':
                        foods.add(new Food(cherryImage, x, y, tileSize, tileSize, "cherry", 100));
                        break;
                    case 's':
                        foods.add(new Food(strawberryImage, x, y, tileSize, tileSize, "strawberry", 300));
                        break;
                    case 'a':
                        foods.add(new Food(appleImage, x, y, tileSize, tileSize, "apple", 500));
                        break;
                    case 'v':
                        foods.add(new Food(orangeImage, x, y, tileSize, tileSize, "orange", 700));
                        break;
                    case 'm':
                        foods.add(new Food(mysteryImage, x, y, tileSize, tileSize, "mystery", 1000));
                        break;
                    case '.':
                        foods.add(new Food(null, x + 8, y + 8, 16, 16, "big", 50));
                        break;
                    case 'P':
                        if (pacman == null) {
                            pacman = new Player(pacmanRightImage, x, y, tileSize, tileSize, this);
                        } else {
                            pacman.reset();
                        }
                        break;
                    case ' ':
                        foods.add(new Food(null, x + 12, y + 12, 8, 8, "default", 10));
                        break;
                }
            }
        }
    }

    /**
     * Resets positions of all mobile entities (Pac-Man and ghosts) to their starting positions.
     */
    public void resetPositions() {
        pacman.reset();
        pacman.setVelocityX(0);
        pacman.setVelocityY(0);

        for (Ghost ghost : ghosts) {
            ghost.reset();
            ghost.setScared(false);
        }
    }

    /**
     * Overrides the {@code paintComponent} method to provide custom rendering.
     *
     * @param g the {@link Graphics} context to paint on
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    /**
     * Renders all game entities on the screen.
     *
     * @param g the {@link Graphics} context to paint on
     */
    public void draw(Graphics g) {
        for (Wall wall : walls) {
            g.drawImage(wall.getImage(), wall.getX(), wall.getY(), wall.getWidth(), wall.getHeight(), null);
        }

        g.setColor(Color.YELLOW);
        for (Food food : foods) {
            if (food.getFoodType().equals("default") || food.getFoodType().equals("big")) {
                g.fillOval(food.getX(), food.getY(), food.getWidth(), food.getHeight());
            } else {
                g.drawImage(food.getImage(), food.getX(), food.getY(), food.getWidth(), food.getHeight(), null);
            }
        }

        g.drawImage(pacman.getImage(), pacman.getX(), pacman.getY(), pacman.getWidth(), pacman.getHeight(), null);

        for (Ghost ghost : ghosts) {
            g.drawImage(ghost.getImage(), ghost.getX(), ghost.getY(), ghost.getWidth(), ghost.getHeight(), null);
        }

        int fontSize = Math.max(16, tileSize / 2);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, fontSize));
        if (pacman.isGameOver()) {
            g.drawString("Game Over: " + pacman.getScore(), tileSize / 2, tileSize / 2 + fontSize / 3);
            g.drawString("Level: " + level, tileSize * cols - tileSize * 3, tileSize / 2 + fontSize / 3);
        } else {
            g.drawString("Lives: " + pacman.getLives() + "   Score: " + pacman.getScore(), tileSize / 2, tileSize / 2 + fontSize / 3);
            g.drawString("Level: " + level, tileSize * cols - tileSize * 3, tileSize / 2 + fontSize / 3);
        }
    }

    /***
     * Gets the numbers of rows in the map
     * @return numbers of rows
     */
	public int getRows() {
		return rows;
	}

	/***
	 * Set the amount of rows
	 * @param rows Amount of rows
	 */
	public void setRows(int rows) {
		this.rows = rows;
	}

	/***
	 * Gets the amount of columns
	 * 
	 * @return cols The amount of columns
	 */
	public int getCols() {
		return cols;
	}

	/***
	 *  Set the amount of columns
	 * @param cols The amount of columns
	 */
	public void setCols(int cols) {
		this.cols = cols;
	}

    /**
     * Gets the size of each tile in the map.
     *
     * @return the tile size
     */
    public int getTileSize() {
        return tileSize;
    }

    /**
     * Sets the size of each tile in the map.
     *
     * @param tileSize the tile size to set
     */
    public void setTileSize(int tileSize) {
        this.tileSize = tileSize;
    }

    /**
     * Gets the width of the map in pixels.
     *
     * @return the width of the map
     */
    public int getWidth() {
        return width;
    }

    /**
     * Sets the width of the map in pixels.
     *
     * @param width the width to set
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Gets the height of the map in pixels.
     *
     * @return the height of the map
     */
    public int getHeight() {
        return height;
    }

    /**
     * Sets the height of the map in pixels.
     *
     * @param height the height to set
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Gets the image used for walls in the map.
     *
     * @return the wall image
     */
    public Image getWallImage() {
        return wallImage;
    }

    /**
     * Sets the image used for walls in the map.
     *
     * @param wallImage the wall image to set
     */
    public void setWallImage(Image wallImage) {
        this.wallImage = wallImage;
    }

    /**
     * Gets the image used for the blue ghost.
     *
     * @return the blue ghost image
     */
    public Image getBlueGhostImage() {
        return blueGhostImage;
    }

    /**
     * Sets the image used for the blue ghost.
     *
     * @param blueGhostImage the blue ghost image to set
     */
    public void setBlueGhostImage(Image blueGhostImage) {
        this.blueGhostImage = blueGhostImage;
    }

    /**
     * Gets the image used for the orange ghost.
     *
     * @return the orange ghost image
     */
    public Image getOrangeGhostImage() {
        return orangeGhostImage;
    }

    /**
     * Sets the image used for the orange ghost.
     *
     * @param orangeGhostImage the orange ghost image to set
     */
    public void setOrangeGhostImage(Image orangeGhostImage) {
        this.orangeGhostImage = orangeGhostImage;
    }

    /**
     * Gets the image used for the pink ghost.
     *
     * @return the pink ghost image
     */
    public Image getPinkGhostImage() {
        return pinkGhostImage;
    }

    /**
     * Sets the image used for the pink ghost.
     *
     * @param pinkGhostImage the pink ghost image to set
     */
    public void setPinkGhostImage(Image pinkGhostImage) {
        this.pinkGhostImage = pinkGhostImage;
    }

    /**
     * Gets the image used for the red ghost.
     *
     * @return the red ghost image
     */
    public Image getRedGhostImage() {
        return redGhostImage;
    }

    /**
     * Sets the image used for the red ghost.
     *
     * @param redGhostImage the red ghost image to set
     */
    public void setRedGhostImage(Image redGhostImage) {
        this.redGhostImage = redGhostImage;
    }

    /**
     * Gets the image used for Pac-Man facing up.
     *
     * @return the Pac-Man up image
     */
    public Image getPacmanUpImage() {
        return pacmanUpImage;
    }

    /**
     * Sets the image used for Pac-Man facing up.
     *
     * @param pacmanUpImage the Pac-Man up image to set
     */
    public void setPacmanUpImage(Image pacmanUpImage) {
        this.pacmanUpImage = pacmanUpImage;
    }

    /**
     * Gets the image used for Pac-Man facing down.
     *
     * @return the Pac-Man down image
     */
    public Image getPacmanDownImage() {
        return pacmanDownImage;
    }

    /**
     * Sets the image used for Pac-Man facing down.
     *
     * @param pacmanDownImage the Pac-Man down image to set
     */
    public void setPacmanDownImage(Image pacmanDownImage) {
        this.pacmanDownImage = pacmanDownImage;
    }

    /**
     * Gets the image used for Pac-Man facing left.
     *
     * @return the Pac-Man left image
     */
    public Image getPacmanLeftImage() {
        return pacmanLeftImage;
    }

    /**
     * Sets the image used for Pac-Man facing left.
     *
     * @param pacmanLeftImage the Pac-Man left image to set
     */
    public void setPacmanLeftImage(Image pacmanLeftImage) {
        this.pacmanLeftImage = pacmanLeftImage;
    }

    /**
     * Gets the image used for Pac-Man facing right.
     *
     * @return the Pac-Man right image
     */
    public Image getPacmanRightImage() {
        return pacmanRightImage;
    }

    /**
     * Sets the image used for Pac-Man facing right.
     *
     * @param pacmanRightImage the Pac-Man right image to set
     */
    public void setPacmanRightImage(Image pacmanRightImage) {
        this.pacmanRightImage = pacmanRightImage;
    }

    /**
     * Gets the image used for cherries.
     *
     * @return the cherry image
     */
    public Image getCherryImage() {
        return cherryImage;
    }

    /**
     * Sets the image used for cherries.
     *
     * @param cherryImage the cherry image to set
     */
    public void setCherryImage(Image cherryImage) {
        this.cherryImage = cherryImage;
    }

    /**
     * Gets the set of walls in the map.
     *
     * @return the set of walls
     */
    public HashSet<Wall> getWalls() {
        return walls;
    }

    /**
     * Sets the set of walls in the map.
     *
     * @param walls the set of walls to set
     */
    public void setWalls(HashSet<Wall> walls) {
        this.walls = walls;
    }

    /**
     * Gets the set of ghosts in the map.
     *
     * @return the set of ghosts
     */
    public HashSet<Ghost> getGhosts() {
        return ghosts;
    }

    /**
     * Sets the set of ghosts in the map.
     *
     * @param ghosts the set of ghosts to set
     */
    public void setGhosts(HashSet<Ghost> ghosts) {
        this.ghosts = ghosts;
    }

    /**
     * Gets the set of food items in the map.
     *
     * @return the set of food items
     */
    public HashSet<Food> getFoods() {
        return foods;
    }

    /**
     * Sets the set of food items in the map.
     *
     * @param foods the set of food items to set
     */
    public void setFoods(HashSet<Food> foods) {
        this.foods = foods;
    }

    /**
     * Gets the Pac-Man player in the map.
     *
     * @return the Pac-Man player
     */
    public Player getPacman() {
        return pacman;
    }

    /**
     * Sets the Pac-Man player in the map.
     *
     * @param pacman the Pac-Man player to set
     */
    public void setPacman(Player pacman) {
        this.pacman = pacman;
    }

    /**
     * Gets the tile map representation of the level.
     *
     * @return the tile map
     */
    public String[] getTileMap() {
        return tileMap;
    }

    /**
     * Sets the tile map representation of the level.
     *
     * @param tileMap the tile map to set
     */
    public void setTileMap(String[] tileMap) {
        this.tileMap = tileMap;
    }

    /**
     * Gets the current level of the game.
     *
     * @return the current level
     */
    public int getLevel() {
        return level;
    }

    /**
     * Sets the current level of the game.
     *
     * @param level the level to set
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * Gets the image used for strawberries.
     *
     * @return the strawberry image
     */
    public Image getStrawberryImage() {
        return strawberryImage;
    }

    /**
     * Sets the image used for strawberries.
     *
     * @param strawberryImage the strawberry image to set
     */
    public void setStrawberryImage(Image strawberryImage) {
        this.strawberryImage = strawberryImage;
    }

    /**
     * Gets the image used for oranges.
     *
     * @return the orange image
     */
    public Image getOrangeImage() {
        return orangeImage;
    }

    /**
     * Sets the image used for oranges.
     *
     * @param orangeImage the orange image to set
     */
    public void setOrangeImage(Image orangeImage) {
        this.orangeImage = orangeImage;
    }

    /**
     * Gets the image used for apples.
     *
     * @return the apple image
     */
    public Image getAppleImage() {
        return appleImage;
    }

    /**
     * Sets the image used for apples.
     *
     * @param appleImage the apple image to set
     */
    public void setAppleImage(Image appleImage) {
        this.appleImage = appleImage;
    }

    /**
     * Gets the image used for mystery items.
     *
     * @return the mystery image
     */
    public Image getMysteryImage() {
        return mysteryImage;
    }

    /**
     * Sets the image used for mystery items.
     *
     * @param mysteryImage the mystery image to set
     */
    public void setMysteryImage(Image mysteryImage) {
        this.mysteryImage = mysteryImage;
    }

    /**
     * Finds the nearest food of a specific type to a given position.
     *
     * @param pos  the position to search from
     * @param type the type of food to search for
     * @return the nearest food of the specified type, or {@code null} if none is found
     */
    public Food getNearestFood(Point2D pos, String type) {
        Food nearest = null;
        double minDist = Double.MAX_VALUE;

        for (Food food : foods) {
            if (!food.getFoodType().equals(type)) continue;

            Point2D foodPos = new Point2D.Double(food.getX(), food.getY());
            double dist = pos.distance(foodPos);

            if (dist < minDist) {
                minDist = dist;
                nearest = food;
            }
        }

        return nearest;
    }

    /**
     * Checks if there is a wall at the specified position.
     *
     * @param x the X coordinate
     * @param y the Y coordinate
     * @return {@code true} if there is a wall at the position, {@code false} otherwise
     */
    public boolean hasWall(int x, int y) {
        Rectangle test = new Rectangle(x, y, tileSize, tileSize);
        for (Wall wall : walls) {
            if (wall.getBounds().intersects(test)) {
                return true;
            }
        }
        return false;
    }




}
