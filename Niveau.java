package game;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.AbstractMap;
import java.util.Random;
import java.util.ArrayList;
/**
 * Cette classe <i>Niveau</i> représente un Niveau avec un tableau, des pièces, des pièges, un joueur est le statut du niveau
 * @author yanis
 * @version alpha
 */
public class Niveau {
	//Tableau représentant le niveau
	private Cellule[][] tableau;
	//Nombre de pièces dans le niveau
	private int pieces=0;
	//Etat du niveau
	private boolean estTermine=false;
	
	private static int nombreEnnemis=0;
	
	private static ArrayList<Ennemi> ennemis = new ArrayList<>();

	/**
	 * Constructeur qui prend en parametre un tableau de tableaux char et un Joueur
	 * @param tab '#' représente un mur, ' ' représente un vide, '.' représente une pièce, '*' représente un piège
	 * @param j Joueur dans le niveau
	 */
	public Niveau(Cellule[][] tab) {
		this.tableau = tab;

	}

	/**
	 * Constructeur qui crée un niveau (tableau 3x3) à partir d'un joueur spécifié.
	 * Ce constructeur initialise un tableau 3x3 où le joueur est placé au centre
	 * et le reste du tableau est rempli de murs ('#').
	 * 
	 * @param j0 Joueur du niveau qui sera placé dans le tableau
	 */

	public Niveau(Joueur j0) {
		//Créer un tableau 3x3
		this(new Cellule[3][3]);
		for(int i=0;i<3;i++) {
			for(int j=0;j<3;j++) {
				if(i==j && i==1) {
					//Met le joueur au milieu
					Cellule cel = new Cellule(i,j,false,"joueur");
					this.setCellule(cel,i,j);
					continue;
				}
				//Le rempli de murs
				Cellule cel = new Cellule(i,j,false,"mur");
				this.setCellule(cel, i, j);
			}
		}
	}
	/**
	 * Constructeur qui créer un niveau à partir d'un fichier et d'un joueur, si le fichier n'existe pas afficher une erreur
	 * @param path Chemin du fichier
	 * @param joueur0 Joueur du niveau
	 */
	public Niveau(String chemin) {
		Path filePath = Path.of(chemin); // chemin
		try {
			//Transforme le fichier en tableau de caracteres
			this.tableau= readFileToCellArray(filePath);
			for(int i=0;i<tableau.length;i++) {
				for(int j=0;j<tableau[i].length;j++) {
					if(this.tableau[i][j].contientPiece()) {
						//Si on rencontre une pièce, met a jour le nombre de pieces du niveau
						pieces++;
					}
				}
			}

		} catch (IOException e) {
			//S'il y a un probleme lors de la lecture du fichier
			System.err.println("Problème lors du chargement du fichier");
			System.exit(0);
		}
	}

	/**
	 * Méthode qui lit un fichier texte, qui en créer un tableau de Cellules et le retourne
	 * @param filePath Chemin du fichier
	 * @return Tableau de tableaux de caracteres
	 * @throws IOException Si une erreur d'entrée/sortie se produit à la lecture du fichier
	 */
	public static Cellule[][] readFileToCellArray(Path chemin) throws IOException {
		// Lire toutes les lignes du fichier
		List<String> lines = Files.readAllLines(chemin);

		// Initialiser le tableau 2D de cellules avec la taille du fichier
		int nbLignes = lines.size();
		int nbColonnes = lines.get(0).length();
		Cellule[][] cellules = new Cellule[nbLignes][nbColonnes];

		// Remplir le tableau de cellules
		for (int i = 0; i < nbLignes; i++) {
			String line = lines.get(i); // Récupérer la ligne courante
			for (int j = 0; j < nbColonnes; j++) {
				char caractere = line.charAt(j); // Récupérer le caractère à la position j
				String temp ="";
				// Créer une cellule avec les coordonnées et un type basé sur le caractère
				switch(caractere) {
				case '#':
					temp="mur";
					break;
				case '.':
					temp="piece";
					break;
				case '1':
					temp="joueur";
					break;
				case ' ':
					temp="vide";
					break;
				case '*':
					temp="piege";
					break;
				case 'R':
					temp="ennemi";
					nombreEnnemis++;
					Ennemi ennemi = new Ennemi("R" + nombreEnnemis,1,i,j,i,j);
					ennemis.add(ennemi);
					break;
				default:
					temp="?";
					break;
				}
				boolean presencePiece = temp.equals("piece");
				cellules[i][j] = new Cellule(i, j, presencePiece, temp);
			}
		}

		return cellules;
	}



