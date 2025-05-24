package genetic;

import java.util.Arrays;

public class GeneticChromosome {
	
	// Chromosomes are stored in their decoded state as a bit array
	private boolean[] genome;
	private GeneticDecoder decoder;
	private double fitness;
	
	/**
	 * Creates a new GeneticChromosome with the specified number of 
	 * randomized bits. This constructor is used to generate the initial
	 * population.
	 * 
	 * @param bits number of bits
	 */
	public GeneticChromosome(GeneticDecoder _decoder, int bits) {
		decoder = _decoder;
		genome = new boolean[bits];
		for(int i = 0; i < genome.length; i++)
			genome[i] = Math.random() < 0.5;
		fitness = decoder.getFitness(getGenotype());
	}
	
	/**
	 * Creates a new GeneticChromosome from a specified bit array.
	 * 
	 * @param genome bit array
	 */
	public GeneticChromosome(GeneticDecoder _decoder, boolean[] _genome) {
		decoder = _decoder;
		genome = _genome;
		fitness = decoder.getFitness(getGenotype());
	}
	
	public double getFitness() {
		return fitness;
	}
	
	/**
	 * Returns a bit string representation (true = '1' and false = '0')
	 * of the underlying bit array.
	 * 
	 * @return bit string
	 */
	public String getGenotype() {
		StringBuilder sb = new StringBuilder();
		for(boolean bit : genome)
			sb.append(bit ? '1' : '0');
		return sb.toString();
	}
	
	/**
	 * This method mates this chromosome with a given chromosome. The specified
	 * crossover rate determines the likelihood that single-point crossover will
	 * occur between the two chromosomes. This method returns an array containing
	 * two offspring.
	 * 
	 * @param othr other parent chromosome
	 * @param rate crossover probability
	 * @return offspring chromosomes
	 */
	public GeneticChromosome[] mate(GeneticChromosome othr, double rate) {
		boolean[] c1 = Arrays.copyOf(this.genome, this.genome.length);
		boolean[] c2 = Arrays.copyOf(othr.genome, othr.genome.length);
		
		if(Math.random() <= rate) {
			int index = (int) (Math.random() * this.genome.length);
			System.arraycopy(this.genome, 0, c2, 0, index);
			System.arraycopy(othr.genome, 0, c1, 0, index);
		}
		
		return new GeneticChromosome[] { new GeneticChromosome(decoder, c1),
				 						 new GeneticChromosome(decoder, c2) };
	}
	
	/**
	 * This method randomly flips bits in the genome based on a specified 
	 * mutation probability. The greater the mutation rate, the more likely 
	 * mutations will occur.
	 * 
	 * @param rate mutation probability
	 */
	public void mutate(double rate) {
		for(int i = 0; i < genome.length; i++)
			if(Math.random() <= rate)
				genome[i] = !genome[i];
		fitness = decoder.getFitness(getGenotype());
	}
}