package game;
//import java.nio.file.Paths;
import java.io.IOException;
import java.util.Scanner;


public class Cliente {

	public static void main(String[] args) throws IOException  {
        @SuppressWarnings("resource")
		Scanner obj = new Scanner(System.in);
        System.out.println("Entrer votre pseudo: ");
        
        @SuppressWarnings("unused")
		String pseudo = obj.nextLine();
        
        Joueur j0= new Joueur(pseudo);
        Jeu jeu;
		if(args.length==0) {
			System.err.println("Arguments non reçus !");
			jeu = new Jeu(j0);
			
		}
		else {
			jeu = new Jeu(args,j0);
		}
		jeu.partie();
		
		System.out.println("Merci d'avoir joué a bientot !");
			
	}
	}
        
