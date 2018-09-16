package main.behaviour;

// Going around boulder and avoid arrows
import main.Algorithms.PageRank;
import main.entities.enemies.Enemy;
import main.entities.pickup.Pickup;
import main.maploading.Level;
import main.math.Vec2i;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;

public class StrategistBehaviour implements AIBehaviour {
    @Override
    public ArrayList<Vec2i> decideMove(Level level,
                                       Vec2i currLocation,
                                       Vec2i userLocation,
                                       ArrayList<Integer> pastMoves,
                                       ArrayList<Enemy> entities) {
        // Possible coordinates
        ArrayList<Vec2i> pCoord = getPossibleCoord(level,userLocation);

        // Test for all possible solutions
        if (pCoord.size() == 0) {
            // No accessible square and hence just go to the player
            pCoord.add(userLocation);
            return pCoord;
        }
        else if (hasPlayer(userLocation, currLocation)) {
            ArrayList<Vec2i> ret = new ArrayList<>();
            ret.add(userLocation);
            return ret;
        }
        else if (currLocation.equals(userLocation)) {
            ArrayList<Vec2i> ret = new ArrayList<>();
            ret.add(currLocation);
            return ret;
        }
        else {

            // If there is a direct goal of the user
            if (hasItem(pCoord, level)) {
                // Rank the priority of the contained goals
                Map<Vec2i, Integer> rank = new HashMap<>();

                // Check for that all the included square have items on them or not?
                for (Vec2i x : pCoord) {
                    // Hash level to store all the ranks
                    int currRank = determineRank(x, level);
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
                    PageRank rankedResult = new PageRank(pastMoves, pCoord, currLocation);
                    Vec2i preferredResult = rankedResult.getResult();

                    // Heuristic is to pick the lowest rank
                    ArrayList<Vec2i> ret = new ArrayList<>();
                    ret.add(preferredResult);

                    return ret;
                }
            }
        }
    }

    /**
     * Get what coordinate is possible, note that it only checks coordinates no entities
     * @param level Current state of level
     * @param userLocation Current location of the strategist
     * @return list of possible coordinates
     */
    private ArrayList<Vec2i> getPossibleCoord(Level level, Vec2i userLocation) {
        ArrayList<Vec2i> ret = new ArrayList<>();
        // Get current coordinate
        int coordX = userLocation.getX();
        int coordY = userLocation.getY();

        // Get dimension of the level
        int dimx = level.getNCols();
        int dimy = level.getNRows();

        if (coordX - 1 >= 0 && (level.isPassableForEnemy(new Vec2i(coordX - 1, coordY), null))) {
            Vec2i buf1 = new Vec2i(coordX - 1, coordY);
            ret.add(buf1);
        }
        if (coordX + 1 < dimx  && (level.isPassableForEnemy(new Vec2i(coordX + 1, coordY), null))) {
            Vec2i buf1 = new Vec2i(coordX + 1, coordY);
            ret.add(buf1);
        }
        if (coordY - 1 >= 0 && (level.isPassableForEnemy(new Vec2i(coordX, coordY - 1), null))) {
            Vec2i buf1 = new Vec2i(coordX, coordY - 1);
            ret.add(buf1);
        }
        if (coordY + 1 < dimy && (level.isPassableForEnemy(new Vec2i(coordX, coordY + 1), null))) {
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

    /**
     * If the tile x has a items
     * @param x The requested tile position
     * @param level the current state of the map
     * @return
     */
    private boolean isItem(Vec2i x, Level level) {
        Iterator Items = level.getPickupIterator();

        while(Items.hasNext()) {
            Pickup Item =  (Pickup)Items.next();

            if (x.equals(Item.getGridPos())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determine the rank of the the item on a tile, threat based ranks as the AI
     * assumes that player must want to protect
     * @param x Coordinate on map
     * @param level Current map state
     * @return
     */
    private int determineRank(Vec2i x,Level level) {
        Iterator Items = level.getPickupIterator();

        while(Items.hasNext()) {
            Pickup Item =  (Pickup)Items.next();
            if (x.equals(Item.getGridPos())) {
                return Item.getScore();
            }
        }
        return 0;
    }

    /**
     * @param request The list to check if player is reachable
     * @return if the player is near the strategist
     */
    private boolean hasPlayer(Vec2i request, Vec2i currLocation) {
        // Get current coordinate
        int coordX = currLocation.getX();
        int coordY = currLocation.getY();

        if (new Vec2i(coordX - 1, coordY).equals(request)) {
            return true;
        }
        if (new Vec2i(coordX + 1, coordY).equals(request)) {
            return true;
        }
        if (new Vec2i(coordX, coordY - 1).equals(request)) {
            return true;
        }
        if (new Vec2i(coordX, coordY + 1).equals(request)) {
            return true;
        }
        return false;
    }
}
