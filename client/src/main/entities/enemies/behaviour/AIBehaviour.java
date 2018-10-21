package main.entities.enemies.behaviour;

import main.Level;
import main.math.Vec2i;

import java.util.List;

/**
 * Interface to decide the AI's move
 */
public abstract class AIBehaviour {

    protected Level level;
    protected Vec2i pos;
    protected Vec2i target;

    /**
     * Determines the next move for each AI
     *
     * @param level level the AI exists in
     * @param pos the AI's current position
     * @param target the target's current position
     */
    public AIBehaviour(Level level, Vec2i pos, Vec2i target) {
        this.level  = level;
        this.pos    = pos;
        this.target = target;
    }

    public abstract List<Vec2i> decideTargetTiles();
}
