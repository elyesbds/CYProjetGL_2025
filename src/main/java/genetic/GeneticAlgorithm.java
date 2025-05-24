package genetic;

import java.util.Properties;

public class GeneticAlgorithm {

    public static final String PROPERTIES = "./ga.properties";

    /**
     * Runs the binary genetic algorithm with the specified fitness calculator
     * and the specified properties. This method returns the best chromosome.
     */
    public static GeneticChromosome run(GeneticDecoder decoder, Properties props) {
        GeneticPopulation pop = new GeneticPopulation(decoder, props);
        int maxGen = Integer.parseInt(props.getProperty("ga.maxgen"));
        int gen = 0;

        printHeader();
        printGen(gen, pop, decoder);

        while (gen < maxGen) {
            pop = pop.evolve();
            gen++;
            printGen(gen, pop, decoder);
        }

        return pop.getBestChromosome();
    }

    /** Prints the header for the tabular data. */
    private static void printHeader() {
        System.out.printf("%6s\t%15s\t%15s\n", "Gen", "Best Fitness", "Avg Fitness");
    }

    /** Prints the current generation data. */
    private static void printGen(int gen, GeneticPopulation pop, GeneticDecoder decoder) {
        double best = pop.getBestChromosome().getFitness();
        double avg = pop.getAverageFitness();
        System.out.printf("%6d\t%15.8f\t%15.8f\n", gen, best, avg);
    }
}

