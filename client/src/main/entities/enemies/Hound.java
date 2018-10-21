package main.entities.enemies;

import main.Level;
import main.entities.enemies.behaviour.AIBehaviour;
import main.entities.enemies.behaviour.CowardBehaviour;
import main.entities.enemies.behaviour.HoundBehaviour;
import main.entities.enemies.behaviour.HunterBehaviour;
import main.math.Vec2d;
import main.sprite.SpriteView;

/**
 * The Hound enemy entity
 * Tries to trap the Avatar between it and the Hunter nearest to
 * the Avatar. Becomes a Hunter if no Hunters exist.
 */
public class Hound extends Enemy { //TODO remove test codes

    {
        symbol = '3';
        isHunter = false;
    }

    /**
     * Basic constructor
     * @param level Level the enemy will exist in
     */
    public Hound(Level level) {
        super(level);
    }
    
    @Override
    public void onCreated(){
        super.onCreated();
        sprite = new SpriteView(getImage("sprite/enemies/hound/0.png"),new Vec2d(-6,-8), 1.875,1.875);
        view.addNode(sprite);
    }

    @Override
    public AIBehaviour decideBehaviour() {
        if (manager.isAvatarRaged())
            return new CowardBehaviour(level, pos, manager.getAvatarPos());

        if (manager.checkHunterExists())
            return new HoundBehaviour(level, pos, manager.getAvatarPos());
        else
            return new HunterBehaviour(level, pos, manager.getAvatarPos());
    }
}
