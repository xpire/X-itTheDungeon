package main.behaviour;

import main.Level;
import main.entities.enemies.Enemy;
import main.math.Vec2i;

import java.util.ArrayList;

/**
 * Implements the behaviour specific to the Coward enemy
 */
public class CowardBehaviour2 implements AIBehaviour {
    @Override
    public ArrayList<Vec2i> decideMove(
            Level level,
            Vec2i currPos,
            Vec2i playerPos,
            ArrayList<Integer> pastMoves,
            ArrayList<Enemy> enemies) {

        ArrayList<Vec2i> targets = new ArrayList<>();

        int maxDist = currPos.manhattan(playerPos);
        for (Vec2i dir : Vec2i.DIRECTIONS) {

            Vec2i v = currPos.add(dir);

            if (!level.isValidGridPos(v)) continue;

            int dist = v.manhattan(currPos);

            if (dist > maxDist) {
                maxDist = dist;
                targets.clear();
                targets.add(v);
            }
            else if (dist == maxDist) {
                targets.add(v);
            }
        }
        return targets;
    }
}