	/**
	 * Méthode qui renvoie le nombre de pieces dans le niveau
	 * @return pieces Nombre de pièces dans le niveau
	 */
	public int getPieces() {
		return pieces;
	}
	/**
	 * Méthode qui renvoie le nombre d'ennemis présents dans le niveau
	 * @return
	 */
	public static int getNombreEnnemis() {
		return nombreEnnemis;
	}
	public ArrayList<Ennemi> getEnnemis(){
		return this.ennemis;
	}
	/**
	 * Méthode qui renvoie le nombre de pièces qu'il y a initialement dans le niveau
	 * 
	 * @param lvl Tableau qui représente un niveau
	 * 
	 * @return nbpieces Nombre de pieces à la création du niveau
	 */
	public int getPiecesInit(char [][] lvl) {
		int nbpieces=0;
		for(int i=0;i<lvl.length;i++) {
			for(int j=0;j<lvl[i].length;j++) {
				if(lvl[i][j]=='.') {
					//Si on rencontre une pièce, met a jour le nombre de pieces du niveau
					nbpieces++;
				}
			}
		}
		return nbpieces;
	}

	/**
	 * Méthode qui retourne un le niveau
	 * @return tableau Niveau sous forme de char[][]
	 */
	public Cellule[][] getNiveau() {
		return tableau;
	}
	/**
	 * Méthode qui retourne la taille du Niveau
	 * @return tableau.length Taille du niveau
	 */
	public int getNombreColonnes() {
		return tableau.length;
	}
	/**
	 * Méthode qui retourne la taille du tableau a l'indice donné
	 * @param index La ligne du premier tableau
	 * @return tableau[index].length Taille de la ligne
	 */
	public int getNombreLignes(int indice) {
		return tableau[indice].length;
	}
	/**
	 * Méthode qui prend en parametre un tableau de char et un int index et l'ajoute à l'index du tableau de tableau
	 * @param tab Tableau
	 * @param index Indice
	 */
	public void setLigne(Cellule[] tab,int indice){
		tableau[indice]=tab;
	}
	/**
	 * Méthode qui prend en parametre une , et l'ajoute au tableau numéro indexLigne a l'index indexColonne
	 * @param cel Cellule à ajouter
	 * @param indexSimple Position a laquelle ajouter le caractere
	 */
	public void setCellule(Cellule cel, int indiceLigne, int indiceColonne) {
		tableau[indiceLigne][indiceColonne]=cel;
	}
	/**
	 * Méthode qui retourne le caractere a l'indice [indiceDouble][indiceSimple] du tableau
	 * @param indiceDouble Indice de la ligne
	 * @param indiceSimple Indice de la colonne
	 * 
	 * @return char Caractere à l'indice indiceDouble indiceSimple
	 */
	public Cellule getCellule(int indiceDouble, int indiceSimple) {
		return tableau[indiceDouble][indiceSimple];
	}
	/**
	 * Méthode qui retourne la tableau a l'indice indexDouble du tableau
	 * @param indexDouble Indice du tableau
	 * @return Le tableau à l'indice indexDouble
	 */
	public Cellule[] getLigne(int indiceDouble) {

		return tableau[indiceDouble];
	}
	/**
	 * Retourne le joueur du niveau et une représentation textuelle du niveau sous forme de matrice.
	 * Chaque ligne de la matrice est séparée par un saut de ligne.
	 *
	 * @return Une chaîne de caractères représentant le niveau sous forme matricielle.
	 */

