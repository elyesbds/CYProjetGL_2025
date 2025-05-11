package game;
import java.util.Random;
/**
 * Classe qui fait le lien entre les classes Niveau et TouchesDirectionnelles, pour un code plus propre
 */
public class Controleur {
	private Niveau niveau;
	private Joueur joueur;
	
	/**
	 * Constructeur qui prend en parametre le niveau a controler
	 */
	public Controleur(Niveau niveau, Joueur j){
		this.niveau=niveau;
		this.joueur = j;
	}
	/**
	 * Méthode qui renvoie si le niveau est terminé
	 * @return niveau.niveauTermine
	 */
	public boolean getStatutNiveau() {
		return this.niveau.niveauTermine();
	}
	/**
	 * Méthode qui renvoie le joueur
	 * @return joueur
	 */
	public Joueur getJoueur() {
		return this.joueur;
	}
	/**
	 * Méthode qui déplace le joueur (dans la mesure du possible)
	 * @param direction
	 */
	public void deplacerJoueur(Direction direction) {
		this.niveau.deplacement(this.joueur, direction);
	}

	public void deplacerEnnemi() {
		for (Ennemi ennemi : this.niveau.getEnnemis()) {
	        Direction[] choixDirections= Direction.values(); // tableau des valeurs de l'enum
	        Random rand = new Random();
	        Direction choix = choixDirections[rand.nextInt(choixDirections.length)];
		    this.niveau.deplacement(ennemi, choix);
		}
	}
}
