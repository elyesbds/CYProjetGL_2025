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
 * <p>The neural network has a fixed architecture with 18 input neurons,
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
        props.load(new FileInputStream(new File(GeneticAlgorithm.PROPERTIES)));
        props.load(new FileInputStream(new File("ga.properties")));

        PacManDecoder decoder = new PacManDecoder();
        GeneticChromosome best = GeneticAlgorithm.run(decoder, props);

        NeuralNet net = new NeuralNet(18, 20, 4);
        net.setWeights(decoder.getPhenotype(best.getGenotype())); 
        net.save(new File(NEURAL_FILE));

        System.out.println("Best weights saved to " + NEURAL_FILE);
    }

    /**
     * Constructs the decoder and initializes the genes for the neural network.
     */
    public PacManDecoder() {
        super(generateGenes(18, 20, 4));
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
            NeuralNet net = new NeuralNet(18, 20, 4);
            net.setWeights(weights);

            double totalScore = 0;

            for (int i = 0; i < 3; i++) {
                GameAI game = new GameAI(net);
                game.reset();
                game.start();

                PlayerAI pacman = (PlayerAI) game.getMap().getPacman();
                totalScore += pacman.getScore();
            }

            return 1.0 / (totalScore / 3.0 + 1e-6);

        } catch (Exception e) {
            e.printStackTrace();
            return 1e6; // Penalized fitness in case of error
        }
    }
}
