package genetic;

import java.math.BigInteger;

/**
 * The {@code GeneticGene} class represents a single gene used in a genetic algorithm.
 * It defines a numerical value (phenotype) within a specified range, encoded as a binary string (genotype)
 * using a fixed number of bits. This class provides methods for encoding a real value into
 * a bitstring and decoding a bitstring back into a real value.
 *
 * <p>This is particularly useful in genetic algorithms where chromosomes are represented
 * as binary strings and each gene represents a parameter of the problem to optimize.</p>
 * 
 * <p>For example, a gene might encode a neural network weight in the range [-1.0, 1.0] using 10 bits.</p>
 * 
 */
public class GeneticGene {

    /** The name of the gene (for identification or debugging purposes). */
    private String name;

    /** The number of bits used to encode the gene's value. */
    private int bits;

    /** The lower bound of the value range the gene can represent. */
    private double lower;

    /** The upper bound of the value range the gene can represent. */
    private double upper;

    /**
     * Constructs a {@code GeneticGene} with the specified parameters.
     *
     * @param _name  the name of the gene
     * @param _bits  the number of bits used for encoding
     * @param _lower the lower bound of the gene's value
     * @param _upper the upper bound of the gene's value
     */
    public GeneticGene(String _name, int _bits, double _lower, double _upper) {
        name = _name;
        bits = _bits;
        lower = _lower;
        upper = _upper;
    }

    /**
     * Returns the name of the gene.
     *
     * @return the gene name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the number of bits used to encode this gene.
     *
     * @return the number of bits
     */
    public int getBits() {
        return bits;
    }

    /**
     * Returns the lower bound of the gene's value range.
     *
     * @return the lower bound
     */
    public double getLowerBound() {
        return lower;
    }

    /**
     * Returns the upper bound of the gene's value range.
     *
     * @return the upper bound
     */
    public double getUpperBound() {
        return upper;
    }

    /**
     * Encodes a given real value into a binary string representation.
     * The value must be within the gene's defined range.
     *
     * @param val the value to encode
     * @return a binary string of length {@code bits}
     */
    public String encode(double val) {
        double norm = (val - lower) / (upper - lower); // Normalize value to [0, 1]
        BigInteger bin = BigInteger.valueOf((long) Math.round(norm * (Math.pow(2, bits) - 1)));
        String _bits = bin.toString(2);

        while (_bits.length() < bits)
            _bits = "0" + _bits;

        return _bits;
    }

    /**
     * Decodes a binary string back into its corresponding real value.
     *
     * @param bitstring a binary string of length {@code bits}
     * @return the decoded value in the original range
     */
    public double decode(String bitstring) {
        double bin = new BigInteger(bitstring, 2).doubleValue();
        return lower + (upper - lower) * bin / (Math.pow(2, bits) - 1);
    }
}
