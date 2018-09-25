package main.entities.enemies;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import main.behaviour.AIBehaviour;
import main.behaviour.CowardBehaviour;
import main.behaviour.StrategistBehaviour;
import main.Level;
import main.math.Vec2i;

/**
 * The Strategist enemy entity
 * Follows the shortest path to a tile the Avatar
 * is likely to go into
 */
public class Strategist extends Enemy {

    {
        symbol      = '2';
        isHunter    = false;
    }

    /**
     * Basic constructor
     * @param level Level the enemy will exist in
     */
    public Strategist(Level level) {
        super(level);
    }

    public Strategist(Level map, Vec2i pos) {
        super(map, pos);
    }

    @Override
    public void onCreated(){
        super.onCreated();
        view.addNode(new Circle(10, Color.BLUE));
    }

    @Override
    public AIBehaviour decideBehaviour() {
        if (manager.isAvatarRaged())
            return new CowardBehaviour(level, pos, manager.getAvatarPos());
        else
            return new StrategistBehaviour(level, pos, manager.getAvatarPos(), manager.getPastMoves());
    }
}
