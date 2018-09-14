package main.behaviour;

import main.entities.Entity;
import main.maploading.TileMap;
import main.math.Vec2i;

import java.util.ArrayList;

public class HoundBehaviour implements AIBehaviour {
    // TODO: change the type of the map

    @Override
    public ArrayList<Vec2i> decideMove(TileMap map, Vec2i currLocation, Vec2i playerLocation, ArrayList<Integer> pastMoves, ArrayList<main.entities.Entity> entities, ArrayList<Vec2i> entitiesCoord) {
        // find the hunter closest to the player
        int lowestDistanceSquared = 64*64*2;
        Entity closestHunter = null; //NOT SURE ABOUT THIS
        Vec2i closestHunterLocation = new Vec2i(0,0);
//        for (Entity e : map.getEntities()) {
        int counter = 0;
        for (Entity e : entities) {
            if (e.getName().equals("main.enemies.Hunter")) {
//                int distanceSquared = (playerLocation.getX() - e.getLocation().getX())^2 + (playerLocation.getY() - e.getLocation().getY())^2;
                int distanceSquared = (playerLocation.getX() - entitiesCoord.get(counter).getX())^2 + (playerLocation.getY() - entitiesCoord.get(counter).getY())^2;
                if (distanceSquared <= lowestDistanceSquared) {
                    lowestDistanceSquared = distanceSquared;
                    closestHunter = e;
                    closestHunterLocation = entitiesCoord.get(counter);

                }
            }
            counter++;
        }
        ArrayList<Vec2i> targetSquares = new ArrayList<Vec2i>();
        if (closestHunter == null) {
            // no closest hunter, main.enemies.Hound becomes a hunter
            targetSquares.add(playerLocation);
            return targetSquares;
        }
        //pretend we have a cartesian plane
//        int changeX = closestHunter.getLocation().getX() - playerLocation.getX();
//        int changeY = closestHunter.getLocation().getY() - playerLocation.getY();
        int changeX = closestHunterLocation.getX() - playerLocation.getX();
        int changeY = closestHunterLocation.getY() - playerLocation.getY();
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
                    //check if output is inside the map
                    if (output.getX() >= 0 && output.getX() < map.getNCols() &&
                            output.getY() >= 0 && output.getY() < map.getNRows()) {
                        //check if it is accessible
                        if (map.isPassable(output)) targetSquares.add(output);
                    }
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
                    //check if output is inside the map
                    if (output.getX() >= 0 && output.getX() < map.getNCols() &&
                            output.getY() >= 0 && output.getY() < map.getNRows()) {
                        //check if it is accessible
                        if (map.isPassable(output)) targetSquares.add(output);
                    }
                }
            }
        } else if (changeX > 0 && changeY == 0) {
            // Easterly Direction
            // p->h
            for (int x = currLocation.getX(); x >= 0; x--) {
                for (int y = currLocation.getY() - width; y <= currLocation.getY() + width; y++) {
                    if (y < 0 || y >= map.getNRows()) continue;
                    Vec2i output = new Vec2i(x,y);
                    //check if output is inside the map
                    if (output.getX() >= 0 && output.getX() < map.getNCols() &&
                            output.getY() >= 0 && output.getY() < map.getNRows()) {
                        //check if it is accessible
                        if (map.isPassable(output)) targetSquares.add(output);
                    }
                }
            }
        } else if (changeX < 0 && changeY == 0) {
            // Westerly Direction
            // h<-p
            for (int x = currLocation.getX(); x < map.getNCols(); x++) {
                for (int y = currLocation.getY() - width; y <= currLocation.getY() + width; y++) {
                    if (y < 0 || y >= map.getNRows()) continue;
                    Vec2i output = new Vec2i(x,y);
                    //check if output is inside the map
                    if (output.getX() >= 0 && output.getX() < map.getNCols() &&
                            output.getY() >= 0 && output.getY() < map.getNRows()) {
                        //check if it is accessible
                        if (map.isPassable(output)) targetSquares.add(output);
                    }
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
                    //check if output is inside the map
                    if (output.getX() >= 0 && output.getX() < map.getNCols() &&
                            output.getY() >= 0 && output.getY() < map.getNRows()) {
                        //check if it is accessible
                        if (map.isPassable(output)) targetSquares.add(output);
                    }
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
                    //check if output is inside the map
                    if (output.getX() >= 0 && output.getX() < map.getNCols() &&
                            output.getY() >= 0 && output.getY() < map.getNRows()) {
                        //check if it is accessible
                        if (map.isPassable(output)) targetSquares.add(output);
                    }
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
                    //check if output is inside the map
                    if (output.getX() >= 0 && output.getX() < map.getNCols() &&
                            output.getY() >= 0 && output.getY() < map.getNRows()) {
                        //check if it is accessible
                        if (map.isPassable(output)) targetSquares.add(output);
                    }
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
                    //check if output is inside the map
                    if (output.getX() >= 0 && output.getX() < map.getNCols() &&
                            output.getY() >= 0 && output.getY() < map.getNRows()) {
                        //check if it is accessible
                        if (map.isPassable(output)) targetSquares.add(output);
                    }
                }
            }
        }
        return targetSquares;

    }
}
