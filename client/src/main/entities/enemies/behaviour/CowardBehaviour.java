package main.entities.enemies.behaviour;

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

        int maxDist = pos.manhattan(target);

        for (Vec2i dir : Vec2i.DIRECTIONS) {

            Vec2i adj = pos.add(dir);

            if (!level.isValidGridPos(adj)) continue;
            if (!level.isPassableForEnemy(adj, null)) continue;

            int dist = adj.manhattan(target);

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