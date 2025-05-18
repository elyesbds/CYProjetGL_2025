package com.monjeu;

import java.awt.*;

public class Player extends Entity implements Mobile{

    public int score = 0;
    public int lives = 3;
    public boolean gameOver;

    Player(Image image, int x, int y, int width, int height, Map map){
        super(image, x, y, width, height, map);
        this.velocityX = 0;
        this.velocityY = 0;
        this.direction = 'U';
        gameOver = false;
    }

    @Override
    public void move(){
        if(this.direction == 'U'){
            this.image = map.pacmanUpImage;
        }
        else if(this.direction == 'D'){
            this.image = map.pacmanDownImage;
        }
        else if(this.direction == 'L'){
            this.image = map.pacmanLeftImage;
        }
        else if(this.direction == 'R'){
            this.image = map.pacmanRightImage;
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
                break;
            }
        }

        Food foodEaten= null;
        for (Food food : map.foods){
            if(collision(this, food)){
                foodEaten = food;
                this.score += 10;
            }
        }
        map.foods.remove(foodEaten);

        if(map.foods.isEmpty()){
            map.loadMap();
            map.resetPositions();
        }

    }
}
