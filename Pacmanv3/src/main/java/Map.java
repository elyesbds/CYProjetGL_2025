import java.awt.*;
import java.util.HashSet;
import javax.swing.*;

public class Map extends JPanel {

    public int rows = 21;
    public int cols = 19;
    public int tileSize;
    public int width;
    public int height;

    public int gamesCounter = 1;
    
    private final double SMALL_FOOD_POSITION_FACTOR = 0.4; 
    private final double SMALL_FOOD_SIZE_FACTOR = 0.19;    
    
    private final double BIG_FOOD_POSITION_FACTOR = 0.31;  
    private final double BIG_FOOD_SIZE_FACTOR = 0.375;     

    public Image wallImage;
    public Image blueGhostImage;
    public Image orangeGhostImage;
    public Image pinkGhostImage;
    public Image redGhostImage;
    public Image scaredGhostImage;

    public Image pacmanUpImage;
    public Image pacmanDownImage;
    public Image pacmanLeftImage;
    public Image pacmanRightImage;

    public Image cherryImage;

    HashSet<Wall> walls;
    HashSet<Ghost> ghosts;
    HashSet<Food> foods;
    Player pacman;

    private String[] tileMap = {
            "XXXXXXXXXXXXXXXXXXX",
            "X        X        X",
            "X XX XXX X XXX XX X",
            "X  c     .     c  X",
            "X XX X XXXXX X XX X",
            "X    X       X    X",
            "XXXX XXXX XXXX XXXX",
            "OOOX X       X XOOO",
            "XXXX X XXrXX X XXXX",
            "O       bpo       O",
            "XXXX X XXXXX X XXXX",
            "OOOX X       X XOOO",
            "XXXX X XXXXX X XXXX",
            "X   .    X    .   X",
            "X XX XXX X XXX XX X",
            "X  X     P     X  X",
            "XX X X XXXXX X X XX",
            "X    X   X   X    X",
            "X XXXXXX X XXXXXX X",
            "X  c           c  X",
            "XXXXXXXXXXXXXXXXXXX"
    };

    Map(int tileSize) {
        this.tileSize = tileSize;
        this.width = cols * tileSize;
        this.height = rows * tileSize;

        setPreferredSize(new Dimension(width, height));
        setBackground(Color.BLACK);

        loadImages();
        loadMap();
    }
    
    private void loadImages() {
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
    }

    public int getTileSize() {
        return tileSize;
    }

    public void loadMap() {
        walls = new HashSet<Wall>();
        foods = new HashSet<Food>();
        ghosts = new HashSet<Ghost>();

        for(int r = 0; r < rows; r++) {
            for(int c = 0; c < cols; c++) {
                String row = tileMap[r];
                char tileMapChar = row.charAt(c);

                int x = c * tileSize;
                int y = r * tileSize;

                if(tileMapChar == 'X') {
                    Wall wall = new Wall(wallImage, x, y, tileSize, tileSize, this);
                    walls.add(wall);
                }
                else if(tileMapChar == 'b') {
                    BlueGhost ghost = new BlueGhost(blueGhostImage, x, y, tileSize, tileSize, this);
                    ghosts.add(ghost);
                }
                else if(tileMapChar == 'o') {
                    OrangeGhost ghost = new OrangeGhost(orangeGhostImage, x, y, tileSize, tileSize, this);
                    ghosts.add(ghost);
                }
                else if(tileMapChar == 'p') {
                    PinkGhost ghost = new PinkGhost(pinkGhostImage, x, y, tileSize, tileSize, this);
                    ghosts.add(ghost);
                }
                else if(tileMapChar == 'r') {
                    RedGhost ghost = new RedGhost(redGhostImage, x, y, tileSize, tileSize, this);
                    ghosts.add(ghost);
                }
                else if (tileMapChar == 'c') {
                    Food food = new Food(cherryImage, x, y, tileSize, tileSize, "cherry", this);
                    foods.add(food);
                }
                else if(tileMapChar == '.') {
                    int foodX = x + (int)(tileSize * BIG_FOOD_POSITION_FACTOR);
                    int foodY = y + (int)(tileSize * BIG_FOOD_POSITION_FACTOR);
                    int foodSize = (int)(tileSize * BIG_FOOD_SIZE_FACTOR);
                    
                    Food food = new Food(null, foodX, foodY, foodSize, foodSize, "big", this);
                    foods.add(food);
                }
                else if(tileMapChar == 'P') {
                    if (pacman == null) {
                        pacman = new Player(pacmanRightImage, x, y, tileSize, tileSize, this);
                    }
                }
                else if(tileMapChar == ' ') {
                    int foodX = x + (int)(tileSize * SMALL_FOOD_POSITION_FACTOR);
                    int foodY = y + (int)(tileSize * SMALL_FOOD_POSITION_FACTOR);
                    int foodSize = (int)(tileSize * SMALL_FOOD_SIZE_FACTOR);
                    
                    Food food = new Food(null, foodX, foodY, foodSize, foodSize, "default", this);
                    foods.add(food);
                }
            }
        }
    }

    public void resetPositions() {
        pacman.reset();
        pacman.velocityX = 0;
        pacman.velocityY = 0;

        for(Ghost ghost : ghosts) {
            ghost.reset();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        for (Wall wall : walls) {
            g.drawImage(wall.image, wall.x, wall.y, wall.width, wall.height, null);
        }

        g.setColor(Color.YELLOW);
        for (Food food : foods) {
            if(food.getFoodType() == "default" || food.getFoodType() == "big") {
                g.fillOval(food.x, food.y, food.width, food.height);
            }
            else if(food.getFoodType() == "cherry") {
                g.drawImage(food.image, food.x, food.y, food.width, food.height, null);
            }
        }

        g.drawImage(pacman.image, pacman.x, pacman.y, pacman.width, pacman.height, null);

        for (Ghost ghost : ghosts) {
            g.drawImage(ghost.image, ghost.x, ghost.y, ghost.width, ghost.height, null);
        }

        int fontSize = Math.max(16, tileSize / 2);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, fontSize));
        
        if(pacman.gameOver) {
            g.drawString("Game Over: " + String.valueOf(pacman.score), tileSize/2, tileSize/2 + fontSize/3);
            g.drawString("Niveau: " + gamesCounter, tileSize*cols - tileSize*3, tileSize/2 + fontSize/3);
        }
        else {
            g.drawString("Lives: " + String.valueOf(pacman.lives) + "   Score: " + String.valueOf(pacman.score), tileSize/2, tileSize/2 + fontSize/3);
            g.drawString("Niveau: " + gamesCounter, tileSize*cols - tileSize*3, tileSize/2 + fontSize/3);
        }
    }
}