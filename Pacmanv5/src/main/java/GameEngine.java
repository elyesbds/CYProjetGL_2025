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
        map.getPacman().updateDirection(map.getPacman().getNextDirection());
        map.getPacman().move();
        for(Ghost ghost : map.getGhosts()){
            ghost.move();
        }
        map.repaint();
        if(map.getPacman().isGameOver()){
            gameLoop.stop();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {
        if(map.getPacman().isGameOver()) {
            map.loadMap();
            map.resetPositions();
            map.getPacman().setLives(3);
            map.getPacman().setScore(0);
            map.getPacman().setGameOver(false);
            gameLoop.start();
        }

        if (e.getKeyCode() == KeyEvent.VK_UP){
            map.getPacman().updateNextDirection('U');
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN){
        	map.getPacman().updateNextDirection('D');
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT){
        	map.getPacman().updateNextDirection('L');
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT){
        	map.getPacman().updateNextDirection('R');
        }

    }

}