	@Override
	public String toString() {
		// Utilisation d'un StringBuilder pour construire la chaîne de manière efficace
		StringBuilder sb = new StringBuilder();

		// Parcours des lignes de la matrice
		for (int i = 0; i < this.getNombreColonnes(); i++) {

			// Parcours des colonnes de la ligne actuelle
			for (int j = 0; j < this.getNombreLignes(i); j++) {
				// Ajout de l'élément courant à la représentation textuelle
				sb.append(this.getCellule(i,j).getCaractereAffiche());
			}

			// Ajout d'un saut de ligne après chaque ligne de la matrice
			sb.append('\n');
		}

		// Retourne la chaîne construite
		return sb.toString();
	}
	/**
	 * Méthode qui cherche si un élément est dans le niveau, et si présent renvoie sa position sinon null
	 * @param elt Element a recherche
	 * @return tuple Couple de valeur entieres
	 */
	public AbstractMap.SimpleEntry<Integer,Integer> chercherElt(String elt) {
		for(int i=0;i<this.getNombreColonnes();i++) {

			for(int j=0;j<this.getNombreLignes(i);j++) {
				if(this.getCellule(i,j).getType()==elt) {
					AbstractMap.SimpleEntry<Integer, Integer> tuple = new AbstractMap.SimpleEntry<>(i, j);
					return tuple;
				}
			}
		}
		return null;
	}
	/**
	 * Méthode qui cherche le nombre d'occurrence d'un élément dans le niveau
	 * @param elt a chercher
	 * @return nombre d'occurrence de l'element
	 */
	public int countElt(String elt) {
		int count =0;
		for(int i=0;i<this.getNombreColonnes();i++) {

			for(int j=0;j<this.getNombreLignes(i);j++) {
				if(this.getCellule(i,j).getType()==elt) {
					count++;
				}
			}
		}
		return count;
	}

	/**
	 * Override de la méthode equals pour comparer 2 niveaux.
	 * Cette méthode compare la taille et les éléments des deux tableaux qui composent les niveaux.
	 * @param obj L'objet à comparer avec le niveau actuel
	 * @return true si les deux niveaux sont égaux (taille et contenu), false sinon
	 */
	@Override
	public boolean equals(Object obj) {
		// Vérifie si l'objet est une instance de Niveau
		if (obj instanceof Niveau) {
			Niveau tab = (Niveau) obj;

			// Compare les tailles des tableaux principaux
			if (this.getNombreColonnes() != tab.getNombreColonnes()) {
				return false;
			}

			// Compare chaque ligne du tableau
			for (int j = 0; j < this.getNombreColonnes(); j++) {
				// Compare la taille des lignes
				if (this.getNombreLignes(j) != tab.getNombreLignes(j)) {
					return false;
				}

				// Compare les éléments de chaque ligne
				for (int i = 0; i < this.getNombreLignes(j); i++) {
					// Si un élément est différent, retourne false
					if (!(this.getCellule(j, i) == tab.getCellule(j, i))) {
						return false;
					}
				}
			}
		}

		// Si tous les tests sont passés, les niveaux sont considérés égaux
		return true;
	}
	/**
	 * Méthode qui renvoie un booleen si une entite est dans le niveau
	 * @param Entite entite à vérifier
	 * @return {@code true} si l'entite est dans le niveau, {@code false} sinon
	 */
	public boolean estDansNiveau(Entite entite){
		// Vérifier si le joueur dépasse les limites en ligne
		if (entite.getPositionLigne() < 0 || entite.getPositionLigne() >= this.getNombreLignes(0)) {
			System.err.println("Le joueur dépasse en ligne");
			return false;
		}
		// Vérifier si le joueur dépasse les limites en colonne
		if (entite.getPositionColonne() < 0 || entite.getPositionColonne() >= this.getNombreColonnes()) {
			System.err.println("Le joueur dépasse en colonne");
			return false;
		}
		return true;
	}
	/**
	 * Méthode qui permet de savoir si le niveau est en cours ou non
	 * 
	 * @return {@code true} si le niveau est terminé, {@code false} sinon
	 */
	public boolean niveauTermine() {
		return this.estTermine;
	}
	/**
	 * Méthode qui prend en parametre un tableau de tableau de caracteres et l'assigne en tant que niveau
	 * @param tab Tableau qui represente un niveau
	 */
	public void setTableau(Cellule [][] tab) {
		this.tableau = tab;
	}
	
