import java.awt.*;
import java.util.Random;

import javax.swing.ImageIcon;

public abstract class Ghost extends Entity implements Mobile{

    protected Point center;
    protected int radius;

    public char[] directions = {'U', 'D', 'L', 'R'};
    Random random = new Random();
    private Image realImage;
    public Image scaredGhostImage;
    public Image whiteScaredGhostImage;  
    public Image eatenGhostImage;

    protected boolean isScared;
    protected boolean isEaten; 
    protected int frameCounter = 0;
    protected int scaredTimer = 0;
    protected int respawnTimer = 0;
    protected final int respawnDelay = 180;
    protected final int endScaredTimer = 300;
    protected final int targetUpdateInterval = 1;
    protected final int changeDirectionInterval = 60;

    Ghost(Image image, int x, int y, int width, int height, Map map){
        super(image, x, y, width, height, map);
        this.isScared = false;
        this.isEaten = false;
        this.realImage = image;
        scaredGhostImage = new ImageIcon(getClass().getResource("/scaredGhost.png")).getImage();
        eatenGhostImage = new ImageIcon(getClass().getResource("/ghostEyes.png")).getImage();
        whiteScaredGhostImage = new ImageIcon(getClass().getResource("/whiteScaredGhost.png")).getImage();
    }

    public int getRadius() {
        return width / 2;
    }

    public Point getCenter() {
        return new Point(x + width / 2, y + height / 2);
    }

    protected boolean isInTunnel() {
        int tunnelY = 9 * map.tileSize; 
        return (this.y >= tunnelY && this.y < tunnelY + map.tileSize) &&
            (this.x < 3 * map.tileSize || this.x > (map.cols - 3) * map.tileSize);
    }

    @Override
    public final void move(){
        if(this.isEaten){
            respawnTimer++;
            if(respawnTimer >= respawnDelay){
                respawnTimer = 0;
                isEaten = false;
                this.setImage(this.realImage);
                this.isScared = false;
                this.scaredTimer = 0;
            }
            return;
        }

        if(this.isScared){
            scaredMove();
        }
        else{
            decideDirection();
        }

        double speedMultiplier = this.isInTunnel() ? 0.75 : 1.0;

        this.x += this.velocityX * speedMultiplier;
        this.y += this.velocityY * speedMultiplier;

        if(this.x <= 0){
            this.x = map.cols*map.tileSize - map.tileSize;
        }else if(this.x >= (map.cols*map.tileSize - map.tileSize)){
            this.x = 0;
        }

        for (Wall wall : map.walls){
            if(collision(this, wall)){
                this.x -= this.velocityX * speedMultiplier;
                this.y -= this.velocityY * speedMultiplier;
                
                handleWallCollision();
                break;
            }
        }

        if(this.isColliding(map.pacman)){
            if(this.isScared){
                this.x = this.startX;
                this.y= this.startY;
                this.isEaten = true;
                this.setImage(this.eatenGhostImage);
                map.pacman.score+=50;
            }
            else{
                map.pacman.lives -= 1;
                if(map.pacman.lives == 0){
                    map.pacman.gameOver = true;
                return;
            }
            map.resetPositions();
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
        }
        if(scaredTimer >= endScaredTimer){
            this.isScared = false;
            this.setImage(this.realImage);
            this.scaredTimer = 0;
        }else if (scaredTimer >= endScaredTimer-120 && scaredTimer < endScaredTimer){
            if(this.image == scaredGhostImage && scaredTimer%10 == 0){
                this.setImage(whiteScaredGhostImage);
            }else if (scaredTimer%10 == 0){
                this.setImage(scaredGhostImage);
            }
        }
    }

    protected char findBestDirection(int targetX, int targetY){
        char bestDir = ' ';
        double shortestDistance = Double.MAX_VALUE;

        for(char dir : directions){
            if(isOppositeDirection(dir, this.direction)){
                continue;
            }

            int newX = this.x;
            int newY = this.y;

            if (dir == 'U'){
                newY -= map.tileSize/16;
            }
            else if (dir == 'D'){
                newY += map.tileSize/16;
            }
            else if (dir == 'L'){
                newX -= map.tileSize/16;
            }
            else if (dir == 'R'){
                newX += map.tileSize/16;
            }

            boolean collides = false;
            for (Wall wall : map.walls) {
                if (collision(new Entity(null, newX, newY, this.width, this.height, map), wall)) {
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
}
