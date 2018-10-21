package main.entities.enemies.behaviour;

import main.Level;
import main.algorithms.PageRank;
import main.math.Vec2i;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Implements the behaviour specific to the Strategist
 */
public class StrategistBehaviour extends AIBehaviour {

    private ArrayList<Integer> pastMoves;

    /**
     * Generic Constructor
     * @param level : level which the strategist belongs to
     * @param pos : position of the strategist
     * @param target : position of its target
     * @param pastMoves : list of the strategists past moves
     */
    public StrategistBehaviour(Level level, Vec2i pos, Vec2i target, ArrayList<Integer> pastMoves) {
        super(level, pos, target);
        this.pastMoves = pastMoves;
    }

    @Override
    public List<Vec2i> decideTargetTiles(){

        // possible target tiles
        List<Vec2i> tiles = new ArrayList<>();
        tiles.add(target);

        List<Vec2i> adjs = getAdjs(target);

        // go to target if no adjacent tiles accessible, or if adjacent to the AI
        if (adjs.isEmpty() || pos.isAdjacent(target))
            return tiles;

        // find the tile with highest incentive for the avatar (if any pickups nearby)
        if (hasPickup(adjs)) {
            tiles.add(getHighestIncentiveTile(adjs));
            return tiles;
        }

        // select the adjacent tile that the avatar is most likely to visit next
        if (adjs.size() == 1) {
            tiles.addAll(adjs);
        }
        else {
            PageRank pr = new PageRank(pos, adjs, pastMoves);
            tiles.add(pr.getResult());
        }
        return tiles;
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

    /**
     * Getter for the highest value tile
     * @param tiles list of tiles to choose from
     * @return the best tile to travel to
     */
    private Vec2i getHighestIncentiveTile(List<Vec2i> tiles) {
        return tiles.stream()
                .max(Comparator.comparing(this::getScore))
                .orElse(target);
    }

    /**
     * Checks if a tile within a region has a pickup entity on it
     * @param tiles : region of tiles to check
     * @return true if any tile in the region has a pickup
     */
    private boolean hasPickup(List<Vec2i> tiles) {
        return tiles.stream().anyMatch(level::hasPickup);
    }

    /**
     * Gets the score of a tile
     * @param pos : the tile to check
     * @return : integer value
     */
    private int getScore(Vec2i pos) {
        if (level.hasPickup(pos))
            return level.getPickup(pos).getScore();
        else
            return 0;
    }
}
