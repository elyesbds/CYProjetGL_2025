package game;

/**
 * Classe qui représente une entité dans un niveau.
 * Une entité possède un nom, un nombre de vies, une position initiale et une position actuelle sur une grille.
 * Elle peut être déplacée ou réinitialisée à sa position de départ.
 * 
 * @author yanis
 * @version alpha
 */
public class Entite {
    
    /** Nom de l'entité */
    private String nom;

    /** Nombre de vies de l'entité */
    private int vies;

    /** Position de départ (ligne) */
    private int positionLigneInitiale;

    /** Position de départ (colonne) */
    private int positionColonneInitiale;

    /** Position actuelle (ligne) */
    private int positionLigne;

    /** Position actuelle (colonne) */
    private int positionColonne;

    /**
     * Constructeur principal de l'entité.
     * 
     * @param nom Nom de l'entité
     * @param vies Nombre de vies
     * @param positionLigneInitiale Ligne de départ
     * @param positionColonneInitiale Colonne de départ
     * @param positionLigne Ligne actuelle
     * @param positionColonne Colonne actuelle
     */
    public Entite(String nom, int vies, int positionLigneInitiale, int positionColonneInitiale, int positionLigne, int positionColonne) {
        this.nom = nom;
        this.vies = vies;
        this.positionLigneInitiale = positionLigneInitiale;
        this.positionColonneInitiale = positionColonneInitiale;
        this.positionLigne = positionLigne;
        this.positionColonne = positionColonne;
    }

    // Getters

    /**
     * @return Le nom de l'entité
     */
    public String getNom() {
        return nom;
    }

    /**
     * @return Le nombre de vies restantes
     */
    public int getVies() {
        return vies;
    }

    /**
     * @return Ligne de départ de l'entité
     */
    public int getPositionLigneInitiale() {
        return positionLigneInitiale;
    }

    /**
     * @return Colonne de départ de l'entité
     */
    public int getPositionColonneInitiale() {
        return positionColonneInitiale;
    }

    /**
     * @return Ligne actuelle de l'entité
     */
    public int getPositionLigne() {
        return positionLigne;
    }

    /**
     * @return Colonne actuelle de l'entité
     */
    public int getPositionColonne() {
        return positionColonne;
    }

    // Setters

    /**
     * @param nom Nouveau nom à attribuer
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * @param vies Nouveau nombre de vies
     */
    public void setVies(int vies) {
        this.vies = vies;
    }

    /**
     * @param positionLigneInitiale Nouvelle ligne de départ
     */
    public void setPositionLigneInitiale(int positionLigneInitiale) {
        this.positionLigneInitiale = positionLigneInitiale;
    }

    /**
     * @param positionColonneInitiale Nouvelle colonne de départ
     */
    public void setPositionColonneInitiale(int positionColonneInitiale) {
        this.positionColonneInitiale = positionColonneInitiale;
    }

    /**
     * @param positionLigne Nouvelle ligne actuelle
     */
    public void setPositionLigne(int positionLigne) {
        this.positionLigne = positionLigne;
    }

    /**
     * @param positionColonne Nouvelle colonne actuelle
     */
    public void setPositionColonne(int positionColonne) {
        this.positionColonne = positionColonne;
    }

    /**
     * Retourne une représentation textuelle de l'entité.
     * 
     * @return Chaîne décrivant l'entité
     */
    @Override
    public String toString() {
        return "Entite{" +
                "nom='" + nom + '\'' +
                ", vies=" + vies +
                ", positionLigneInitiale=" + positionLigneInitiale +
                ", positionColonneInitiale=" + positionColonneInitiale +
                ", positionLigne=" + positionLigne +
                ", positionColonne=" + positionColonne +
                '}';
    }

    /**
     * Vérifie si deux entités sont égales selon leurs attributs.
     * 
     * @param o Objet à comparer
     * @return true si les deux entités sont identiques, false sinon
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Entite)) return false;
        Entite entite = (Entite) o;
        return vies == entite.vies &&
                positionLigneInitiale == entite.positionLigneInitiale &&
                positionColonneInitiale == entite.positionColonneInitiale &&
                positionLigne == entite.positionLigne &&
                positionColonne == entite.positionColonne &&
                nom.equals(entite.nom);
    }

    /**
     * Réinitialise la position actuelle à la position initiale.
     */
    public void resetPosition() {
        this.positionLigne = this.positionLigneInitiale;
        this.positionColonne = this.positionColonneInitiale;
    }

    /**
     * Modifie la position actuelle de l'entité.
     * 
     * @param nouvelleLigne Nouvelle ligne
     * @param nouvelleColonne Nouvelle colonne
     */
    public void deplacer(int nouvelleLigne, int nouvelleColonne) {
        this.positionLigne = nouvelleLigne;
        this.positionColonne = nouvelleColonne;
    }

    /**
     * Calcule une nouvelle position en fonction d'une direction donnée, avec rebouclage aux bords.
     * 
     * @param direction Direction du déplacement
     * @param maxLignes Nombre total de lignes dans la grille
     * @param maxColonnes Nombre total de colonnes dans la grille
     * @return Tableau contenant la nouvelle ligne et la nouvelle colonne
     */
    public int[] calculerNouvellePosition(Direction direction, int maxLignes, int maxColonnes) {
        int nouvelleLigne = positionLigne;
        int nouvelleColonne = positionColonne;

        switch (direction) {
            case NORD:
                nouvelleLigne = (positionLigne - 1 + maxLignes) % maxLignes;
                break;
            case SUD:
                nouvelleLigne = (positionLigne + 1 + maxLignes) % maxLignes;
                break;
            case EST:
                nouvelleColonne = (positionColonne + 1 + maxColonnes) % maxColonnes;
                break;
            case OUEST:
                nouvelleColonne = (positionColonne - 1 + maxColonnes) % maxColonnes;
                break;
        }

        return new int[] { nouvelleLigne, nouvelleColonne };
    }
}
