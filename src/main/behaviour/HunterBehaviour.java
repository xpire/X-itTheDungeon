package main.behaviour;

import main.entities.enemies.Enemy;
import main.math.Vec2i;
import main.entities.*;
import main.maploading.Level;
import java.util.ArrayList;

/**
 * Implements the behaviour specific to the Hunter
 */
public class HunterBehaviour implements AIBehaviour {
    @Override
    public ArrayList<Vec2i> decideMove(
            Level level,
            Vec2i currLocation,
            Vec2i userLocation,
            ArrayList<Integer> pastMoves,
            ArrayList<Enemy> entities) {
        ArrayList<Vec2i> result = new ArrayList<>();
        result.add(userLocation);
        return result;
    }
}
