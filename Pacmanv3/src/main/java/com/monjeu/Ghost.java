package main.java.com.monjeu;

import java.awt.*;
import java.util.Random;

public class Ghost extends Entity implements Mobile{

    public char[] directions = {'U', 'D', 'L', 'R'};
    Random random = new Random();

    private int frameCounter = 0;
    private final int changeDirectionInterval = 60;

    Ghost(Image image, int x, int y, int width, int height, Map map){
        super(image, x, y, width, height, map);
        char newDirection = directions[random.nextInt(4)];
        this.updateDirection(newDirection);
    }

    @Override
    public void move(){
        frameCounter ++;
        if (frameCounter == changeDirectionInterval){
            char newDirection = directions[random.nextInt(4)];
            this.updateDirection(newDirection);
            frameCounter = 0;
        }else if(this.y == map.tileSize*9 && this.direction != 'U' && this.direction != 'D'){
            this.updateDirection('U');
        }

        this.x += this.velocityX;
        this.y += this.velocityY;

        if(this.x <= 0){
            this.x = map.cols*map.tileSize - map.tileSize;
        }else if(this.x >= (map.cols*map.tileSize - map.tileSize)){
            this.x = 0;
        }

        for (Wall wall : map.walls){
            if(collision(this, wall)){
                this.x -= this.velocityX;
                this.y -= this.velocityY;
                char newDirection = directions[random.nextInt(4)];
                this.updateDirection(newDirection);
                break;
            }
        }

        if(collision(this, map.pacman)){
            map.pacman.lives -= 1;
            if(map.pacman.lives == 0){
                map.pacman.gameOver = true;
                return;
            }
            map.resetPositions();
        }
    }
}
