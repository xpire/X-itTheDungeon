package main.algorithms;

import main.math.Vec2i;

import java.util.Comparator;
import java.util.List;

/**
 * PageRank calculator for the strategist to determine the optimal target tile
 */
public class PageRank {

    private Vec2i pos;
    private List<Vec2i> targets;
    private List<Integer> pastMoves;

    private final int NUM_ITERATIONS = 20;

    /**
     * @param pos current enemy position
     * @param targets enemy target tiles
     * @param pastMoves past moves of the avatar
     */
    public PageRank(Vec2i pos, List<Vec2i> targets, List<Integer> pastMoves) {
        this.pos        = pos;
        this.targets    = targets;
        this.pastMoves  = pastMoves;
    }

    /**
     * Gets the optimal page rank result
     * @return the optimal result
     */
    public Vec2i getResult() {
        float[][] markov = initMarkovMatrix();
        float[] ranks = pageRank(markov);
        return getBestPage(ranks);
    }


    /**
     * Initialize the markov to a generic matrix
     * @return A initial matrix
     */
    private float[][] initMarkovMatrix() {
        float[][] markov = new float[4][4];

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                markov[i][j] = 1;
            }
        }

        int[] totals = {4,4,4,4};

        processPastMoves(markov, totals);

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                markov[i][j] /= totals[i];
            }
        }

        return markov;
    }

    /**
     * Changes to a move frequency matrix from past moves of the player
     * @param markov The initialised matrix
     */
    private void processPastMoves(float[][] markov, int[] totals) {
        pastMoves.stream()
            .reduce((prev, curr) -> {
                markov[prev][curr]++; // increase frequency
                totals[prev]++;       // increase normalising factor
                return 0;             // ignore accumulation
            });
    }

    /**
     * Applying a simple page rank algorithm
     * @param markov The markov matrix
     * @return the calculated page rank
     */
    private float[] pageRank(float[][] markov) {
        float[] ret = {1,1,1,1}; // initial ranks

        for (int i = 0; i < NUM_ITERATIONS; i++)
            ret = matMul(markov, ret);

        return ret;
    }

    /**
     * Handles matrix multiplication for this algorithm
     * @param matrix Request matrices
     * @param vec Vector of ranks
     * @return the multiplied matrix
     */
    private float[] matMul(float[][] matrix, float[] vec) {
        float[] ret = new float[4];

        for (int j = 0; j < 4; j++)
            ret[j] = sumCol(matrix[j], vec);

        return ret;
    }

    /**
     * Handles row multiplication
     * @param row Desired row
     * @param vec Current resultant vector
     * @return result of multiplication
     */
    private float sumCol(float[] row, float[] vec) {
        float ret = 0;
        for (int j = 0; j < 4; j++){
            ret += row[j] * vec[j];
        }
        return ret;
    }

    /**
     * Get result from possible coordinates and ranks
     * @param ranks rank result from algorithm
     * @return Best square to move in
     */
    private Vec2i getBestPage(float[] ranks) {
        return targets.stream()
                .max(Comparator.comparingDouble(t -> getRank(t, ranks)))
                .orElse(pos);
    }

    /**
     * Get the rank number of a specific tile in a direction
     * @param v Request vector
     * @param ranks List of rank
     * @return The rank of the coordinate
     */
    private float getRank(Vec2i v, float[] ranks) {
        if (v.equals(pos.add(Vec2i.NORTH)))
            return ranks[0];

        if (v.equals(pos.add(Vec2i.SOUTH)))
            return ranks[1];

        if (v.equals(pos.add(Vec2i.WEST)))
            return ranks[2];

        if (v.equals(pos.add(Vec2i.EAST)))
            return ranks[3];

        else
            return -1;
    }
}
