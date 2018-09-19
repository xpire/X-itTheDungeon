package main.behaviour;

import main.entities.enemies.Enemy;
import main.math.Vec2i;
import main.Level;
import java.util.ArrayList;

/**
 * Implements the behaviour specific to the Hunter
 */
public class HunterBehaviour implements AIBehaviour {
    @Override
    public ArrayList<Vec2i> decideMove(
            Level level,
            Vec2i currPos,
            Vec2i playerPos,
            ArrayList<Integer> pastMoves,
            ArrayList<Enemy> enemies) {

        ArrayList<Vec2i> targets = new ArrayList<>();
        targets.add(playerPos);
        return targets;
    }
}
