package main.java.com.monjeu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.Timer;

public class GameEngine implements ActionListener, KeyListener{

    private Map map;

    Timer gameLoop;

    GameEngine(Map map){
        this.map = map;

        map.addKeyListener(this);
        map.setFocusable(true);

        gameLoop = new Timer(16, this);
        gameLoop.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        map.pacman.updateDirection(map.pacman.nextDirection);
        map.pacman.move();
        for(Ghost ghost : map.ghosts){
            ghost.move();
        }
        map.repaint();
        if(map.pacman.gameOver){
            gameLoop.stop();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {
        if(map.pacman.gameOver) {
            map.loadMap();
            map.resetPositions();
            map.pacman.lives = 3;
            map.pacman.score = 0;
            map.pacman.gameOver = false;
            gameLoop.start();
        }

        if (e.getKeyCode() == KeyEvent.VK_UP){
            map.pacman.updateNextDirection('U');
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN){
            map.pacman.updateNextDirection('D');
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT){
            map.pacman.updateNextDirection('L');
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT){
            map.pacman.updateNextDirection('R');
        }

    }

}
