package game;
import java.awt.event.*;
import javax.swing.JFrame;

/**
 * Cette classe <i> TouchesDirectionnelles </i>gère les saisies clavier nécessaires pour déplacer le joueur
 * A un attribut controleur qui va s'occuper du déplacement du joueur dans le niveau
 * @version alpha
 * @author yanis
 **/
public class TouchesDirectionnelles extends JFrame implements KeyListener {
		private static final long serialVersionUID = 1L; //Pour éviter un Warning
		private Controleur controleur;
		/**
		 * Constructeur qui prend en paramètre un controleur, qui lance une fenetre pour récupérer les saisies utilisateur
		 * @param controleur
		 **/
	    public TouchesDirectionnelles(Controleur controleur) {
	    	this.controleur=controleur;
	    	//Titre de la fenetre
	        setTitle("Minecraft");
	        //Taille de la fenetre
	        setSize(400, 300);
	        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        setFocusable(true);  // Important pour capturer les touches
	        addKeyListener(this);  // Ajout du KeyListener
	        setVisible(true);//Voir la fenetre
	        //Pour éviter que le main ne se lance directement
	        while (this.isVisible()) {
	            try {
	                Thread.sleep(100); // Petite pause pour éviter de surcharger le CPU
	            } catch (InterruptedException e) {
	            	//Si le thread est interrompu
	                e.printStackTrace();
	            }
	        }

	    }
		/**
		 * Réécriture de la méthode keyTyped pour déplacer le joueur quand une touche est pressée (ZQSD), ou fermer la fenetre avec PBEKey
		 * @param KeyEvent e
		 **/
	    @Override
	    public void keyTyped(KeyEvent e) {

	        //Déplacement en fonction de la touche tapée (ZQSD)  P Pour quitter
	        switch(e.getKeyChar()) {
	        	case 'z':
	        		this.controleur.deplacerJoueur(Direction.NORD);
	        		System.out.println(this.controleur.getJoueur());
	        		break;
	        	case 'q':
	        		this.controleur.deplacerJoueur(Direction.OUEST);
	        		System.out.println(this.controleur.getJoueur());
	        		break;
	        	case 's':
	        		this.controleur.deplacerJoueur(Direction.SUD);
	        		System.out.println(this.controleur.getJoueur());
	        		break;
	        	case 'd':
	        		this.controleur.deplacerJoueur(Direction.EST);
	        		System.out.println(this.controleur.getJoueur());
	        		break;
	        	case 'p':
	        		System.out.println("Ciao !");
	        		dispose();
	        		System.exit(1);
	        	default:
	        		//TODO
	        }
	    	if(this.controleur.getStatutNiveau() || this.controleur.getJoueur().getVies()==0) {
	    		dispose();
	    	}

	    }
		//Nécessaire au bon fonctionnement
	    @Override
	    public void keyPressed(KeyEvent e) {
	        // Détecte quand une touche est enfoncée

	    }
		//Nécessaire au bon fonctionnement
	    @Override
	    public void keyReleased(KeyEvent e) {
	        // Détecte quand une touche est relâchée

	    }
}
