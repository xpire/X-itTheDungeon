package main.behaviour;
import main.maploading.TileMap;
import main.math.Vec2i;

import java.util.ArrayList;

public class CowardBehaviour implements AIBehaviour {
    @Override
    public ArrayList<Vec2i> decideMove(TileMap map, Vec2i currLocation, Vec2i playerLocation, ArrayList<Integer> pastMoves, ArrayList<main.entities.Entity> entities, ArrayList<Vec2i> entitiesCoord) {
        Vec2i direction = new Vec2i(playerLocation.getX() - currLocation.getX(),
                                    playerLocation.getY() - currLocation.getY());
        //         N   E   S   W
        // x (sin) 0   +   0   -   (direction[0])
        // y (-cos)-   0   +   0   (direction[1])
        //         0   1   2   3
        //         0   90  180 270
        // find out where player is in relation to coward
        // java.lang.Math.asin(direction[0]*java.lang.Math.PI/2);
        ArrayList<Vec2i> targetSquares = new ArrayList<Vec2i>();
        if (direction.getX() >= 0) {
            // EAST
            Vec2i output = new Vec2i(currLocation.getX()+1, currLocation.getY());
            //check if output is inside the map
            if (output.getX() >= 0 && output.getX() < map.getNCols() &&
                output.getY() >= 0 && output.getY() < map.getNRows()) {
                //check if it is accessible
                if (map.isPassable(output)) targetSquares.add(output);
            }
        } else if (direction.getX() < 0) {
            // WEST
            Vec2i output = new Vec2i(currLocation.getX()-1, currLocation.getY());
            //check if output is inside the map
            if (output.getX() >= 0 && output.getX() < map.getNCols() &&
                    output.getY() >= 0 && output.getY() < map.getNRows()) {
                //check if it is accessible
                if (map.isPassable(output)) targetSquares.add(output);
            }
        }
        // -java.lang.Math.acos(direction[1]*java.lang.Math.PI/2);
        if (direction.getY() >= 0) {
            // SOUTH
            Vec2i output = new Vec2i(currLocation.getX(), currLocation.getY()+1);
            //check if output is inside the map
            if (output.getX() >= 0 && output.getX() < map.getNCols() &&
                    output.getY() >= 0 && output.getY() < map.getNRows()) {
                //check if it is accessible
                if (map.isPassable(output)) targetSquares.add(output);
            }
        } else if (direction.getY() < 0) {
            // NORTH
            Vec2i output = new Vec2i(currLocation.getX(), currLocation.getY()-1);
            //check if output is inside the map
            if (output.getX() >= 0 && output.getX() < map.getNCols() &&
                    output.getY() >= 0 && output.getY() < map.getNRows()) {
                //check if it is accessible
                if (map.isPassable(output)) targetSquares.add(output);
            }
        }
        return targetSquares;

//        return new int[0];
    }
}
