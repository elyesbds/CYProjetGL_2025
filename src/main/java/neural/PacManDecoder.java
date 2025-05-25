package neural;

import genetic.*;
import engine.*;
import entities.*;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

/**
 * The {@code PacManDecoder} class is a genetic decoder used to train 
 * a neural network to play Pac-Man using a genetic algorithm.
 * It extends {@link GeneticDecoder} and provides:
 * <ul>
 *     <li>Generation of genes corresponding to the neural network weights.</li>
 *     <li>Evaluation of the performance of a weight set through gameplay simulation.</li>
 *     <li>Training and saving of the best-performing network.</li>
 * </ul>
 *
 * <p>The neural network has a fixed architecture with 16 input neurons,
 * 20 hidden neurons, and 4 output neurons.</p>
 *
 * <p>The score achieved during gameplay is used as the fitness measure.
 * The higher the score, the lower the fitness value (as the algorithm minimizes the fitness).</p>
 * 
 */
public class PacManDecoder extends GeneticDecoder {

    /** File name used to save the trained neural network. */
    private static final String NEURAL_FILE = "n2.ser";

    /**
     * Main entry point of the program.
     * Launches the genetic algorithm to train a neural network,
     * then saves the best-performing network to a file.
     *
     * @param args command-line arguments (not used)
     * @throws Exception if an error occurs during execution
     */
    public static void main(String[] args) throws Exception {
        Properties props = new Properties();
        
        // Charger seulement le fichier ga.properties qui existe
        try {
            props.load(new FileInputStream(new File("ga.properties")));
        } catch (Exception e) {
            System.err.println("Erreur : fichier ga.properties non trouvé");
            return;
        }

        PacManDecoder decoder = new PacManDecoder();
        GeneticChromosome best = GeneticAlgorithm.run(decoder, props);
        
        NeuralNet net = new NeuralNet(16, 20, 4);
        net.setWeights(decoder.getPhenotype(best.getGenotype())); 
        net.save(new File(NEURAL_FILE));

        System.out.println("Best weights saved to " + NEURAL_FILE);
    }

    /**
     * Constructs the decoder and initializes the genes for the neural network.
     */
    public PacManDecoder() {
        super(generateGenes(16, 20, 4));
    }

    /**
     * Generates an array of genes corresponding to the weights of a neural network.
     *
     * @param input  number of input neurons
     * @param hidden number of hidden neurons
     * @param output number of output neurons
     * @return an array of genes for the genetic algorithm
     */
    private static GeneticGene[] generateGenes(int input, int hidden, int output) {
        int totalWeights = (input + 1) * hidden + (hidden + 1) * output;
        GeneticGene[] genes = new GeneticGene[totalWeights];
        for (int i = 0; i < totalWeights; i++) {
            genes[i] = new GeneticGene("w" + i, 10, -1.0, 1.0);
        }
        return genes;
    }

    /**
     * Evaluates the fitness of a given set of neural network weights.
     * The fitness is computed as the inverse of the average score across 10 game simulations.
     * The goal is to minimize this value, so higher scores yield better fitness.
     *
     * @param weights array of neural network weights
     * @return the fitness value (lower is better)
     */
@Override
public double getFitness(double[] weights) {
    try {
        NeuralNet net = new NeuralNet(16, 20, 4);
        net.setWeights(weights);

        double totalScore = 0;
        double totalSurvivalTime = 0;
        double totalExploration = 0;
        int validRuns = 0;

        // Utiliser 3 simulations pour une meilleure évaluation
        for (int i = 0; i < 3; i++) {
            GameAI game = new GameAI(net);
            game.reset();
            
            long startTime = System.currentTimeMillis();
            game.start();
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            
            // Vérifier que la simulation n'a pas pris trop de temps
            if (duration > 20000) { // Plus de 20 secondes = problème
                return 15.0; // Pénalité sévère
            }
            
            PlayerAI pacman = (PlayerAI) game.getMap().getPacman();
            int score = pacman.getScore();
            int lives = pacman.getLives();
            
            // Pénalité pour simulations trop courtes
            if (duration < 50) { // Moins de 50ms = mort immédiate
                return 12.0;
            }
            
            // Calculer un score d'exploration basé sur les zones visitées
            double explorationScore = pacman.getVisitedAreas().size() * 10.0;
            
            // Bonus pour conserver des vies (éviter les fantômes)
            double lifeBonus = lives * 100;
            
            // Bonus pour la nourriture mangée (progression)
            int totalFood = 240; // Approximation du nombre total de food items
            int remainingFood = game.getMap().getFoods().size();
            double foodProgress = ((double)(totalFood - remainingFood) / totalFood) * 200;
            
            double adjustedScore = score + lifeBonus + explorationScore + foodProgress;
            
            totalScore += adjustedScore;
            totalSurvivalTime += duration;
            totalExploration += explorationScore;
            validRuns++;
        }

        if (validRuns == 0) return 15.0;
        
        double avgScore = totalScore / validRuns;
        double avgSurvivalTime = totalSurvivalTime / validRuns;
        double avgExploration = totalExploration / validRuns;
        
        // Fitness basée sur le score ajusté (incluant bonus de vie et exploration)
        double fitness = 1.0 / (avgScore + 1.0);
        
        // Bonus supplémentaire pour survie longue
        if (avgSurvivalTime > 3000) {
            fitness *= 0.7; // Améliore significativement la fitness
        }
        
        // Bonus pour bonne exploration
        if (avgExploration > 100) {
            fitness *= 0.8; // Bonus pour explorer beaucoup de zones
        }
        
        return fitness;

    } catch (Exception e) {
        System.err.println("Erreur dans getFitness: " + e.getMessage());
        return 15.0;
    }
}

    
}