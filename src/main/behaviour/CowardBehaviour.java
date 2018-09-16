package main.behaviour;

import main.entities.enemies.Enemy;
import main.maploading.Level;
import main.math.Vec2i;

import java.util.ArrayList;

public class CowardBehaviour implements AIBehaviour {
    @Override
    public ArrayList<Vec2i> decideMove(
            Level map,
            Vec2i currLocation,
            Vec2i playerLocation,
            ArrayList<Integer> pastMoves,
            ArrayList<Enemy> entities
    ) {
        Vec2i direction = new Vec2i(playerLocation.getX() - currLocation.getX(),
                playerLocation.getY() - currLocation.getY());
        //         N   E   S   W
        // x (sin) 0   +   0   -   (direction[0])
        // y (-cos)-   0   +   0   (direction[1])
        //         0   1   2   3
        //         0   90  180 270
        // find out where player is in relation to coward
        // java.lang.Math.asin(direction[0]*java.lang.Math.PI/2);
//        ArrayList<Vec2i> targetSquares = new ArrayList<Vec2i>();
        System.out.printf("P (%d, %d) m:",direction.getX(), direction.getY());
        ArrayList<Vec2i> maxDistance = new ArrayList<>();
        int maxManhattanDistance = distance(currLocation.getX(),currLocation.getY(),playerLocation);
        System.out.println(maxManhattanDistance);
        for (int x = currLocation.getX()-1; x <= currLocation.getX()+1; x++) {
            for (int y = currLocation.getY()-1; y <= currLocation.getY()+1; y++) {
                //continue if at diagonal square or at currLocation
                if ((x + y - currLocation.getX() - currLocation.getY())%2 == 0) continue;
                if (!check(map,new Vec2i(x,y))) continue;
                System.out.printf("v:%d %d, m:%d\n",x,y, distance(x,y,playerLocation));
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

    public Boolean check(Level map,Vec2i location) {
        return (location.getX() >= 0 && location.getX() < map.getNCols() &&
                location.getY() >= 0 && location.getY() < map.getNRows());
    }

    public int distance(int x, int y, Vec2i playerLocation) {
        return Math.abs(x-playerLocation.getX()) + Math.abs(y-playerLocation.getY());
    }
}