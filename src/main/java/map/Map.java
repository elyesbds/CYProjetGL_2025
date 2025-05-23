package map;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.util.HashSet;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import entities.Player;
import entities.ghosts.BlueGhost;
import entities.ghosts.Ghost;
import entities.ghosts.OrangeGhost;
import entities.ghosts.PinkGhost;
import entities.ghosts.RedGhost;

/**
 * The Map class represents the game board and manages all game entities.
 * It extends JPanel to handle rendering and contains the game's logical layout.
 * 
 * <p>Responsibilities include:
 * <ul>
 *   <li>Managing the game grid and tile system</li>
 *   <li>Loading and rendering all game entities (walls, food, ghosts, player)</li>
 *   <li>Handling level progression and resets</li>
 *   <li>Providing access to game state information</li>
 * </ul>
 */
@SuppressWarnings("serial")
public class Map extends JPanel {

    private int rows = 21;
    private int cols = 19;
    private int tileSize;
    private int width;
    private int height;
    private int level = 1;

    // Entity images
    private Image wallImage;
    private Image blueGhostImage;
    private Image orangeGhostImage;
    private Image pinkGhostImage;
    private Image redGhostImage;
    private Image pacmanUpImage;
    private Image pacmanDownImage;
    private Image pacmanLeftImage;
    private Image pacmanRightImage;
    private Image cherryImage;
    private Image strawberryImage;
    private Image orangeImage;
    private Image appleImage;
    private Image mysteryImage;

    // Entity collections
    private HashSet<Wall> walls;
    private HashSet<Ghost> ghosts;
    private HashSet<Food> foods;
    private Player pacman;

