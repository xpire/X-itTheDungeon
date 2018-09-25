package main.behaviour;

import main.entities.enemies.Enemy;
import main.Level;
import main.math.Vec2i;

import java.util.ArrayList;

/**
 * Implements the behaviour specific to the Coward enemy
 */
public class CowardBehaviour implements AIBehaviour {
    @Override
    public ArrayList<Vec2i> decideMove(
            Level level,
            Vec2i currLocation,
            Vec2i playerLocation,
            ArrayList<Integer> pastMoves,
            ArrayList<Enemy> entities) {
        ArrayList<Vec2i> maxDistance = new ArrayList<>();

        int maxManhattanDistance = distance(currLocation.getX(), currLocation.getY(), playerLocation);

        for (int x = currLocation.getX() - 1; x <= currLocation.getX() + 1; x++) {
            for (int y = currLocation.getY() - 1; y <= currLocation.getY() + 1; y++) {
                //continue if at diagonal square or at currLocation
                if ((x + y - currLocation.getX() - currLocation.getY())%2 == 0) continue;

                if (!check(level, new Vec2i(x,y))) continue;
                if (!level.isValidGridPos(new Vec2i(x, y))) continue;



                if (distance(x,y,playerLocation) > maxManhattanDistance) {
                    //new max, remove old max
                    maxManhattanDistance = distance(x, y, playerLocation);
                    maxDistance.clear();
                    maxDistance.add(new Vec2i(x,y));
                } else if (distance(x,y,playerLocation) == maxManhattanDistance) {
                    maxDistance.add(new Vec2i(x,y));
                }
            }
        }
        return maxDistance;
    }

    /**
     * Same thing as level.isValidGridPos(pos)
     * TODO Refactor in next iteration
     * @param map current level
     * @param location location to check
     * @return true if valid position
     */
    public Boolean check(Level map,Vec2i location) {
        return (location.getX() >= 0 && location.getX() < map.getNCols() &&
                location.getY() >= 0 && location.getY() < map.getNRows());
    }

    /**
     * Calculates the Manhatten distance
     *
     * @param x x-coord of target
     * @param y y-coord of target
     * @param playerLocation position of player
     * @return
     */
    public int distance(int x, int y, Vec2i playerLocation) {
        return Math.abs(x-playerLocation.getX()) + Math.abs(y-playerLocation.getY());
    }
}