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
public class Map extends JPanel {

    private int rows = 21;
    private int cols = 19;
    private int tileSize;
    private int width;
    private int height;

    private int level = 1;  

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

    private HashSet<Wall> walls;
    private HashSet<Ghost> ghosts;
    private HashSet<Food> foods;
    private Player pacman;

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
     * Constructs a new Map instance.
     * Initializes the map size, loads images, and sets up the game map.
     */
    public Map(int tileSize){
        this.tileSize = tileSize;
        this.width = cols * tileSize;
        this.height = rows * tileSize;

        setPreferredSize(new Dimension(width, height));
        setBackground(Color.BLACK);

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
     * Loads the game map based on the current level.
     * Creates all game entities (walls, foods, ghosts, pacman) according to the tile map.
     */
    public void loadMap(){
        walls = new HashSet<Wall>();
        foods = new HashSet<Food>();
        ghosts = new HashSet<Ghost>();

        for(int r=0; r < rows; r++){
            for(int c=0; c < cols; c++){
                String row = tileMap[r];
                char tileMapChar = row.charAt(c);

                int x = c*tileSize;
                int y = r*tileSize;
                
                switch(this.level%5) {
                case(0):
                    if(r==1 && c==16) {
                        tileMapChar = 'c';
                    }
                    break;
                case(1):
                    if(r==3 && c==2) {
                        tileMapChar = 's';
                    }
                    break;
                case(2):
                    if(r==19 && c==16) {
                        tileMapChar = 'v';
                    }
                    break;
                case(3):
                    if(r==15 && c==1) {
                        tileMapChar = 'a';
                    }
                    break;
                case(4):
                    if(r==9 && c==7) {
                        tileMapChar = 'm';
                    }
                    break;
                default:
                    break;
                }

                if(tileMapChar == 'X'){
                    Wall wall = new Wall(wallImage, x, y, tileSize, tileSize, this);
                    walls.add(wall);
                }
                else if(tileMapChar == 'b'){
                    BlueGhost ghost = new BlueGhost(blueGhostImage, x, y, tileSize, tileSize, this);
                    ghosts.add(ghost);
                }
                else if(tileMapChar == 'o'){
                    OrangeGhost ghost = new OrangeGhost(orangeGhostImage, x, y, tileSize, tileSize, this);
                    ghosts.add(ghost);
                }
                else if(tileMapChar == 'p'){
                    PinkGhost ghost = new PinkGhost(pinkGhostImage, x, y, tileSize, tileSize, this);
                    ghosts.add(ghost);
                }
                else if(tileMapChar == 'r'){
                    RedGhost ghost = new RedGhost(redGhostImage, x, y, tileSize, tileSize, this);
                    ghosts.add(ghost);
                }
                else if (tileMapChar == 'c'){
                    Food food = new Food(cherryImage,x,y,tileSize,tileSize,"cherry",100);
                    foods.add(food);
                }
                else if (tileMapChar == 's'){
                    Food food = new Food(strawberryImage,x,y,tileSize,tileSize,"strawberry",300);
                    foods.add(food);
                }
                else if (tileMapChar == 'a'){
                    Food food = new Food(appleImage,x,y,tileSize,tileSize,"apple",500);
                    foods.add(food);
                }
                else if (tileMapChar == 'v'){ 
                    Food food = new Food(orangeImage,x,y,tileSize,tileSize,"orange",700);
                    foods.add(food);
                }
                else if (tileMapChar == 'm'){
                    Food food = new Food(mysteryImage,x,y,tileSize,tileSize,"mystery",1000);
                    foods.add(food);
                }
                else if(tileMapChar == '.'){
                    Food food = new Food(null, x+8, y+8, 16, 16, "big", 50);
                    foods.add(food);
                }
                else if(tileMapChar == 'P'){
                    if (pacman == null){
                        pacman = new Player(pacmanRightImage, x, y, tileSize, tileSize, this);
                    }
                    else{
                        pacman.reset();
                    }
                    
                }
                else if(tileMapChar == ' '){
                    Food food = new Food(null, x+12, y+12, 8, 8, "default", 10);
                    foods.add(food);
                }
            }
        }
    }

    /**
     * Resets positions of all mobile entities (pacman and ghosts) to their starting positions.
     */
    public void resetPositions(){
        pacman.reset();
        pacman.setVelocityX(0);
        pacman.setVelocityY(0);

        for(Ghost ghost : ghosts){
            ghost.reset();
            ghost.setScared(false);
        }
    }

    /**
     * Overrides the paintComponent method to provide custom rendering.
     *
     * @param g The Graphics context to paint on
     */
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    /**
     * Renders all game entities on the screen.
     *
     * @param g The Graphics context to paint on
     */

    public void draw(Graphics g){
         for (Wall wall : walls){
            g.drawImage(wall.getImage(), wall.getX(), wall.getY(), wall.getWidth(), wall.getHeight(), null);
        }

        g.setColor(Color.YELLOW);
        for (Food food : foods){
            if(food.getFoodType()=="default" || food.getFoodType()=="big"){
                g.fillOval(food.getX(), food.getY(), food.getWidth(), food.getHeight());
            }
            else if(food.getFoodType()=="cherry"){
                g.drawImage(food.getImage(),food.getX(),food.getY(),food.getWidth(),food.getHeight(),null);
            }
            else if(food.getFoodType()=="apple"){
                g.drawImage(food.getImage(),food.getX(),food.getY(),food.getWidth(),food.getHeight(),null);
            }
            else if(food.getFoodType()=="strawberry"){
                g.drawImage(food.getImage(),food.getX(),food.getY(),food.getWidth(),food.getHeight(),null);
            }
            else if(food.getFoodType()=="orange"){
                g.drawImage(food.getImage(),food.getX(),food.getY(),food.getWidth(),food.getHeight(),null);
            }
            else if(food.getFoodType()=="mystery"){
                g.drawImage(food.getImage(),food.getX(),food.getY(),food.getWidth(),food.getHeight(),null);
            }
            
        }

        g.drawImage(pacman.getImage(), pacman.getX(), pacman.getY(), pacman.getWidth(), pacman.getHeight(), null);

        for (Ghost ghost : ghosts){
            g.drawImage(ghost.getImage(), ghost.getX(), ghost.getY(), ghost.getWidth(), ghost.getHeight(), null );
        }

        int fontSize = Math.max(16, tileSize / 2);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, fontSize));
        if(pacman.isGameOver()) {
            g.drawString("Game Over: " + String.valueOf(pacman.getScore()), tileSize/2, tileSize/2 + fontSize/3);
            g.drawString("Niveau: " + level, tileSize*cols - tileSize*3, tileSize/2 + fontSize/3);
        }
        else {
            g.drawString("Lives: " + String.valueOf(pacman.getLives()) + "   Score: " + String.valueOf(pacman.getScore()), tileSize/2, tileSize/2 + fontSize/3);
            g.drawString("Niveau: " + level, tileSize*cols - tileSize*3, tileSize/2 + fontSize/3);
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
     * @return the wall image
     */
	public Image getWallImage() {
		return wallImage;
	}

    /**
     * @param wallImage the image for a wall
     */
	public void setWallImage(Image wallImage) {
		this.wallImage = wallImage;
	}

    /**
     * 
     * @return the blueGhost image
     */
	public Image getBlueGhostImage() {
		return blueGhostImage;
	}

    /**
     * 
     * @param blueGhostImage the image for a blue ghost
     */
	public void setBlueGhostImage(Image blueGhostImage) {
		this.blueGhostImage = blueGhostImage;
	}

    /**
     * 
     * @return the orangeGhost image
     */
	public Image getOrangeGhostImage() {
		return orangeGhostImage;
	}

    /**
     * 
     * @param orangeGhostImage the image for an orange ghost
     */
	public void setOrangeGhostImage(Image orangeGhostImage) {
		this.orangeGhostImage = orangeGhostImage;
	}

    /**
     * 
     * @return the pinkGhost image
     */
	public Image getPinkGhostImage() {
		return pinkGhostImage;
	}

    /**
     * 
     * @param pinkGhostImage the image for a pink ghost
     */
	public void setPinkGhostImage(Image pinkGhostImage) {
		this.pinkGhostImage = pinkGhostImage;
	}

    /**
     * 
     * @return the redGhost image
     */
	public Image getRedGhostImage() {
		return redGhostImage;
	}

    /**
     * 
     * @param redGhostImage the image for a red ghost
     */
	public void setRedGhostImage(Image redGhostImage) {
		this.redGhostImage = redGhostImage;
	}

    /**
     * 
     * @return the pacmanUp image
     */
	public Image getPacmanUpImage() {
		return pacmanUpImage;
	}

    /**
     * 
     * @param pacmanUpImage the image for pacman going up
     */
	public void setPacmanUpImage(Image pacmanUpImage) {
		this.pacmanUpImage = pacmanUpImage;
	}

    /**
     * 
     * @return the pacmanDown image
     */
	public Image getPacmanDownImage() {
		return pacmanDownImage;
	}

    /**
     * 
     * @param pacmanDownImage the image for pacman going down
     */
	public void setPacmanDownImage(Image pacmanDownImage) {
		this.pacmanDownImage = pacmanDownImage;
	}

    /**
     * 
     * @return the pacmanLeft image
     */
	public Image getPacmanLeftImage() {
		return pacmanLeftImage;
	}

    /**
     * 
     * @param pacmanLeftImage the image for pacman going left
     */
	public void setPacmanLeftImage(Image pacmanLeftImage) {
		this.pacmanLeftImage = pacmanLeftImage;
	}

    /**
     * 
     * @return the pacmanRight image
     */
	public Image getPacmanRightImage() {
		return pacmanRightImage;
	}

    /**
     * 
     * @param pacmanRightImage the image for pacman going left
     */
	public void setPacmanRightImage(Image pacmanRightImage) {
		this.pacmanRightImage = pacmanRightImage;
	}

    /**
     * 
     * @return the Cherry image
     */
	public Image getCherryImage() {
		return cherryImage;
	}

    /**
     * 
     * @param cherryImage the image in order to draw a cherry on grid
     */
	public void setCherryImage(Image cherryImage) {
		this.cherryImage = cherryImage;
	}

    /**
     * @return the collection of walls in the map
     */
	public HashSet<Wall> getWalls() {
		return walls;
	}

    /**
     * 
     * @param walls set the walls of the grid
     */
	public void setWalls(HashSet<Wall> walls) {
		this.walls = walls;
	}

    /**
     * @return the collection of ghosts in the map
     */
	public HashSet<Ghost> getGhosts() {
		return ghosts;
	}

    /**
     * 
     * @param ghosts set the ghosts present on the grid
     */
	public void setGhosts(HashSet<Ghost> ghosts) {
		this.ghosts = ghosts;
	}

    /**
     * 
     * @return the collection of foods in the map
     */
	public HashSet<Food> getFoods() {
		return foods;
	}

    /**
     * 
     * @param foods set the foods present on the grid
     */
	public void setFoods(HashSet<Food> foods) {
		this.foods = foods;
	}

    /**
     * 
     * @return the pacman
     */
	public Player getPacman() {
		return pacman;
	}

    /**
     * 
     * @param pacman set pacman
     */
	public void setPacman(Player pacman) {
		this.pacman = pacman;
	}

    /**
     * 
     * @return the game grid
     */
	public String[] getTileMap() {
		return tileMap;
	}

    /**
     * 
     * @param tileMap set the game grid
     */
	public void setTileMap(String[] tileMap) {
		this.tileMap = tileMap;
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

    /**
     * 
     * @return the Strawberry image
     */
	public Image getStrawberryImage() {
		return strawberryImage;
	}

    /**
     * 
     * @param strawberryImage set the image for strawberry
     */
	public void setStrawberryImage(Image strawberryImage) {
		this.strawberryImage = strawberryImage;
	}

    /**
     * 
     * @return the Orange image
     */
	public Image getOrangeImage() {
		return orangeImage;
	}

    /**
     * 
     * @param orangeImage set the image for an orange
     */
	public void setOrangeImage(Image orangeImage) {
		this.orangeImage = orangeImage;
	}

    /**
     * 
     * @return the Apple image
     */
	public Image getAppleImage() {
		return appleImage;
	}

    /**
     * 
     * @param appleImage set the image for an apple
     */
	public void setAppleImage(Image appleImage) {
		this.appleImage = appleImage;
	}

    /**
     * 
     * @return the Mystery image
     */
	public Image getMysteryImage() {
		return mysteryImage;
	}

    /**
     * 
     * @param mysteryImage set the image for the mystery bonus
     */
	public void setMysteryImage(Image mysteryImage) {
		this.mysteryImage = mysteryImage;
	}

}
