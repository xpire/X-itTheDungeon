package Behavior;

import java.util.ArrayList;

public class HoundBehavior implements AIBehavior {
    // TODO: change the type of the map
    @Override
    public int[] decideMove(int[] map, int[] currLocation, int[] playerLocation, ArrayList<Integer> pastMoves) {
        // find the hunter closest to the player
        lowestDistanceSquared = 64*64*2;
        closestHunter = null;
        for (Entity e : map.getEntities()) {
            if (e.getName = "Hunter") {
                distanceSquared = (playerLocation[0] - e.getX())^2 + (playerLocation[1] - e.getY())^2;
                if (distanceSquared <= lowestDistanceSquared) {
                    lowestDistanceSquared = distanceSquared;
                    closestHunter = e;
                }
            }
        }
        ArrayList<int[]> targetSquares = new ArrayList<int[2]>();
        int[] output = new int[2];
        if (closestHunter == null) {
            // no closest hunter, Hound becomes a hunter
            targetSquares.add(playerLocation);
            return targetSquares;
        }
        //pretend we have a cartesian plane
        changeX = closestHunter.getX() - playerLocation[0];
        changeY = closestHunter.getY() - playerLocation[1]);
        if (changeX == 0 && changeY > 0) {
            // Southerly Direction
            // p
            // |
            // v
            // h
            int width = 0;
            for (int y = currLocation[1]; y >= 0; y--) {
                for (int x = currLocation[0] - width; x <= currLocation[0] + width; x++) {
                    if (x < 0 || x >= map.getX()) continue;
                    output[0] = x;
                    output[1] = y;
                    // check if output is inside the map and is accessible
                    targetSquares.add(output);
                }
            }
        } else if (changeX == 0 && changeY < 0) {
            // Northerly Direction
            // h
            // ^
            // |
            // p
            for (int y = currLocation[1]; y < map.getY(); y++) {
                for (int x = currLocation[0] - width; x <= currLocation[0] + width; x++) {
                    if (x < 0 || x >= map.getX()) continue;
                    output[0] = x;
                    output[1] = y;
                    // check if output is inside the map and is accessible
                    targetSquares.add(output);
                }
            }
        } else if (changeX > 0 && changeY == 0) {
            // Easterly Direction
            // p->h
            for (int x = currLocation[0]; x < map.getX(); x++) {
                for (int y = currLocation[1] - width; y <= currLocation[1] + width; y++) {
                    if (y < 0 || y >= map.getY()) continue;
                    output[0] = x;
                    output[1] = y;
                    // check if output is inside the map and is accessible
                    targetSquares.add(output);
                }
            }
        } else if (changeX < 0 && changeY == 0) {
            // Westerly Direction
            // h<-p
            for (int x = currLocation[0]; x >= 0; x--) {
                for (int y = currLocation[1] - width; y <= currLocation[1] + width; y++) {
                    if (y < 0 || y >= map.getY()) continue;
                    output[0] = x;
                    output[1] = y;
                    // check if output is inside the map and is accessible
                    targetSquares.add(output);
                }
            }
        } else if (changeX > 0 && changeY > 0) {
            // South-Easterly Direction
            // p
            //  \
            //  v
            //   h
            for (int x = playerLocation[0]; x < map.getX(); x++) {
                for (int y = playerLocation[1]; y < map.getY(); y++) {
                    output[0] = x;
                    output[1] = y;
                    // check if output is inside the map and is accessible
                    targetSquares.add(output);
                }
            }
        } else if (changeX > 0 && changeY < 0) {
            // North-Easterly Direction
            //    h
            //  ^
            // /
            //p
            for (int x = playerLocation[0]; x < map.getX(); x++) {
                for (int y = playerLocation[1]; y >= 0; y--) {
                    output[0] = x;
                    output[1] = y;
                    // check if output is inside the map and is accessible
                    targetSquares.add(output);
                }
            }
        } else if (changeX < 0 && changeY < 0) {
            // North-Westerly Direction
            //h
            // ^
            //  \
            //   p
            for (int x = playerLocation[0]; x >= 0; x--) {
                for (int y = playerLocation[1]; y >= 0; y--) {
                    output[0] = x;
                    output[1] = y;
                    // check if output is inside the map and is accessible
                    targetSquares.add(output);
                }
            }
        } else if (changeX < 0 && changeY > 0) {
            // South-Westerly Direction
            //   p
            //  /
            // v
            // h
            for (int x = playerLocation[0]; x >= 0; x--) {
                for (int y = playerLocation[1]; y < map.getY(); y++) {
                    output[0] = x;
                    output[1] = y;
                    // check if output is inside the map and is accessible
                    targetSquares.add(output);
                }
            }
        }
        return targetSquares;

    }
}
