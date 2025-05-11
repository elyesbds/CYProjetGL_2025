package game;
import java.util.AbstractMap;
import java.util.Scanner;
/**
 * Classe qui représente le jeu, comportant une liste de tous les niveaux et un joueur
 */
public class Jeu {
	private Niveau[] liste_niveaux;//Liste des niveaux pour jouer
	private Niveau[] listeNiveauxCopy;//Liste des niveaux intacts qu'on reprendra en cas de gameOver
	private Joueur joueur;//Le joueur des niveaux
	private int indiceNiveauActuel;//N° du niveau
	
	/**
	 * Constructeur qui prend en paramètres un tableau de chaine de caracteres (les chemins vers les niveaux) et un joueur
	 * @param args Tableau de chaine de caracteres, ou chaque element correspond au chemin d'un niveau
	 * @param j Joueur
	 */
	public Jeu(String[] args,Joueur j) {
		Niveau[] liste_niveaux = new Niveau[args.length]; // Tableau de args.lenght objets de type Niveau
		Niveau[] listeNiveauxCopy = new Niveau[args.length];
		for(int i=0;i<args.length;i++) {
			
			liste_niveaux[i]= new Niveau(args[i]);
			listeNiveauxCopy[i]= new Niveau(args[i]);
		}
		this.liste_niveaux = liste_niveaux;
		this.listeNiveauxCopy = listeNiveauxCopy;
		this.joueur=j;
		this.indiceNiveauActuel=0;
	}
	/**
	 * Constructeur qui génère un niveau de base avec un joueur passé en parametres
	 * @param Joueur j
	 */
	public Jeu(Joueur j){
		Niveau[] liste_niveaux = new Niveau[1]; // Création d'un tableau avec une case
		liste_niveaux[0] = new Niveau(j);
		Niveau[] listeNiveauxCopy = new Niveau[1]; // Création d'un tableau avec une case
		listeNiveauxCopy[0] = new Niveau(j);
		this.liste_niveaux = liste_niveaux;
		this.listeNiveauxCopy = listeNiveauxCopy;
		this.joueur=j;
	}
	/**
	 * Constructeur qui créer un joueur, et qui prend en parametres un tableau de chaine de caracteres (les chemins bers les niveaux)
	 * @param args Tableau de chaine de caracteres, ou chaque chaine est un chemin vers un niveau
	 */
	public Jeu(String[] args) {
		this(args, new Joueur());
	}
	/**
	 * Méthode qui renvoie l'indice du niveau actuel (Le N° du niveau)
	 * @return indiceNiveauActuel
	 */
	public int getIndiceNiveauActuel(){
		return this.indiceNiveauActuel;
	}
	/**
	 * Méthode qui renvoie la liste de tous les niveaux d'une partie
	 * @return listeNiveaux
	 */
	public Niveau[] getListeNiveaux() {
		return this.liste_niveaux;
	}
	/**
	 * Méthode qui renvoie le niveau N° indice
	 * @param indice Numéro de niveau
	 * @return liste_niveaux[indice] Niveau N°indice
	 */
	public Niveau getNiveau(int indice) {
		if(indice<this.liste_niveaux.length){
			return liste_niveaux[indice];
		}
		else {
			return null;
		}
	}
	/**
	 * Méthode qui renvoie le joueur du jeu
	 * @return joueur
	 */
	public Joueur getJoueur() {
		return this.joueur;
	}
	/**
	 * Méthode qui met à jour le joueur du jeu
	 * @param j Joueur dans le jeu
	 */
	public void setJoueur(Joueur j) {
		this.joueur=j;
	}
	/**
	 * Méthode qui met à jour la liste des niveaux du jeu
	 * @param liste Liste des niveaux
	 */
	public void setListeNiveaux(Niveau[] liste) {
		this.liste_niveaux = liste;
	}
	/**
	 * Méthode qui met à jour le niveau n°indice
	 * @param indice Indice du niveau à mettre a jour
	 * @param niveau Nouveau niveau
	 */
	public void setNiveau(int indice,Niveau niveau) {
		this.liste_niveaux[indice]=niveau;
	}
	/**
	 * Méthode qui gère le jeu, elle s'occupe de la gestion des niveaux en fonction des retours de la méthode deplacement de la classe Niveau
	 */
	public void partie(){
		boolean gameOver=false;
		boolean continuer=true;
		Niveau niveauActuel;
		try {
			niveauActuel = this.liste_niveaux[this.indiceNiveauActuel];
		} catch (IndexOutOfBoundsException e) {
			niveauActuel= new Niveau(this.joueur);
			System.out.println("Erreur : Indice du niveau actuel hors limites.");
			return;
		}
		continuer=true;
		//System.out.println(this.liste_niveaux[0]);
		//System.out.println(this.liste_niveaux[0].getCellule(niveauActuel.getNombreColonnes()-1, niveauActuel.getNombreLignes(0)-1).getCelluleOpposee());
		while (continuer) {
			//Boucle principale
			if(gameOver){
				//Si le joueur a perdu, on doit réintialiser le joueur
				gameOver=false;
				this.joueur.setVies(3);
				niveauActuel = this.listeNiveauxCopy[this.indiceNiveauActuel]; //Réinitialisation du niveau
				AbstractMap.SimpleEntry<Integer, Integer> positionInitiale = niveauActuel.chercherElt("joueur");
				this.joueur.setPositionLigne(positionInitiale.getKey());
				this.joueur.setPositionColonne(positionInitiale.getValue());
				this.joueur.ajouterScore(-this.joueur.getScore());
				System.out.println(niveauActuel);
			}
			else{
				//On entre dans le else si on a fini un niveau ou au premier niveau
				if(this.indiceNiveauActuel>=this.liste_niveaux.length){
					//Plus de niveaux
					System.out.println("Vous avez fini tous les niveaux ! A la prochaine !");
					try {
						Thread.sleep(1000); // Pause for 1 second
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt(); // Restore interrupted status
						System.out.println("Thread was interrupted");
					}
					System.exit(20);
					
				}
				//Initialisation du niveau et du joueur
				niveauActuel = this.liste_niveaux[this.indiceNiveauActuel];
				AbstractMap.SimpleEntry<Integer, Integer> positionInitiale = niveauActuel.chercherElt("joueur");
				if(positionInitiale==null){
					System.err.println("Le joueur n'a pas été trouvé dans le niveau");
					System.exit(5);
				}
				
				//Initialisation du joueur (tuple)
				this.joueur.setPositionLigne(positionInitiale.getKey());
				this.joueur.setPositionColonne(positionInitiale.getValue());
				this.joueur.setPositionLigneInitiale(positionInitiale.getKey());
				this.joueur.setPositionColonneInitiale(positionInitiale.getValue());
				System.out.println(niveauActuel);
			}
			//Le controleur permet de controler le déplacement du joueur grace aux saisies clavier de manière plus structurée
			Controleur controleur = new Controleur(niveauActuel, this.joueur);
			//Création de la fenetre de récupération des touches
			new TouchesDirectionnelles(controleur);
			//La fenetre se ferme, 2 cas possibles: Victoire ou GameOver
			if(this.joueur.getVies()==0) {
				//Alors le joueur a perdu (gameOver)
				
				gameOver=true;
				System.out.println("GAME OVER");
			    String reponse = "";
			    while(!reponse.equals("oui") && !reponse.equals("non")) {
			        //On demande tant que l'utilisateur répond mal
			        System.out.println("Voulez vous rejouer ? (Entrer oui ou non)");
						
					@SuppressWarnings("resource")
					Scanner obj = new Scanner(System.in);
				        
				    reponse = obj.nextLine();
			    }
			    switch(reponse) {
			        case "non":
			        	System.out.println("Merci d'avoir joué, a bientot");
			        	System.exit(0);
			        case "oui":
			        	//On retourne au début de la boucle sans changer indiceNiveauActuel et avec le booleen gameOver=true
			        	
			        	continue;
			}
			}
			else {
				//Alors le joueur a gagné, on passe au niveau suivant
				this.indiceNiveauActuel++;
			}
			
		}
	}
}
