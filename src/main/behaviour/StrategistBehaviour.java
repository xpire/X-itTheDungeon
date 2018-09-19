package main.behaviour;

// Going around boulder and avoid arrows
import main.algorithms.PageRank;
import main.entities.enemies.Enemy;

import main.Level;
import main.math.Vec2i;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Implements the behaviour specific to the Strategist
 */
public class StrategistBehaviour implements AIBehaviour {


    @Override
    public ArrayList<Vec2i> decideMove(Level level,
                                       Vec2i currPos,
                                       Vec2i playerPos,
                                       ArrayList<Integer> pastMoves,
                                       ArrayList<Enemy> enemies) {


        // Possible coordinates
        ArrayList<Vec2i> adjs = getAdjs(level, playerPos);
        ArrayList<Vec2i> targets = new ArrayList<>();

        // No adjacent square accessible
        if (adjs.isEmpty()) {
            targets.add(playerPos);
            return targets;
        }

        // Avatar adjacent to the enemy
        if (playerPos.sub(currPos).norm1() <= 1) {
            targets.add(playerPos);
            return targets;
        }

        // Find the target with highest incentive (if any pickups nearby)
        if (hasItem(adjs, level)) {

            Vec2i maxPos = adjs.stream()
                    .max(Comparator.comparing(adj -> getScore(adj, level)))
                    .orElse(playerPos);

            targets.add(maxPos);
            return targets;
        }


        // Select the adjacent square that the avatar is
        // most likely to visit next
        if (adjs.size() == 1)
            return adjs;

        // Heuristic is to pick the lowest rank
        PageRank pr = new PageRank(pastMoves, targets, currPos);
        targets.add(pr.getResult());
        return targets;
    }



    /**
     * Get what coordinate is possible, note that it only checks coordinates with no entities
     * @param level Current state of level
     * @param pos Current location of the strategist TODO was called playerPos?
     * @return list of possible coordinates
     */
    private ArrayList<Vec2i> getAdjs(Level level, Vec2i pos) {
        ArrayList<Vec2i> res = new ArrayList<>();

        for (Vec2i dir : Vec2i.DIRECTIONS) {

            Vec2i adj = pos.add(dir);
            if (!level.isValidGridPos(adj)) continue;
            if (!level.isPassableForEnemy(adj, null)) continue;

            res.add(adj);
        }
        return res;
    }

    private boolean hasItem(ArrayList<Vec2i> coords, Level level) {
        return coords.stream().anyMatch(level::hasPickup);
    }


    private int getScore(Vec2i pos, Level level) {
        if (level.hasPickup(pos))
            return level.getPickup(pos).getScore();
        else
            return 0;
    }
}
