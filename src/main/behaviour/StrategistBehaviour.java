package main.behaviour;

import main.maploading.Level;
import main.math.Vec2i;
import main.entities.*;
import java.util.Iterator;

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
                                       ArrayList<main.entities.Entity> entities,
                                       ArrayList<Vec2i> entitiesCoord) {
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
                    int[][] markovMat = initMatrice();
                    // Use data
                    // repeated multiplication
                }
            }
            /*
            else {

            }
            */
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

        // TODO Likely error due to dimension
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

    private int[][] initMatrice() {
        int [][] ret = new int [4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                ret[i][j] = 1/4;
            }
        }
    }
}
