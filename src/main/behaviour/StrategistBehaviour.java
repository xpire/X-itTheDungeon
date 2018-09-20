package main.behaviour;

// Going around boulder and avoid arrows

import main.algorithms.PageRank;
import main.entities.enemies.Enemy;

import main.Level;
import main.math.Vec2i;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static java.util.Collections.singletonList;

/**
 * Implements the behaviour specific to the Strategist
 */
public class StrategistBehaviour implements AIBehaviour {

    private Level level;
    private Vec2i currPos;
    private Vec2i avatarPos;
    private ArrayList<Integer> pastMoves;
    private ArrayList<Enemy> enemies;

    public StrategistBehaviour(Level level, Vec2i currPos, Vec2i avatarPos,
                                ArrayList<Integer> pastMoves, ArrayList<Enemy> enemies) {
        this.level = level;
        this.currPos = currPos;
        this.avatarPos = avatarPos;
        this.pastMoves = pastMoves;
        this.enemies = enemies;
    }


    @Override
    public List<Vec2i> decideMove(Level level,
                                  Vec2i currPos,
                                  Vec2i playerPos,
                                  ArrayList<Integer> pastMoves,
                                  ArrayList<Enemy> enemies) {


        // Possible coordinates
        List<Vec2i> adjs = getAdjs(avatarPos);

        // No adjacent square accessible
        if (adjs.isEmpty())
            return singletonList(avatarPos);

        // Avatar adjacent to the enemy
        if (currPos.isAdjacent(avatarPos))
            return singletonList(avatarPos);

        // Find the target with highest incentive (if any pickups nearby)
        if (hasPickup(adjs))
            return singletonList(getHighestIncentiveTile(adjs));

        // Select the adjacent square that the avatar is
        // most likely to visit next
        if (adjs.size() == 1)
            return adjs;

        // Heuristic is to pick the lowest rank
        PageRank pr = new PageRank(pastMoves, adjs, currPos);
        return singletonList(pr.getResult());
    }



    /**
     * Get what coordinate is possible, note that it only checks coordinates with no entities
     * @param pos Current location of the strategist TODO was called playerPos?
     * @return list of possible coordinates
     */
    private List<Vec2i> getAdjs(Vec2i pos) {
        ArrayList<Vec2i> res = new ArrayList<>();

        for (Vec2i dir : Vec2i.DIRECTIONS) {

            Vec2i adj = pos.add(dir);
            if (!level.isValidGridPos(adj)) continue;
            if (!level.isPassableForEnemy(adj, null)) continue; //TODO apply for all other behaviours

            res.add(adj);
        }
        return res;
    }



    private Vec2i getHighestIncentiveTile(List<Vec2i> tiles) {
        return tiles.stream()
                .max(Comparator.comparing(adj -> getScore(adj)))
                .orElse(avatarPos);
    }

    private boolean hasPickup(List<Vec2i> tiles) {
        return tiles.stream().anyMatch(level::hasPickup);
    }

    private int getScore(Vec2i pos) {
        if (level.hasPickup(pos))
            return level.getPickup(pos).getScore();
        else
            return 0;
    }
}
