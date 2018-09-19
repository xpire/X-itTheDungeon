package main.algorithms;

import main.Level;
import main.math.Tuple;
import main.math.Vec2i;

import java.util.ArrayList;
import java.util.List;

public class AStarUser {

    private Vec2i pos;
    private Level level;
    private ArrayList<Vec2i> targets;

    public AStarUser(Vec2i pos, Level level, ArrayList<Vec2i> targets) {
        this.pos = pos;
        this.level = level;
        this.targets = targets;
    }

    public void search() {
        AStar star = new AStar();
        List<Vec2i> path = star.search(pos, this::isGoal, this::getAdjs, this::manhattanDist);
    }

    public boolean isGoal(Vec2i vertex) {
        return targets.stream().anyMatch(t -> t.equals(vertex));
    }

    public ArrayList<Tuple<Vec2i, Integer>> getAdjs(Vec2i vertex) {

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
    public int manhattanDist(Vec2i vertex) {
        return targets.stream()
                .map(t -> t.manhattan(vertex))
                .min(Integer::compare).get();
    }
}
