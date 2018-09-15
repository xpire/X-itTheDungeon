package main.behaviour;

import main.maploading.Level;
import main.math.Vec2i;
import main.entities.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class StrategistBehaviour implements AIBehaviour {
    @Override
    public ArrayList<Vec2i> decideMove(Level map,
                                       Vec2i currLocation,
                                       Vec2i userLocation,
                                       ArrayList<Integer> pastMoves,
                                       ArrayList<main.entities.Entity> entities) {
        // Possible coordinates
        ArrayList<Vec2i> pCoord = getPossibleCoord(map,currLocation);
        // Test for all possible solutions
        if (pCoord.size() == 0) {
            // No accessible square and hence just go to the player
            pCoord.add(userLocation);
            return pCoord;
        }
        else {

            // If there is a direct goal of the user
            if (hasItem(pCoord, map)) {
                // Rank the priority of the contained goals
                Map<Vec2i, Integer> rank = new HashMap<Vec2i, Integer>();

                // Check for that all the included square have items on them or not?
                for (Vec2i x : pCoord) {
                    // Hash map to store all the ranks
                    int currRank = determineRank(x, map);
                    rank.put(x, currRank);
                }


                // Find the maximum
                Vec2i maxCoord = null;
                int maxRank = 0;
                for (Map.Entry<Vec2i, Integer> Rank : rank.entrySet()) {
                    if (maxCoord == null) {
                        maxCoord = Rank.getKey();
                        maxRank = Rank.getValue();
                    }
                    else {
                        if (maxRank < Rank.getValue()) {
                            maxCoord = Rank.getKey();
                            maxRank = Rank.getValue();
                        }
                    }
                }

                // Return that maximum coordinate
                ArrayList<Vec2i> ret = new ArrayList<>();
                ret.add(maxCoord);
                return ret;

            }
            // No item hence its markov time xD
            else {
                // If there are only 1 option left
                if (pCoord.size() == 1) {
                    return pCoord;
                }
                else {
                    int[] totals = {4,4,4,4};

                    float[][] stohasticMarkov = dataProcess(initMatrice(), pastMoves, totals);
                    // repeated multiplication

                    float[] init = {1,1,1,1};
                    float[] resultRank = pageRank(stohasticMarkov, init);

                    // Heuristic is to pick the lowest rank
                    ArrayList<Vec2i> ret = new ArrayList<>();
                    ret.add(rankedResult(resultRank, pCoord, currLocation));

                    return ret;
                }
            }
        }
    }

    /**
     * Get what coordinate is possible, note that it only checks coordinates no entities
     * @param map Current state of map
     * @param currLocation Current location of the strategist
     * @return list of possible coordinates
     */
    private ArrayList<Vec2i> getPossibleCoord(Level map, Vec2i currLocation) {
        ArrayList<Vec2i> ret = new ArrayList<>();
        // Get current coordinate
        int coordX = currLocation.getX();
        int coordY = currLocation.getY();

        // Get dimension of the map
        int dimx = map.getNCols();
        int dimy = map.getNRows();

        if (coordX - 1 >= 0 && (map.isPassable(new Vec2i(coordX - 1, coordY)))) {
            Vec2i buf1 = new Vec2i(coordX - 1, coordY);
            ret.add(buf1);
        }
        if (coordX + 1 < dimx  && (map.isPassable(new Vec2i(coordX + 1, coordY)))) {
            Vec2i buf1 = new Vec2i(coordX + 1, coordY);
            ret.add(buf1);
        }
        if (coordY - 1 >= 0 && (map.isPassable(new Vec2i(coordX, coordY - 1)))) {
            Vec2i buf1 = new Vec2i(coordX, coordY - 1);
            ret.add(buf1);
        }
        if (coordY + 1 < dimy && (map.isPassable(new Vec2i(coordX, coordY + 1)))) {
            Vec2i buf1 = new Vec2i(coordX, coordY + 1);
            ret.add(buf1);
        }
        return ret;
    }

    /**
     * Check if the coordinates have any direct items within them
     * @param pCoord All possible coordinates
     * @param map Map of current state
     * @return If there are item in the near square then return true
     */
    private boolean hasItem(ArrayList<Vec2i> pCoord, Level map) {
        // Sequential check
        for (Vec2i x: pCoord) {
            if (isItem(x, map)) {
                return true;
            }
        }
        return false;
    }

    private boolean isItem(Vec2i x, Level map) {
        List<Entity> Item = map.getTile(x).getEntities();

        switch ((Item.get(0)).getSymbol()) {
            case 'K' :
            case '$' :
            case '+' :
            case '-' :
            case '!' :
            case '>' :
            case '^' : return true;
            default  : return false;
        }
    }

    /**
     * Determine the rank of the the item on a tile, threat based level as the AI
     * assumes that player must want to protect
     * @param x Coordinate on map
     * @param map Current map state
     * @return
     */
    private int determineRank(Vec2i x,Level map) {
        // Rank of the items for priority
        List<Entity> Item = map.getTile(x).getEntities();
        switch((Item.get(0)).getSymbol()) {
            case '+' :
            case '-' :
            case '!' :
            case '>' : return 3;
            case '^' :
            case 'K' : return 2;
            case '$' : return 1;
            default  : return 0;

        }
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
