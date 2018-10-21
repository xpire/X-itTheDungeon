package main.entities.enemies;

<<<<<<< HEAD
import main.entities.enemies.behaviour.AIBehaviour;
import main.entities.enemies.behaviour.CowardBehaviour;
import main.entities.enemies.behaviour.StrategistBehaviour;
import main.Level;
=======
import main.Level;
import main.behaviour.AIBehaviour;
import main.behaviour.CowardBehaviour;
import main.behaviour.StrategistBehaviour;
>>>>>>> Entity package javadoc done
import main.math.Vec2d;
import main.sprite.SpriteView;

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

    @Override
    public void onCreated(){
        super.onCreated();
        sprite = new SpriteView(getImage("sprite/enemies/strategist/0.png"),new Vec2d(-6,-8), 1.875,1.875);
        view.addNode(sprite);
    }

    @Override
    public AIBehaviour decideBehaviour() {
        if (manager.isAvatarRaged())
            return new CowardBehaviour(level, pos, manager.getAvatarPos());
        else
            return new StrategistBehaviour(level, pos, manager.getAvatarPos(), manager.getPastMoves());
    }
}
