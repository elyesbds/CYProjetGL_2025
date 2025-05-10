package game;
/**
 * Classe qui représente une cellule de niveau et ses caractéristiques
 */
public class Cellule {
	private int posLigne;
	private int posColonne;
	private boolean presencePiece;
	private String type; //Surement faire une enum
	/**
	 * Constructeur qui initialise une cellule
	 * @param ligne Indice de la ligne
	 * @param colonne Indice de la colonne
	 * @param presencePiece Parametere booleen qui indique si la cellule contient une piece
	 * @param type Le type de cellule (vide, mur, piege, piece)
	 */
	public Cellule(int ligne, int colonne, boolean presencePiece, String type) {
		this.posLigne=ligne;
		this.posColonne=colonne;
		this.presencePiece=presencePiece;
		this.type=type;
	}
	/**
	 * Méthode qui renvoie la ligne de la cellule
	 * @return posLigne Ligne de la cellule
	 */
	public int getPosLigne() {
		return this.posLigne;
	}
	/**
	 * Méthode qui renvoie la colonne de la celluke
	 * @return posColonne Colonne de la cellule
	 */
	public int getPosColonne() {
		return this.posColonne;
	}
	/**
	 * Méthode qui renvoie un booleen en fonction de la presence d'une piece
	 * @return true si contient une piece false si non
	 */
	public boolean contientPiece() {
		return this.presencePiece;
	}
	/**
	 * Méthode qui renvoie le type de la celluke
	 * @return type Type de la cellule
	 */
	public String getType() {
		return this.type;
	}
	/**
	 * Méthode qui met a jour le type de la cellule
	 * @param str Type de la cellule
	 */
	public void setType(String str) {
		this.type=str;
	}
	/**
	 * Compare un objet Cellule et l'objet passé en parametre pour verifier s'ils sont de la meme instance
	 * puis compare leurs coordonnées, si elles sont égales ce sont alors la même cellule
	 * @param obj L'objet a comparer
	 * 
	 * @return {@code true} Si l'objet est une Cellule avec les memes coordonées, {@code false}
	 */
	@Override
	public boolean equals(Object obj) {
		//Teste si l'objet est une Cellule
		if(obj instanceof Cellule) {
			Cellule c = (Cellule) obj;
			if(this.getPosLigne()==(c.getPosLigne()) && this.getPosColonne()==(c.getPosColonne())) {
				return true;
			}
		}
		return false;
	}
	/**
	 * Retourne une représentation textuelle d'une Cellule sous la forme:
	 * COORDONNEES | Présence pièce: BOOLEAN | TYPE
	 * @return Chaine de caracteres
	 */
	@Override
	public String toString() {
		return "("+this.getPosLigne()+","+this.getPosColonne()+") | Présence pièce: "+this.contientPiece()+" | "+this.getType();
	}
	/**
	 * Méthode qui renvoie le caractere a afficher en fonction du type de la cellule
	 * @return '#' pour mur, '.' pour piece, '1' pour joueur, ' ' pour vide, 'R' pour ennemi, '?' sinon
	 */
	public char getCaractereAffiche() {
    	switch(this.getType()) {
		case "mur":
			return('#');
		case "piece":
			return('.');
		case "joueur":
			return('1');
		case "vide":
			return(' ');
		case "piege":
			return ('*');
		case "ennemi":
			return ('R');
		default:
			return('?');
	}
	}
}
