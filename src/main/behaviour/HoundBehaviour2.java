package main.behaviour;

import main.Level;
import main.entities.enemies.Enemy;
import main.math.Vec2i;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Implements the behaviour specific to the Hound
 */
public class HoundBehaviour2 implements AIBehaviour {

    @Override
    public ArrayList<Vec2i> decideMove(Level level,
                                       Vec2i currPos,
                                       Vec2i playerPos,
                                       ArrayList<Integer> pastMoves,
                                       ArrayList<Enemy> enemies){


        // find the hunter closest to the player
        Vec2i closestHunterPos = level.getEnemies().stream()
                .filter(Enemy::isHunter)
                .map(Enemy::getGridPos)
                .min(Comparator.comparing(v -> v.manhattan(playerPos)))
                .orElse(null);

        ArrayList<Vec2i> targets = new ArrayList<>();

        // no closest hunter, Hound becomes a hunter
        if (closestHunterPos == null) {
            targets.add(playerPos);
            return targets;
        }

        //pretend we have a cartesian plane
        int changeX = closestHunterPos.getX() - playerPos.getX();
        int changeY = closestHunterPos.getY() - playerPos.getY();

        //Distance now defines distance to player Location
        int dist = currPos.manhattan(playerPos);

        if (changeX != 0 && changeY != 0)
            targets.addAll(quadrantSweep(level, playerPos, !(changeX>0), !(changeY>0), dist));

        else if (changeX == 0)
            targets.addAll(verticalSweep(level, playerPos,!(changeY>0), dist));

        else if (changeY == 0)
            targets.addAll(horizontalSweep(level, playerPos,!(changeX>0), dist));

        return targets;
    }


    /**
     * Checks whether or not a looping limit has been reached, given an increment/decrement flag
     * @param increase inc/dec flag
     * @param value value being inc/dec
     * @param limit max value
     * @return
     */
    public Boolean checkLimit(Boolean increase, int value, int limit) {
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
    public int iterate(Boolean increase, int value) {
        return increase ? value + 1 : value - 1;
    }


    /**
     * Get the diagonally opposite quadrant to a Hunter's curr position relative to the player
     * @param level : Level the hound exists in
     * @param playerPos : Avatar location
     * @param incX : inc/dec flag for X
     * @param incY : inc/dec flag for Y
     * @param dist : Manhatten distance
     * @return the quadrant diagonally opposite to the hound's current location
     */
    public ArrayList<Vec2i> quadrantSweep(Level level, Vec2i playerPos, Boolean incX, Boolean incY, int dist) {
        ArrayList<Vec2i> targets = new ArrayList<>();

        int limitX = incX ? level.getNCols() - 1 : 0;
        int limitY = incY ? level.getNRows() - 1 : 0;

        for (int x = playerPos.getX(); checkLimit(incX, x, limitX); x = iterate(incX, x)) {
            for (int y = playerPos.getY(); checkLimit(incY, y, limitY); y = iterate(incY, y)) {

                Vec2i target = new Vec2i(x, y);

                if (target.manhattan(playerPos) > dist) continue; // TODO: what does this do?
                if (!level.isValidGridPos(target)) continue;
                if (!level.isPassableForEnemy(target, null)) continue;

                targets.add(target);
            }
        }
        return targets;
    }

    /**
     * Does the same as above, with a 45\deg rotation to the axis
     * Gets the vertically opposite quadrant to a Hunter's curr. position, relative to the Avatar
     * @param level : current level
     * @param playerPos : Avatar's location
     * @param increase inc/dec flag
     * @param dist manhatten distance
     * @return quadrant vert. opp. to Hunters curr. position relative to the Avatar
     */
    public ArrayList<Vec2i> verticalSweep(Level level, Vec2i playerPos, Boolean increase, int dist) {

        ArrayList<Vec2i> targets = new ArrayList<>();
        int limit = increase ? level.getNRows() - 1 : 0;

        for (int y = playerPos.getY(); checkLimit(increase, y, limit); y = iterate(increase, y)) {

            int sweepWidth = Math.abs(y - playerPos.getY());
            for (int x = playerPos.getX() - sweepWidth; x <= playerPos.getX() + sweepWidth; x++) {

                Vec2i target = new Vec2i(x, y);

                //check if output is inside the map
                if (target.manhattan(playerPos) > dist) continue;
                if (!level.isValidGridPos(target)) continue;
                if (!level.isPassableForEnemy(target, null)) continue;

                targets.add(target);
            }
        }
        return targets;
    }

    /**
     * 90/deg rotation of the previous method
     * Gets the horizontally opp. quadrant to a Hunter's curr postion relative to the Avatar
     * @param level : current Level
     * @param playerPos : Avatar's position
     * @param increase : inc/dec flag
     * @param dist : manhatten distance
     * @return quadrant hori. opp. to a Hunter's curr position, relative the the Avatar
     */
    public ArrayList<Vec2i> horizontalSweep(Level level, Vec2i playerPos, Boolean increase, int dist) {
        ArrayList<Vec2i> targets = new ArrayList<>();
        int limit = increase ? level.getNCols() : 0;

        for (int x = playerPos.getX(); checkLimit(increase, x, limit); x = iterate(increase, x)) {

            int sweepWidth = Math.abs(x - playerPos.getX());
            for (int y = playerPos.getY() - sweepWidth; y <= playerPos.getY() + sweepWidth; y++) {

                Vec2i target = new Vec2i(x, y);

                //check if output is inside the map
                if (target.manhattan(playerPos) > dist) continue;
                if (!level.isValidGridPos(target)) continue;
                if (!level.isPassableForEnemy(target, null)) continue;

                targets.add(target);
            }
        }
        return targets;
    }
}