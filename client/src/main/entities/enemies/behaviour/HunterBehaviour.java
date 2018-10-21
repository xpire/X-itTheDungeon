package main.entities.enemies.behaviour;

import main.Level;
import main.math.Vec2i;

import java.util.List;

import static java.util.Collections.singletonList;

/**
 * Implements the behaviour specific to the Hunter
 */
public class HunterBehaviour extends AIBehaviour {

    public HunterBehaviour(Level level, Vec2i pos, Vec2i target) {
        super(level, pos, target);
    }

    @Override
    public List<Vec2i> decideTargetTiles() {
        return singletonList(target);
    }
}
