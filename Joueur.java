package game;

/**
 * Cette classe <i>Joueur</i> représente un joueur dans le jeu, hérite de la classe {@link Entite}.
 * Un joueur possède un nom, un nombre de vies, une position dans la grille, ainsi qu'un score.
 * 
 * @author yanis
 * @version alpha
 */
public class Joueur extends Entite {
	
    /** Score du joueur */
	private int score;

	/**
	 * Constructeur principal du joueur.
	 * 
	 * @param nom Nom du joueur
	 * @param vies Nombre de vies du joueur
	 * @param positionLigneInitiale Ligne de départ du joueur
	 * @param positionColonneInitiale Colonne de départ du joueur
	 * @param positionLigne Ligne actuelle du joueur
	 * @param positionColonne Colonne actuelle du joueur
	 */
    public Joueur(String nom, int vies, int positionLigneInitiale, int positionColonneInitiale, int positionLigne, int positionColonne) {
        super(nom, vies, positionLigneInitiale, positionColonneInitiale, positionLigne, positionColonne);
        this.score = 0;
    }

	/**
	 * Constructeur secondaire avec nom uniquement, le reste est initialisé par défaut.
	 * 
	 * @param nom Nom du joueur
	 */
    public Joueur(String nom) {
    	this(nom, 3, -1, -1, -1, -1);
    }

	/**
	 * Constructeur par défaut sans paramètres, initialise un joueur anonyme avec les valeurs par défaut.
	 */
    public Joueur() {
    	this("", 3, -1, -1, -1, -1);
    }

	/**
	 * Méthode qui ajoute un nombre de points au score du joueur, uniquement si cela ne rend pas le score négatif.
	 * 
	 * @param nb Nombre de points à ajouter (ou à soustraire si négatif)
	 */
	public void ajouterScore(int nb) {
		if (score + nb >= 0) {
			score += nb;
		}
	}

	/**
	 * Méthode qui retourne le score actuel du joueur.
	 * 
	 * @return Le score du joueur
	 */
	public int getScore() {
		return score;
	}

	/**
	 * Représentation textuelle du joueur, incluant son nom, score, position et nombre de vies.
	 * 
	 * @return Chaîne de caractères représentant le joueur
	 */
	@Override
	public String toString() {
		String var = this.getScore() > 1 ? " pts" : " pt"; 
		String vie = this.getVies() > 1 ? " vies" : " vie";
		return this.getNom() + " : " + this.getScore() + var + 
			   " (" + this.getPositionLigne() + "," + this.getPositionColonne() + ") " +
			   this.getVies() + vie;
	}
}