	/**
	 * Méthode qui déplace l'entite passé en parametre dans le niveau, si possible
	 * @param entite entite à déplacer
	 * @param deplacement Direction selon laquelle l'entite va être déplacé
	 **/
	public void deplacement(Entite entite, Direction deplacement) {
		
	    if (entite == null) {
	        System.err.println("Erreur: L'entité n'est pas initialisée");
	        return;
	    }
	    // On garde les anciennes positions au cas où le déplacement serait illégitime
	    int lignePrecedente = entite.getPositionLigne();
	    int colonnePrecedente = entite.getPositionColonne();

	    int[] nouvellePos = entite.calculerNouvellePosition(deplacement, this.getNombreColonnes(), this.getNombreLignes(0));
	    int nouvelleLigne = nouvellePos[0];
	    int nouvelleColonne = nouvellePos[1];

		//Verifier si le joueur atterri sur un mur
		if(this.getCellule(nouvelleLigne, nouvelleColonne) != null && this.getCellule(nouvelleLigne, nouvelleColonne).getCaractereAffiche() == '#') {
			// Si c'est un mur, on ne déplace pas le joueur
			System.out.println("L'entité ne peut pas traverser un mur!");
			return;
		}
		//Il ne se prend pas un mur
		entite.setPositionLigne(nouvelleLigne);
		entite.setPositionColonne(nouvelleColonne);
		if (entite instanceof Joueur) {
			Joueur joueur = (Joueur) entite;
	        joueur.setPositionLigne(nouvelleLigne);
	        joueur.setPositionColonne(nouvelleColonne);
	        Cellule celluleActuelle = this.getCellule(nouvelleLigne, nouvelleColonne); //pour pas répéter ca a chq fois
	        
		
		
		//On arrive ou le joueur ne s'est pas pris un mur en empruntant une cellule opposée ou normalement
		
			if(celluleActuelle.getCaractereAffiche()=='.') {
				//On rencontre une piece, on diminue le nombre de pieces dans le niveau et ajoute 100 de score au joueur
				pieces--;
				joueur.ajouterScore(100);
				celluleActuelle.setType("vide");
			}
			else if(celluleActuelle.getCaractereAffiche()=='*') {


				//On rencontre un piege, -1 vie au joueur
				
				joueur.setVies(joueur.getVies()-1);
				System.out.println("Attention, vous avez été piégé !");
				System.out.println("-1 VIE");
				celluleActuelle.setType("piege");
				joueur.resetPosition();
				//si vies du joueur = 0 alors il a perdu
				if(joueur.getVies()==0) {
					return;
				}



			}
			this.getCellule(lignePrecedente, colonnePrecedente).setType("vide");
			this.getCellule(joueur.getPositionLigne(), joueur.getPositionColonne()).setType("joueur");
			if(pieces==0) {
				System.out.println("VICTOIRE");
				System.out.println(this);
				System.out.println("Niveau suivant...");
				try {
					Thread.sleep(1000); // Pause for 1 second
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt(); // Restore interrupted status
					System.out.println("Thread was interrupted");
					System.exit(20);
				}



				this.estTermine=true;
				return;
			}
		}
		//L'entite n'est pas un joueur
		else {
			entite.deplacer(nouvelleLigne, nouvelleColonne);
			//Plus rien sur la case d'avant
			this.getCellule(lignePrecedente, colonnePrecedente).setType("vide");
			
			this.getCellule(entite.getPositionLigne(), entite.getPositionColonne()).setType("ennemi");
		}
			System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n"); //Le /clean du pauvre
			System.out.println(this);
		}
	}