    // Tile map representation
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
     * Constructs a new game map with specified tile size.
     * 
     * @param tileSize the size of each tile in pixels
     */
    public Map(int tileSize) {
        this.tileSize = tileSize;
        this.width = cols * tileSize;
        this.height = rows * tileSize;

        setPreferredSize(new Dimension(width, height));
        setBackground(Color.BLACK);

        // Load all game images
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
     * Loads the current level map, creating all game entities based on the tile map.
     * Handles special food placement based on current level.
     */
    public void loadMap() {
        walls = new HashSet<Wall>();
        foods = new HashSet<Food>();
        ghosts = new HashSet<Ghost>();

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                String row = tileMap[r];
                char tileMapChar = row.charAt(c);

                int x = c * tileSize;
                int y = r * tileSize;
                
                // Special food placement based on level
                switch(this.level % 5) {
                    case 0: if(r == 1 && c == 16) tileMapChar = 'c'; break;
                    case 1: if(r == 3 && c == 2) tileMapChar = 's'; break;
                    case 2: if(r == 19 && c == 16) tileMapChar = 'v'; break;
                    case 3: if(r == 15 && c == 1) tileMapChar = 'a'; break;
                    case 4: if(r == 9 && c == 7) tileMapChar = 'm'; break;
                }

                // Create entities based on tile characters
                if (tileMapChar == 'X') {
                    walls.add(new Wall(wallImage, x, y, tileSize, tileSize, this));
                }
                else if (tileMapChar == 'b') {
                    ghosts.add(new BlueGhost(blueGhostImage, x, y, tileSize, tileSize, this));
                }
                else if (tileMapChar == 'o') {
                    ghosts.add(new OrangeGhost(orangeGhostImage, x, y, tileSize, tileSize, this));
                }
                else if (tileMapChar == 'p') {
                    ghosts.add(new PinkGhost(pinkGhostImage, x, y, tileSize, tileSize, this));
                }
                else if (tileMapChar == 'r') {
                    ghosts.add(new RedGhost(redGhostImage, x, y, tileSize, tileSize, this));
                }
                else if (tileMapChar == 'c') {
                    foods.add(new Food(cherryImage, x, y, tileSize, tileSize, "cherry", 100));
                }
                else if (tileMapChar == 's') {
                    foods.add(new Food(strawberryImage, x, y, tileSize, tileSize, "strawberry", 300));
                }
                else if (tileMapChar == 'a') {
                    foods.add(new Food(appleImage, x, y, tileSize, tileSize, "apple", 500));
                }
                else if (tileMapChar == 'v') {
                    foods.add(new Food(orangeImage, x, y, tileSize, tileSize, "orange", 700));
                }
                else if (tileMapChar == 'm') {
                    foods.add(new Food(mysteryImage, x, y, tileSize, tileSize, "mystery", 1000));
                }
                else if (tileMapChar == '.') {
                    foods.add(new Food(null, x+8, y+8, 16, 16, "big", 50));
                }
                else if (tileMapChar == 'P') {
                    if (pacman == null) {
                        pacman = new Player(pacmanRightImage, x, y, tileSize, tileSize, this);
                    } else {
                        pacman.reset();
                    }
                }
                else if (tileMapChar == ' ') {
                    foods.add(new Food(null, x+12, y+12, 8, 8, "default", 10));
                }
            }
        }
    }

    /**
     * Resets all mobile entities to their starting positions.
     * Also resets ghost scared states.
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
     * Paints the game components on the panel.
     * 
     * @param g the Graphics context for painting
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    /**
     * Draws all game entities and UI elements.
     * 
     * @param g the Graphics context for drawing
     */
    public void draw(Graphics g) {
        // Draw walls
        for (Wall wall : walls) {
            g.drawImage(wall.getImage(), wall.getX(), wall.getY(), 
                       wall.getWidth(), wall.getHeight(), null);
        }

        // Draw foods
        g.setColor(Color.YELLOW);
        for (Food food : foods) {
            if (food.getFoodType().equals("default") || food.getFoodType().equals("big")) {
                g.fillOval(food.getX(), food.getY(), food.getWidth(), food.getHeight());
            } else {
                g.drawImage(food.getImage(), food.getX(), food.getY(), 
                           food.getWidth(), food.getHeight(), null);
            }
        }

        // Draw player
        g.drawImage(pacman.getImage(), pacman.getX(), pacman.getY(), 
                   pacman.getWidth(), pacman.getHeight(), null);

        // Draw ghosts
        for (Ghost ghost : ghosts) {
            g.drawImage(ghost.getImage(), ghost.getX(), ghost.getY(), 
                       ghost.getWidth(), ghost.getHeight(), null);
        }

        // Draw UI text
        int fontSize = Math.max(16, tileSize / 2);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, fontSize));
        
        if (pacman.isGameOver()) {
            g.drawString("Game Over: " + pacman.getScore(), 
                         tileSize/2, tileSize/2 + fontSize/3);
        } else {
            g.drawString("Lives: " + pacman.getLives() + "   Score: " + pacman.getScore(), 
                         tileSize/2, tileSize/2 + fontSize/3);
        }
        g.drawString("Niveau: " + level, 
                    tileSize*cols - tileSize*3, tileSize/2 + fontSize/3);
    }

    // Standard getters and setters with documentation

    /**
     * @return the number of rows in the map grid
     */
    public int getRows() {
        return rows;
    }

    /**
     * @param rows the number of rows to set
     */
    public void setRows(int rows) {
        this.rows = rows;
    }

    /**
     * @return the number of columns in the map grid
     */
    public int getCols() {
        return cols;
    }

    /**
     * @param cols the number of columns to set
     */
    public void setCols(int cols) {
        this.cols = cols;
    }

    /**
     * @return the size of each tile in pixels
     */
    public int getTileSize() {
        return tileSize;
    }

    /**
     * @param tileSize the tile size to set in pixels
     */
    public void setTileSize(int tileSize) {
        this.tileSize = tileSize;
    }

    /**
     * @return the total width of the map in pixels
     */
    public int getWidth() {
        return width;
    }

    /**
     * @param width the map width to set in pixels
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * @return the total height of the map in pixels
     */
    public int getHeight() {
        return height;
    }

    /**
     * @param height the map height to set in pixels
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * @return the current game level
     */
    public int getLevel() {
        return level;
    }

    /**
     * @param level the game level to set
     */
    public void setLevel(int level) {
        this.level = level;
    }

    // Additional getters and setters for images and entity collections...

    /**
     * @return the collection of walls in the map
     */
    public HashSet<Wall> getWalls() {
        return walls;
    }

    /**
     * @return the collection of ghosts in the map
     */
    public HashSet<Ghost> getGhosts() {
        return ghosts;
    }

    /**
     * @return the collection of food items in the map
     */
    public HashSet<Food> getFoods() {
        return foods;
    }

    /**
     * @return the player entity
     */
    public Player getPacman() {
        return pacman;
    }

	public void setPacman(Player pacman) {
		this.pacman = pacman;
	}

	public String[] getTileMap() {
		return tileMap;
	}

	public void setTileMap(String[] tileMap) {
		this.tileMap = tileMap;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public Image getStrawberryImage() {
		return strawberryImage;
	}

	public void setStrawberryImage(Image strawberryImage) {
		this.strawberryImage = strawberryImage;
	}

	public Image getOrangeImage() {
		return orangeImage;
	}

	public void setOrangeImage(Image orangeImage) {
		this.orangeImage = orangeImage;
	}

	public Image getAppleImage() {
		return appleImage;
	}

	public void setAppleImage(Image appleImage) {
		this.appleImage = appleImage;
	}

	public Image getMysteryImage() {
		return mysteryImage;
	}

	public void setMysteryImage(Image mysteryImage) {
		this.mysteryImage = mysteryImage;
	}

}
