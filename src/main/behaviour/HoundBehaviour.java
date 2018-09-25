package main.behaviour;

import main.entities.enemies.Enemy;
import main.maploading.Level;
import main.math.Vec2i;

import java.util.ArrayList;

/**
 * Implements the behaviour specific to the Hound
 */
public class HoundBehaviour implements AIBehaviour {

    @Override
    public ArrayList<Vec2i> decideMove(Level level,
                                       Vec2i currLocation,
                                       Vec2i playerLocation,
                                       ArrayList<Integer> pastMoves,
                                       ArrayList<Enemy> entities){
    // find the hunter closest to the player
        //GET NEAREST HUNTER FROM AISYSTEM
        Vec2i closestHunterLocation = new Vec2i(-1,-1);
        int closestHunterDistance = 64+64;
        int distance;
        for (Enemy e: level.getEnemies()) {
            if (e.isHunter()) {
                distance = (Math.abs(e.getGridPos().getX() - playerLocation.getX()) +
                        Math.abs(e.getGridPos().getY() - playerLocation.getY()));
                if (distance < closestHunterDistance) {
                    //found closer hunter
                    closestHunterDistance = distance;
                    closestHunterLocation = e.getGridPos();
                }
            }
        }

        ArrayList<Vec2i> targetSquares = new ArrayList<Vec2i>();
        if (closestHunterLocation.getX() < 0 && closestHunterLocation.getY() < 0) {
            // no closest hunter, main.enemies.Hound becomes a hunter
            targetSquares.add(playerLocation);
            return targetSquares;
        }
        //pretend we have a cartesian plane
        int changeX = closestHunterLocation.getX() - playerLocation.getX();
        int changeY = closestHunterLocation.getY() - playerLocation.getY();
        //Distance now defines distance to player Location
        distance = Math.abs(playerLocation.getX()-currLocation.getX()) + Math.abs(playerLocation.getY()-currLocation.getY());
        System.out.println(distance);
//        int changeX = currLocation.getX() - playerLocation.getX();
//        int changeY = currLocation.getY() - playerLocation.getY();
        if (changeX != 0 && changeY != 0) {
            targetSquares.addAll(quadrantSweep(level,playerLocation,!(changeX>0),!(changeY>0), distance));
        } else if (changeX == 0) {
            targetSquares.addAll(verticalSweep(level,playerLocation,!(changeY>0), distance));
        } else if (changeY == 0) {
            targetSquares.addAll(horizontalSweep(level,playerLocation,!(changeX>0), distance));
        }
        return targetSquares;
    }

    /**
     * Checks whether or not a looping limit has been reached, given an increment/decrement flag
     * @param increase inc/dec flag
     * @param value value being inc/dec
     * @param limit max value
     * @return
     */
    public Boolean checkLimit(Boolean increase, int value, int limit) {
        if (increase) {
            return (value < limit);
        } else {
            return (value >= limit);
        }
    }

    /**
     * Iterates a value given a inc/dec flag
     * @param increase inc/dev flag
     * @param value value to inc/dec
     * @return the new value
     */
    public int iterate(Boolean increase, int value) {
        if (increase) {
            return value+1;
        } else {
            return value-1;
        }
    }

    /**
     * Get the diagonally opposite quadrant to a Hunter's curr position relative to the player
     * @param map : Level the hound exists in
     * @param playerLocation : Avatar location
     * @param increaseX : inc/dec flag for X
     * @param increaseY : inc/dec flag for Y
     * @param distance : Manhatten distance
     * @return the quadrant diagonally opposite to the hound's current location
     */
    public ArrayList<Vec2i> quadrantSweep(Level map, Vec2i playerLocation, Boolean increaseX, Boolean increaseY, int distance) {
        ArrayList<Vec2i> targetSquares = new ArrayList<>();
        int limitX = 0;
        int limitY = 0;
        if (increaseX) limitX = map.getNCols();
        if (increaseY) limitY = map.getNRows();
        for (int x = playerLocation.getX(); checkLimit(increaseX,x,limitX); x = iterate(increaseX,x)) {
            for (int y = playerLocation.getY(); checkLimit(increaseY,y,limitY); y = iterate(increaseY,y)) {
                Vec2i output = new Vec2i(x,y);
                System.out.printf("o:%d %d\n",x,y);
                //check if output is inside the map
                if (Math.abs(x-playerLocation.getX()) + Math.abs(y-playerLocation.getY()) > distance) continue;
                if (output.getX() >= 0 && output.getX() < map.getNCols() &&
                        output.getY() >= 0 && output.getY() < map.getNRows()) {
                    //check if it is accessible
                    if (map.isPassableForEnemy(output, null)) targetSquares.add(output);
                }
            }
        }
        return targetSquares;
    }

    /**
     * Does the same as above, with a 45\deg rotation to the axis
     * Gets the vertically opposite quadrant to a Hunter's curr. position, relative to the Avatar
     * @param map : current level
     * @param playerLocation : Avatar's location
     * @param increase inc/dec flag
     * @param distance manhatten distance
     * @return quadrant vert. opp. to Hunters curr. position relative to the Avatar
     */
    public ArrayList<Vec2i> verticalSweep(Level map, Vec2i playerLocation, Boolean increase, int distance) {
        int limit = 0;
        ArrayList<Vec2i> targetSquares = new ArrayList<>();
        if (increase) limit = map.getNRows();
        for (int y = playerLocation.getY(); checkLimit(increase,y,limit); y = iterate(increase,y)) {
            for (int x = playerLocation.getX()-Math.abs(y-playerLocation.getY());
                 x < 1 + playerLocation.getX()+Math.abs(y-playerLocation.getY()); x++) {
                Vec2i output = new Vec2i(x,y);
                //check if output is inside the map
                if (Math.abs(output.getX()-playerLocation.getX()) + Math.abs(output.getY()-playerLocation.getY()) > distance) continue;
                if (output.getX() >= 0 && output.getX() < map.getNCols() &&
                        output.getY() >= 0 && output.getY() < map.getNRows()) {
                    //check if it is accessible
                    if (map.isPassableForEnemy(output, null)) targetSquares.add(output);
                }
            }
        }
        return targetSquares;
    }

    /**
     * 90/deg rotation of the previous method
     * Gets the horizontally opp. quadrant to a Hunter's curr postion relative to the Avatar
     * @param map : current Level
     * @param playerLocation : Avatar's position
     * @param increase : inc/dec flag
     * @param distance : manhatten distance
     * @return quadrant hori. opp. to a Hunter's curr position, relative the the Avatar
     */
    public ArrayList<Vec2i> horizontalSweep(Level map, Vec2i playerLocation, Boolean increase, int distance) {
        int limit = 0;
        ArrayList<Vec2i> targetSquares = new ArrayList<>();
        if (increase) limit = map.getNCols();
        for (int x = playerLocation.getX(); checkLimit(increase,x,limit); x = iterate(increase,x)) {
            for (int y = playerLocation.getY()-Math.abs(x-playerLocation.getX());
                 y < 1 + playerLocation.getY()+Math.abs(x-playerLocation.getX()); y++) {
                Vec2i output = new Vec2i(x,y);
                //check if output is inside the map
                if (Math.abs(output.getX()-playerLocation.getX()) + Math.abs(output.getY()-playerLocation.getY()) > distance) continue;
                if (output.getX() >= 0 && output.getX() < map.getNCols() &&
                        output.getY() >= 0 && output.getY() < map.getNRows()) {
                    //check if it is accessible
                    if (map.isPassableForEnemy(output, null)) targetSquares.add(output);

                }
            }
        }
        return targetSquares;
    }
}