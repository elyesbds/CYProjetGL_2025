package com.monjeu;

import java.awt.*;
import java.util.Random;

public abstract class Ghost extends Entity implements Mobile{

    public char[] directions = {'U', 'D', 'L', 'R'};
    Random random = new Random();

    protected int frameCounter = 0;
    protected final int targetUpdateInterval = 1;
    protected final int changeDirectionInterval = 60;

    Ghost(Image image, int x, int y, int width, int height, Map map){
        super(image, x, y, width, height, map);
    }

    @Override
    public final void move(){
        decideDirection();

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
                
                handleWallCollision();
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
}
