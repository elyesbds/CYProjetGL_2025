package com.monjeu;

import java.awt.Image;

public class RedGhost extends Ghost{


    RedGhost(Image image, int x, int y, int width, int height, Map map) {
        super(image, x, y, width, height, map);
    }

    @Override
    protected void decideDirection(){
        frameCounter ++;

        if (frameCounter == targetUpdateInterval){
            chaseTarget();
            frameCounter = 0;
        }
    }

    protected void handleWallCollision(){
        chaseTarget();
    }

    private void chaseTarget(){
        int pacmanX = map.pacman.x;
        int pacmanY = map.pacman.y;

        char bestDirection = findBestDirection(pacmanX, pacmanY);
        this.updateDirection(bestDirection);
    }

    private char findBestDirection(int targetX, int targetY){
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
