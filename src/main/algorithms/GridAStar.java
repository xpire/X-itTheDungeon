package main.algorithms;

import main.Level;
import main.math.Tuple;
import main.math.Vec2i;

import java.util.ArrayList;
import java.util.List;

public class GridAStar {

    private Level level;
    private Vec2i pos;
    private List<Vec2i> targets;

    public GridAStar(Level level, Vec2i pos, List<Vec2i> targets) {
        this.level   = level;
        this.pos     = pos;
        this.targets = targets;
    }

    public List<Vec2i> search() {
        AStar<Vec2i> star = new AStar<>();
        return star.search(pos, this::isGoal, this::getAdjs, this::manhattanDist);
    }

    private boolean isGoal(Vec2i vertex) {
        return targets.stream().anyMatch(t -> t.equals(vertex));
    }

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

    // Precondition targets non-empty
    private int manhattanDist(Vec2i vertex) {
        return targets.stream()
                .map(t -> t.manhattan(vertex))
                .min(Integer::compare).get();
    }
}
