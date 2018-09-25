package main.behaviour;

import main.Level;
import main.math.Vec2i;

import java.util.ArrayList;

/**
 * Implements the behaviour specific to the Coward enemy
 */
public class CowardBehaviour extends AIBehaviour {

    public CowardBehaviour(Level level, Vec2i pos, Vec2i target) {
        super(level, pos, target);
    }

    @Override
    public ArrayList<Vec2i> decideTargetTiles() {

        ArrayList<Vec2i> tiles = new ArrayList<>();

        // distance from target
        int maxDist = pos.manhattan(target);

        for (Vec2i dir : Vec2i.DIRECTIONS) {

            // tile adjacent to the AI
            Vec2i adj = pos.add(dir);

            // distance away from the target
            if (!level.isValidGridPos(adj)) continue;
            int dist = adj.manhattan(target);

            // find tiles farthest from the target
            if (dist > maxDist) {
                maxDist = dist;
                tiles.clear();
                tiles.add(adj);
            }
            else if (dist == maxDist) {
                tiles.add(adj);
            }
        }
        return tiles;
    }
}