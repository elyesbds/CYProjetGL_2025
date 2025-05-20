import java.awt.*;
import java.util.HashSet;
import javax.swing.*;

@SuppressWarnings("serial")
public class Map extends JPanel {

    private int rows = 21;
    private int cols = 19;
    private int tileSize = 64;
    private int width = cols * tileSize;
    private int height = rows * tileSize;

    private Image wallImage;
    private Image blueGhostImage;
    private Image orangeGhostImage;
    private Image pinkGhostImage;
    private Image redGhostImage;
    private Image scaredGhostImage;

    private Image pacmanUpImage;
    private Image pacmanDownImage;
    private Image pacmanLeftImage;
    private Image pacmanRightImage;

    private Image cherryImage;

    private HashSet<Wall> walls;
    private HashSet<Ghost> ghosts;
    private HashSet<Food> foods;
    private Player pacman;

    private String[] tileMap = {
            "XXXXXXXXXXXXXXXXXXX",
            "X        X        X",
            "X XX XXX X XXX XX X",
            "X  c              X",
            "X XX X XXXXX X XX X",
            "X    X       X    X",
            "XXXX XXXX XXXX XXXX",
            "OOOX X       X XOOO",
            "XXXX X XXrXX X XXXX",
            "O       bpo       O",
            "XXXX X XXXXX X XXXX",
            "OOOX X       X XOOO",
            "XXXX X XXXXX X XXXX",
            "X        X    .   X",
            "X XX XXX X XXX XX X",
            "X  X     P     X  X",
            "XX X X XXXXX X X XX",
            "X    X   X   X    X",
            "X XXXXXX X XXXXXX X",
            "X                 X",
            "XXXXXXXXXXXXXXXXXXX"
    };

    Map(){
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.BLACK);

        wallImage = new ImageIcon(getClass().getResource("/wall.png")).getImage();
        cherryImage = new ImageIcon(getClass().getResource("/cherry.png")).getImage();

        blueGhostImage = new ImageIcon(getClass().getResource("/blueGhost.png")).getImage();
        orangeGhostImage = new ImageIcon(getClass().getResource("/orangeGhost.png")).getImage();
        pinkGhostImage = new ImageIcon(getClass().getResource("/pinkGhost.png")).getImage();
        redGhostImage = new ImageIcon(getClass().getResource("/redGhost.png")).getImage();
        scaredGhostImage = new ImageIcon(getClass().getResource("/scaredGhost.png")).getImage();

        pacmanDownImage = new ImageIcon(getClass().getResource("/pacmanDown.png")).getImage();
        pacmanUpImage = new ImageIcon(getClass().getResource("/pacmanUp.png")).getImage();
        pacmanLeftImage = new ImageIcon(getClass().getResource("/pacmanLeft.png")).getImage();
        pacmanRightImage = new ImageIcon(getClass().getResource("/pacmanRight.png")).getImage();

        loadMap();
    }

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
                    Food food = new Food(cherryImage,x,y,tileSize,tileSize,"cherry");
                    foods.add(food);
                }
                else if(tileMapChar == '.'){
                    Food food = new Food(null,x+20,y+20,24,24,"big");
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
                    Food food = new Food(null, x + 26, y + 26, 12, 12, "default");
                    foods.add(food);
                }
            }
        }
    }

    public void resetPositions(){
        pacman.reset();
        pacman.setVelocityX(0);
        pacman.setVelocityY(0);

        for(Ghost ghost : ghosts){
            ghost.reset();
        }
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        g.drawImage(pacman.getImage(), pacman.getX(), pacman.getY(), pacman.getWidth(), pacman.getHeight(), null);

        for (Ghost ghost : ghosts){
            g.drawImage(ghost.getImage(), ghost.getX(), ghost.getY(), ghost.getWidth(), ghost.getHeight(), null );
        }

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
            
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 28));
        if(pacman.isGameOver()){
            g.drawString("Game Over: " + String.valueOf(pacman.getScore()), tileSize/2, tileSize/2+8);
        }
        else{
            g.drawString("Lives: " + String.valueOf(pacman.getLives()) + "   Score: " + String.valueOf(pacman.getScore()), tileSize/2, tileSize/2+8);
        }
    }

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getCols() {
		return cols;
	}

	public void setCols(int cols) {
		this.cols = cols;
	}

	public int getTileSize() {
		return tileSize;
	}

	public void setTileSize(int tileSize) {
		this.tileSize = tileSize;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public Image getWallImage() {
		return wallImage;
	}

	public void setWallImage(Image wallImage) {
		this.wallImage = wallImage;
	}

	public Image getBlueGhostImage() {
		return blueGhostImage;
	}

	public void setBlueGhostImage(Image blueGhostImage) {
		this.blueGhostImage = blueGhostImage;
	}

	public Image getOrangeGhostImage() {
		return orangeGhostImage;
	}

	public void setOrangeGhostImage(Image orangeGhostImage) {
		this.orangeGhostImage = orangeGhostImage;
	}

	public Image getPinkGhostImage() {
		return pinkGhostImage;
	}

	public void setPinkGhostImage(Image pinkGhostImage) {
		this.pinkGhostImage = pinkGhostImage;
	}

	public Image getRedGhostImage() {
		return redGhostImage;
	}

	public void setRedGhostImage(Image redGhostImage) {
		this.redGhostImage = redGhostImage;
	}

	public Image getScaredGhostImage() {
		return scaredGhostImage;
	}

	public void setScaredGhostImage(Image scaredGhostImage) {
		this.scaredGhostImage = scaredGhostImage;
	}

	public Image getPacmanUpImage() {
		return pacmanUpImage;
	}

	public void setPacmanUpImage(Image pacmanUpImage) {
		this.pacmanUpImage = pacmanUpImage;
	}

	public Image getPacmanDownImage() {
		return pacmanDownImage;
	}

	public void setPacmanDownImage(Image pacmanDownImage) {
		this.pacmanDownImage = pacmanDownImage;
	}

	public Image getPacmanLeftImage() {
		return pacmanLeftImage;
	}

	public void setPacmanLeftImage(Image pacmanLeftImage) {
		this.pacmanLeftImage = pacmanLeftImage;
	}

	public Image getPacmanRightImage() {
		return pacmanRightImage;
	}

	public void setPacmanRightImage(Image pacmanRightImage) {
		this.pacmanRightImage = pacmanRightImage;
	}

	public Image getCherryImage() {
		return cherryImage;
	}

	public void setCherryImage(Image cherryImage) {
		this.cherryImage = cherryImage;
	}

	public HashSet<Wall> getWalls() {
		return walls;
	}

	public void setWalls(HashSet<Wall> walls) {
		this.walls = walls;
	}

	public HashSet<Ghost> getGhosts() {
		return ghosts;
	}

	public void setGhosts(HashSet<Ghost> ghosts) {
		this.ghosts = ghosts;
	}

	public HashSet<Food> getFoods() {
		return foods;
	}

	public void setFoods(HashSet<Food> foods) {
		this.foods = foods;
	}

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

}
