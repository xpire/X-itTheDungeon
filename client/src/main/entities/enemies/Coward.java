package main.entities.enemies;

import main.Level;
import main.entities.enemies.behaviour.AIBehaviour;
import main.entities.enemies.behaviour.CowardBehaviour;
import main.entities.enemies.behaviour.HunterBehaviour;
import main.math.Vec2d;
import main.sprite.SpriteView;

/**
 * The Coward enemy entity
 * Follows Hunter behaviour while far away from the Avatar,
 * but runs away when it gets close
 */
public class Coward extends Enemy {

    {
        symbol = '4';
        isHunter = false;
    }

    // distance from the avatar at which the coward starts to run away
    private final int SCARE_DIST = 4;

    /**
     * Basic constructor
     * @param level Level the enemy will exist in
     */
    public Coward(Level level) {
        super(level);
    }

    @Override
    public void onCreated(){
        super.onCreated();
        sprite = new SpriteView(getImage("sprite/enemies/coward/0.png"),new Vec2d(-6,-8), 1.875,1.875);
        view.addNode(sprite);
    }


    @Override
    public AIBehaviour decideBehaviour() {
        if (manager.isAvatarRaged() || pos.manhattan(manager.getAvatarPos()) < SCARE_DIST)
            return new CowardBehaviour(level, pos, manager.getAvatarPos());
        else
            return new HunterBehaviour(level, pos, manager.getAvatarPos());
    }
}
