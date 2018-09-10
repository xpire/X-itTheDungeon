package main.behaviour;
import main.math.Vec2i;

import java.util.ArrayList;

public class CowardBehavior implements AIBehavior {
//    @Override
//    public ArrayList<Vec2i> decideMove(int[] map, Vec2i currLocation, Vec2i playerLocation, ArrayList<Integer> pastMoves) {
//        Vec2i direction = new Vec2i(playerLocation.getX() - currLocation.getX(),
//                                    playerLocation.getY() - currLocation.getY());
//        //         N   E   S   W
//        // x (sin) 0   +   0   -   (direction[0])
//        // y (-cos)-   0   +   0   (direction[1])
//        //         0   1   2   3
//        //         0   90  180 270
//        // find out where player is in relation to coward
//        // java.lang.Math.asin(direction[0]*java.lang.Math.PI/2);
//        ArrayList<Vec2i> targetSquares = new ArrayList<Vec2i>();
//        if (direction.getX() >= 0) {
//            // EAST
//            Vec2i output = new Vec2i(currLocation.getX()+1, currLocation.getY());
//            if (map.getType(output) == 1) // check if output is inside the map and is accessible
//                targetSquares.add(output);
//        } else if (direction.getX() < 0) {
//            // WEST
//            Vec2i output = new Vec2i(currLocation.getX()-1, currLocation.getY());
//            if (map.getType(output) == 1) // check if output is inside the map and is accessible
//                targetSquares.add(output);
//        }
//        // -java.lang.Math.acos(direction[1]*java.lang.Math.PI/2);
//        if (direction.getY() >= 0) {
//            // SOUTH
//            Vec2i output = new Vec2i(currLocation.getX(), currLocation.getY()+1);
//            if (map.getType(output) == 1) // check if output is inside the map and is accessible
//                targetSquares.add(output);
//        } else if (direction.getY() < 0) {
//            // NORTH
//            Vec2i output = new Vec2i(currLocation.getX(), currLocation.getY()-1);
//            if (map.getType(output) == 1) // check if output is inside the map and is accessible
//                targetSquares.add(output);
//        }
//        return targetSquares;
//
////        return new int[0];
//    }
}
