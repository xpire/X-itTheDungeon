package main.behaviour;

import main.entities.Entity;
import main.math.Vec2i;

import java.util.ArrayList;

public class HoundBehavior implements AIBehavior {
    // TODO: change the type of the map

    @Override
    public ArrayList<Vec2i> decideMove(int[] map, Vec2i currLocation, Vec2i playerLocation, ArrayList<Integer> pastMoves) {
        // find the hunter closest to the player
        int lowestDistanceSquared = 64*64*2;
        Entity closestHunter = null; //NOT SURE ABOUT THIS
        for (Entity e : map.getEntities()) {
            if (e.getName().equals("main.enemies.Hunter")) {
                int distanceSquared = (playerLocation.getX() - e.getCoord().getX())^2 + (playerLocation.getY() - e.getCoord().getY())^2;
                if (distanceSquared <= lowestDistanceSquared) {
                    lowestDistanceSquared = distanceSquared;
                    closestHunter = e;
                }
            }
        }
        ArrayList<Vec2i> targetSquares = new ArrayList<Vec2i>();
        if (closestHunter == null) {
            // no closest hunter, main.enemies.Hound becomes a hunter
            targetSquares.add(playerLocation);
            return targetSquares;
        }
        //pretend we have a cartesian plane
        int changeX = closestHunter.getCoord().getX() - playerLocation.getX();
        int changeY = closestHunter.getCoord().getY() - playerLocation.getY());
        int width = 0;
        if (changeX == 0 && changeY > 0) {
            // Southerly Direction
            // p
            // |
            // v
            // h2
            for (int y = currLocation.getY(); y >= 0; y--) {
                for (int x = currLocation.getX() - width; x <= currLocation.getX() + width; x++) {
                    if (x < 0 || x >= map.getNCols()) continue;
                    Vec2i output = new Vec2i(x,y);
                    if (map.getType(output) == 1) // check if output is inside the map and is accessible
                        targetSquares.add(output);
                }
            }
        } else if (changeX == 0 && changeY < 0) {
            // Northerly Direction
            // h
            // ^
            // |
            // p
            for (int y = currLocation.getY(); y < map.getNRows(); y++) {
                for (int x = currLocation.getX() - width; x <= currLocation.getX() + width; x++) {
                    if (x < 0 || x >= map.getNCols()) continue;
                    Vec2i output = new Vec2i(x,y);
                    if (map.getType(output) == 1) // check if output is inside the map and is accessible
                        targetSquares.add(output);
                }
            }
        } else if (changeX > 0 && changeY == 0) {
            // Easterly Direction
            // p->h
            for (int x = currLocation.getX(); x >= 0; x--) {
                for (int y = currLocation.getY() - width; y <= currLocation.getY() + width; y++) {
                    if (y < 0 || y >= map.getNRows()) continue;
                    Vec2i output = new Vec2i(x,y);
                    if (map.getType(output) == 1) // check if output is inside the map and is accessible
                        targetSquares.add(output);
                }
            }
        } else if (changeX < 0 && changeY == 0) {
            // Westerly Direction
            // h<-p
            for (int x = currLocation.getX(); x < map.getNCols(); x++) {
                for (int y = currLocation.getY() - width; y <= currLocation.getY() + width; y++) {
                    if (y < 0 || y >= map.getNRows()) continue;
                    Vec2i output = new Vec2i(x,y);
                    if (map.getType(output) == 1) // check if output is inside the map and is accessible
                        targetSquares.add(output);
                }
            }
        } else if (changeX > 0 && changeY > 0) {
            // South-Easterly Direction
            // p
            //  \
            //  v
            //   h
            for (int x = playerLocation.getX(); x >= 0; x--) {
                for (int y = playerLocation.getY(); y >= 0; y--) {
                    Vec2i output = new Vec2i(x,y);
                    if (map.getType(output) == 1) // check if output is inside the map and is accessible
                        targetSquares.add(output);
                }
            }
        } else if (changeX > 0 && changeY < 0) {
            // North-Easterly Direction
            //    h
            //  ^
            // /
            //p
            for (int x = playerLocation.getX(); x >= 0; x--) {
                for (int y = playerLocation.getY(); y < map.getNRows(); y++) {
                    Vec2i output = new Vec2i(x,y);
                    if (map.getType(output) == 1) // check if output is inside the map and is accessible
                        targetSquares.add(output);
                }
            }
        } else if (changeX < 0 && changeY < 0) {
            // North-Westerly Direction
            //h
            // ^
            //  \
            //   p
            for (int x = playerLocation.getX(); x < map.getNCols(); x++) {
                for (int y = playerLocation.getY(); y < map.getNRows(); y++) {
                    Vec2i output = new Vec2i(x,y);
                    if (map.getType(output) == 1) // check if output is inside the map and is accessible
                        targetSquares.add(output);
                }
            }
        } else if (changeX < 0 && changeY > 0) {
            // South-Westerly Direction
            //   p
            //  /
            // v
            // h
            for (int x = playerLocation.getX(); x < map.getNCols(); x++) {
                for (int y = playerLocation.getY(); y >= 0; y--) {
                    Vec2i output = new Vec2i(x,y);
                    if (map.getType(output) == 1) // check if output is inside the map and is accessible
                        targetSquares.add(output);
                }
            }
        }
        return targetSquares;

    }
}
