package main.algorithms;

import main.Level;
import main.math.Tuple;
import main.math.Vec2i;

import java.util.ArrayList;
import java.util.List;

/**
 * AStar implementation on a 2D grid
 */
public class GridAStar {

    private Level level;
    private Vec2i pos;
    private List<Vec2i> targets;

    /**
     * Basic constructor to begin an AStar search
     * @param level : the level the grid belongs to
     * @param pos : current position of the entity calling AStar
     * @param targets : targets of the search
     */
    public GridAStar(Level level, Vec2i pos, List<Vec2i> targets) {
        this.level   = level;
        this.pos     = pos;
        this.targets = targets;
    }

    /**
     * Finds the path to the target
     * @return The list of positions which reach the target
     */
    public List<Vec2i> search() {
        AStar<Vec2i> star = new AStar<>();
        return star.search(pos, this::isGoal, this::getAdjs, this::manhattanDist);
    }

    /**
     * Checks if a tile is a goal of the search
     * @param vertex tile to check
     * @return true if the tile is a goal, false otherwise
     */
    private boolean isGoal(Vec2i vertex) {
        return targets.stream().anyMatch(t -> t.equals(vertex));
    }

    /**
     * Gets a list of adjacent tiles to a specific tile
     * @param vertex : tile in question
     * @return : the list of adjacent tiles
     */
    private ArrayList<Tuple<Vec2i, Integer>> getAdjs(Vec2i vertex) {

        ArrayList<Tuple<Vec2i, Integer>> res = new ArrayList<>();

        for (Vec2i dir : Vec2i.DIRECTIONS) {
            Vec2i adj = vertex.add(dir);

            if (!level.isValidGridPos(adj)) continue;
            if (!level.isPassableForEnemy(adj, null)) continue;

            res.add(new Tuple<>(adj, 1));
        }

        return res;
    }

    /**
     * Calculates the Manhatten distance to another vertex
     * @pre targets is not empty
     * @param vertex the target vertex
     * @return the Manhatten distance
     */
    private int manhattanDist(Vec2i vertex) {
        return targets.stream()
                .mapToInt(t -> t.manhattan(vertex))
                .sum();
    }
}
