package game;

import java.util.Random;

/**
 * La classe <code>Ennemi</code> représente un ennemi dans le jeu, héritant de la classe {@link Entite}.
 * Un ennemi possède un nom, un nombre de vies, ainsi qu'une position initiale et actuelle.
 * Cette classe peut être étendue pour ajouter des comportements spécifiques aux ennemis (IA, déplacement aléatoire, etc.).
 * 
 * @author yanis
 * @version alpha
 */
public class Ennemi extends Entite {

    /**
     * Constructeur de la classe <code>Ennemi</code>.
     *
     * @param nom Nom de l'ennemi
     * @param vies Nombre de vies de l'ennemi
     * @param positionLigneInitiale Ligne de départ
     * @param positionColonneInitiale Colonne de départ
     * @param positionLigne Ligne actuelle
     * @param positionColonne Colonne actuelle
     */
    public Ennemi(String nom, int vies, int positionLigneInitiale, int positionColonneInitiale, int positionLigne, int positionColonne) {
        super(nom, vies, positionLigneInitiale, positionColonneInitiale, positionLigne, positionColonne);
    }

}
