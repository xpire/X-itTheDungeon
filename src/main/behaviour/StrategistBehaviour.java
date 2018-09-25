package main.behaviour;

// Going around boulder and avoid arrows

import main.Level;
import main.algorithms.PageRank;
import main.math.Vec2i;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.util.Collections.singletonList;

/**
 * Implements the behaviour specific to the Strategist
 */
public class StrategistBehaviour extends AIBehaviour {

    private ArrayList<Integer> pastMoves;

    public StrategistBehaviour(Level level, Vec2i pos, Vec2i target, ArrayList<Integer> pastMoves) {
        super(level, pos, target);
        this.pastMoves = pastMoves;
    }

    @Override
    public List<Vec2i> decideTargetTiles(){

        // possible target tiles
        List<Vec2i> adjs = getAdjs(target);

        // go to target if no adjacent tiles accessible
        if (adjs.isEmpty())
            return singletonList(target);

        // go to target if adjacent to the AI
        if (pos.isAdjacent(target))
            return singletonList(target);

        // find the tile with highest incentive for the avatar (if any pickups nearby)
        if (hasPickup(adjs))
            return singletonList(getHighestIncentiveTile(adjs));

        // select the adjacent tile that the avatar is most likely to visit next
        if (adjs.size() == 1)
            return adjs;

        PageRank pr = new PageRank(pastMoves, adjs, pos);
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
                .max(Comparator.comparing(this::getScore))
                .orElse(target);
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
