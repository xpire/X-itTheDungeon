package main.algorithms;

import main.math.Vec2i;

import java.util.List;

public class PageRank {

    private List<Integer> pastMoves;
    private List<Vec2i> pCoord;
    private Vec2i currPos;

    public PageRank(List<Integer> pastMoves, List<Vec2i> pCoord, Vec2i currPos) {
        this.pastMoves = pastMoves;
        this.pCoord = pCoord;
        this.currPos = currPos;
    }

    /* Getting result */
    public Vec2i getResult() {
        int[] totals = {4,4,4,4};

        float[][] markov = dataProcess(initMatrix(), pastMoves, totals);

        // repeated multiplication
        float[] init = {1,1,1,1};
        float[] resultRank = pageRank(markov, init);

        return rankedResult(resultRank, pCoord, currPos);
    }


    /**
     * Initialize the markov to a generic matrix
     * @return A initial matrix
     */
    private float[][] initMatrix() { //TODO shouldn't it be 1/4 each?
        float[][] ret = new float[4][4] ;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                ret[i][j] = 1;
            }
        }
        return  ret;
    }

    /**
     * Changes to a move frequency matrix from past moves of the player
     * @param markov The intialized matrix
     * @param pastMoves players past moves
     */
    private float[][] dataProcess(float[][] markov, List<Integer> pastMoves, int[] totals) {
        int prev = -1;

        // Use data
        for (int x: pastMoves) {
            if (prev == -1) {
                prev = x;

            } else {
                // Increase frequency
                markov[prev][x]++;

                // Increase normalizing factor
                totals[prev]++;
            }
        }

        // Get correct value
        for (int i = 0; i < 4; i++) {
//            float factor = totals[i]; TODO see if it works without this line
            for (int j = 0; j < 4; j++) {
                markov[i][j] /= totals[i];
            }
        }

        return markov;
    }

    /**
     * Applying a simple page rank algorithm
     * @param markov The markov matrix
     * @param init Initialized rank
     * @return
     */
    private float[] pageRank(float[][] markov, float[] init) {
        float[] buffer = init;

        // 20 Rounds of matrix multiplication
        for (int i = 0; i < 20; i++) {
            buffer = matMul(markov, buffer);
        }

        return buffer;
    }

    /**
     * Handles matrix multiplication for this algorithm
     * @param matrix Request matrices
     * @param vec Vector of ranks
     * @return
     */
    private float[] matMul(float[][] matrix, float[] vec) {
        float[] ret = new float[4];

        // Handles each indexing of the vector
        for (int j = 0; j < 4; j++) {
            ret[j] = sumCol(matrix[j], vec);
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
            ret += row[j] * vec[j];
        }
        return  ret;
    }

    /**
     * Get result from possible coordinates and ranks
     * @param resultRank rank result from algorithm
     * @param pCoord Possible coordinate to move to
     * @return Best square to move in
     */
    private Vec2i rankedResult(float[] resultRank, List<Vec2i> pCoord, Vec2i currLocation) {
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
    private float getRank(Vec2i x, float[]  resultRank, Vec2i currPos) {
        // Get current coordinate
        int coordX = currPos.getX();
        int coordY = currPos.getY();

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
