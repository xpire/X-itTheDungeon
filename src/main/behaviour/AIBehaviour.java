package main.behaviour;

import main.entities.enemies.Enemy;
import main.Level;
import main.math.Vec2i;
import java.util.ArrayList;

/**
 * Interface to decide the AI's move
 */
public interface AIBehaviour {
    /**
     * Determines the next move for each AI
     *
     * @param level Level the enemy exists in
     * @param currLocation the enemies current location
     * @param userLocation the Avatar's location
     * @param pastMoves list of the enemies past moves
     * @param entities list of all other enemies
     * @return
     */
    public ArrayList<Vec2i> decideMove(Level level,
                                       Vec2i currLocation,
                                       Vec2i userLocation,
                                       ArrayList<Integer> pastMoves,
                                       ArrayList<Enemy> entities);
}
