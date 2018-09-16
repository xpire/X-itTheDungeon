package main.Algorithms;

import main.math.Vec2i;

import java.util.ArrayList;

public class PageRank {
    private ArrayList<Integer>pastMoves;
    private ArrayList<Vec2i> pCoord;
    private Vec2i curLocation;
    public PageRank(ArrayList<Integer> pastMoves, ArrayList<Vec2i> pCoord, Vec2i curLocation) {
        this.pastMoves = pastMoves;
        this.curLocation = curLocation;
        this.pCoord = pCoord;
    }

    /* Getting result */
    public Vec2i getResult() {
        int[] totals = {4,4,4,4};

        float[][] stohasticMarkov = dataProcess(initMatrice(), pastMoves, totals);
        // repeated multiplication

        float[] init = {1,1,1,1};
        float[] resultRank = pageRank(stohasticMarkov, init);

        return rankedResult(resultRank, pCoord, curLocation);
    }


    /**
     * Initialize the markov to a generic matrix
     * @return A initial matrix
     */
    private float[][] initMatrice() {
        float[][] ret = new float[4][4] ;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                ret[i][j] = 1;
            }
        }
        return  ret;
    }

    /**
     * Changes to a move frequency matrice from past moves of the player
     * @param markov The intialized matrix
     * @param pastMoves players past moves
     */
    private float[][] dataProcess(float[][] markov, ArrayList<Integer> pastMoves, int[] totals) {
        int prev = -1;
        // Use data
        for (int x: pastMoves) {
            if (prev == -1) {
                prev = x;
            } else {
                // Increase frequency
                totals[prev]++;
                // increase normalizing factor
                markov[prev][x]++;
            }
        }

        // Get correct value
        for (int i = 0; i < 4; i++) {
            float factor = totals[i];
            for (int j = 0; j < 4; j++) {
                markov[i][j] = markov[i][j]/factor;
            }
        }
        return markov;
    }

    /**
     * Applying a simple page rank algorithm
     * @param markovMatrix The markov matrice
     * @param init Initialized rank
     * @return
     */
    private float[] pageRank(float[][] markovMatrix, float[] init) {
        float[] buffer = init;
        // 20 Rounds of matrix multiplication
        for (int i = 0; i < 20; i++) {
            buffer = matMul(markovMatrix, buffer);
        }
        return buffer;
    }

    /**
     * Handles matrix multiplication for this algorithm
     * @param matrix Request matrices
     * @param vector Vector of ranks
     * @return
     */
    private float[] matMul(float[][] matrix, float[] vector) {
        float[] ret = new float[4];

        // Handles each indexing of the vector
        for (int j = 0; j < 4; j++) {
            ret[j] = sumCol(matrix[j], vector);
        }
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
            ret += row[j]*vec[j];
        }
        return  ret;
    }

    /**
     * Get result from possible coordinates and ranks
     * @param resultRank rank result from algorithm
     * @param pCoord Possible coordinate to move to
     * @return Best square to move in
     */
    private Vec2i rankedResult(float[] resultRank, ArrayList<Vec2i> pCoord, Vec2i currLocation) {
        // Get current coordinate
        int coordX = currLocation.getX();
        int coordY = currLocation.getY();

        Vec2i max = null;
        float maxRank = 0;

        for (Vec2i x: pCoord) {
            if (max == null) {
                max = x;
                maxRank = getRank(x, resultRank, currLocation);
            } else {
                if (getRank(x, resultRank, currLocation) > maxRank) {
                    max = x;
                    maxRank = getRank(x, resultRank, currLocation);
                }
            }
        }
        return max;
    }

    /**
     * Get the rank number of a specific tile in a direction
     * @param x Request vector
     * @param resultRank List of rank
     * @return The rank of the coordinate
     */
    private float getRank(Vec2i x, float[]  resultRank, Vec2i currLocation) {
        // Get current coordinate
        int coordX = currLocation.getX();
        int coordY = currLocation.getY();

        // Get rank of the coordinate
        if (x.equals(new Vec2i(coordX - 1, coordY))) {
            return resultRank[2];
        }
        else if (x.equals(new Vec2i(coordX + 1, coordY))) {
            return resultRank[3];
        }
        else if (x.equals(new Vec2i(coordX, coordY - 1))) {
            return resultRank[0];
        }
        else {
            return resultRank[1];
        }
    }
}
