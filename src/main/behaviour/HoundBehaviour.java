package main.behaviour;

import main.Level;
import main.entities.enemies.Enemy;
import main.math.Vec2i;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.util.Collections.singletonList;

/**
 * Implements the behaviour specific to the Hound
 */

public class HoundBehaviour extends AIBehaviour {

    public HoundBehaviour(Level level, Vec2i pos, Vec2i target) {
        super(level, pos, target);
    }

    @Override
    public List<Vec2i> decideTargetTiles(){

        // find the hunter closest to the player
        Vec2i closestHunterPos = getClosestHunterPos();

        // no closest hunter, Hound becomes a hunter
        if (closestHunterPos == null) {
            ArrayList<Vec2i> targets = new ArrayList<>();
            targets.add(target);
            return singletonList(target);
        }

        // pretend we have a cartesian plane
        return getOppositeTilesTo(closestHunterPos);
    }


    private Vec2i getClosestHunterPos() {
        return level.getEnemies().stream()
                .filter(Enemy::isHunter)
                .map(Enemy::getGridPos)
                .min(Comparator.comparing(v -> v.manhattan(target)))
                .orElse(null);
    }


    private ArrayList<Vec2i> getOppositeTilesTo(Vec2i closestHunterPos) {
        int changeX = target.getX() - closestHunterPos.getX();
        int changeY = target.getY() - closestHunterPos.getY();

        int dist = pos.manhattan(target);

        if (changeX != 0 && changeY != 0)
            return quadrantSweep(changeX > 0, changeY > 0, dist);

        else if (changeX == 0)
            return verticalSweep( changeY > 0, dist);

        else // changeY == 0
            return horizontalSweep(changeX > 0 , dist);
    }


    /**
     * Get the diagonally opposite quadrant to a Hunter's curr position relative to the player
     * @param incX : inc/dec flag for X
     * @param incY : inc/dec flag for Y
     * @param dist : Manhattan distance
     * @return the quadrant diagonally opposite to the hound's current location
     */
    public ArrayList<Vec2i> quadrantSweep(Boolean incX, Boolean incY, int dist) {
        ArrayList<Vec2i> tiles = new ArrayList<>();

        int limitX = incX ? level.getNCols() - 1 : 0;
        int limitY = incY ? level.getNRows() - 1 : 0;

        for (int x = target.getX(); checkLimit(incX, x, limitX); x = iterate(incX, x)) {
            for (int y = target.getY(); checkLimit(incY, y, limitY); y = iterate(incY, y)) {

                Vec2i target = new Vec2i(x, y);
                if (isValidTile(target, dist))
                    tiles.add(target);
            }
        }
        return tiles;
    }

    /**
     * Does the same as above, with a 45\deg rotation to the axis
     * Gets the vertically opposite quadrant to a Hunter's curr. position, relative to the Avatar
     * @param increase inc/dec flag
     * @param dist Manhattan distance
     * @return quadrant vert. opp. to Hunters curr. position relative to the Avatar
     */
    public ArrayList<Vec2i> verticalSweep(Boolean increase, int dist) {

        ArrayList<Vec2i> tiles = new ArrayList<>();
        int limit = increase ? level.getNRows() - 1 : 0;

        for (int y = target.getY(); checkLimit(increase, y, limit); y = iterate(increase, y)) {

            int sweepWidth = Math.abs(y - target.getY());
            for (int x = target.getX() - sweepWidth; x <= target.getX() + sweepWidth; x++) {

                Vec2i target = new Vec2i(x, y);
                if (isValidTile(target, dist))
                    tiles.add(target);
            }
        }
        return tiles;
    }

    /**
     * 90/deg rotation of the previous method
     * Gets the horizontally opp. quadrant to a Hunter's curr postion relative to the Avatar
     * @param increase : inc/dec flag
     * @param dist : Manhattan distance
     * @return quadrant hori. opp. to a Hunter's curr position, relative the the Avatar
     */
    public ArrayList<Vec2i> horizontalSweep(Boolean increase, int dist) {
        ArrayList<Vec2i> tiles = new ArrayList<>();
        int limit = increase ? level.getNCols() : 0;

        for (int x = target.getX(); checkLimit(increase, x, limit); x = iterate(increase, x)) {

            int sweepWidth = Math.abs(x - target.getX());
            for (int y = target.getY() - sweepWidth; y <= target.getY() + sweepWidth; y++) {

                Vec2i target = new Vec2i(x, y);
                if (isValidTile(target, dist))
                    tiles.add(target);
            }
        }
        return tiles;
    }


    private boolean isValidTile(Vec2i target, int dist) {
        if (target.manhattan(target) > dist)               return false; //TODO what does this do?
        if (!level.isValidGridPos(target))                 return false;
        if (!level.isPassableForEnemy(target, null)) return false;

        return true;
    }



    /**
     * Checks whether or not a looping limit has been reached, given an increment/decrement flag
     * @param increase inc/dec flag
     * @param value value being inc/dec
     * @param limit max value
     * @return
     */
    private Boolean checkLimit(Boolean increase, int value, int limit) {
        if (increase)
            return (value <= limit);
        else
            return (value >= limit);
    }


    /**
     * Iterates a value given a inc/dec flag
     * @param increase inc/dev flag
     * @param value value to inc/dec
     * @return the new value
     */
    private int iterate(Boolean increase, int value) {
        return increase ? value + 1 : value - 1;
    }
}