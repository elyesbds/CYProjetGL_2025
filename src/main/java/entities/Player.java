package entities;
import java.awt.*;

import javax.swing.ImageIcon;

import entities.ghosts.Ghost;
import map.Food;
import map.Map;
import map.Wall;

public class Player extends MobileEntity implements Mobile{

    private int score = 0;
    private int lives = 3;
    private boolean gameOver;
    

    public Player(Image image, int x, int y, int width, int height, Map map){
        super(image, x, y, width, height, map);
        super.setVelocityX(0);
        super.setVelocityY(0);
        super.setDirection('U');
        this.gameOver = false;
    }

    @Override
    public void move(){
        if(super.getDirection() == 'U'){
        	super.setImage(new ImageIcon(getClass().getResource("/pacmanUp.png")).getImage());
        }
        else if(super.getDirection() == 'D'){
        	super.setImage(new ImageIcon(getClass().getResource("/pacmanDown.png")).getImage());
        }
        else if(super.getDirection()== 'L'){
        	super.setImage(new ImageIcon(getClass().getResource("/pacmanLeft.png")).getImage());
        }
        else if(super.getDirection() == 'R'){
        	super.setImage(new ImageIcon(getClass().getResource("/pacmanRight.png")).getImage());
        }
        super.setX(super.getX()+this.getVelocityX());
        super.setY(super.getY()+this.getVelocityY());

        if(super.getX() <= 0){
            super.setX(getMap().getCols()*getMap().getTileSize() - getMap().getTileSize());
        }else if(super.getX() >= (getMap().getCols()*getMap().getTileSize() - getMap().getTileSize())){
            super.setX(0);
        }

        for (Wall wall : getMap().getWalls()){
            if(collision(this, wall)){
                
                super.setX(super.getX()-this.getVelocityX());
                super.setY(super.getY()-this.getVelocityY());

                break;
            }
        }

        Food foodEaten= null;
        for (Food food : getMap().getFoods()){
            if(collision(this, food)){
                foodEaten = food;
                this.score+=foodEaten.getScore();
                if("big".equals(food.getFoodType())){
                    for(Ghost ghost : getMap().getGhosts()){
                        ghost.setScared(true);
                    }
                }
                
            }
        }
        getMap().getFoods().remove(foodEaten);

        if(getMap().getFoods().isEmpty()){
        	getMap().setLevel(getMap().getLevel()+1);
            getMap().loadMap();
            getMap().resetPositions();
            
        }

    }

    @Override
    public void updateVelocity(){
        int s = 10;
        int tileSize = super.getMap().getTileSize();
        int step = Math.round(tileSize/s);

        alignToGrid(step);

        if (super.getDirection() == 'U'){
            super.setVelocityX(0);
            super.setVelocityY(-step);
        }
        else if (super.getDirection() == 'D'){
            super.setVelocityX(0);
            super.setVelocityY(step);
        }
        else if (super.getDirection() == 'L'){
            super.setVelocityX(-step);
            super.setVelocityY(0);
        }
        else if (super.getDirection() == 'R'){
            super.setVelocityX(step);
            super.setVelocityY(0);
        }
    }

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getLives() {
		return lives;
	}

	public void setLives(int lives) {
		this.lives = lives;
	}

	public boolean isGameOver() {
		return gameOver;
	}

	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}
}
