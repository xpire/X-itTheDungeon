package main.entities.enemies;

import main.Level;
import main.entities.enemies.behaviour.AIBehaviour;
import main.entities.enemies.behaviour.CowardBehaviour;
import main.entities.enemies.behaviour.HunterBehaviour;
import main.math.Vec2d;
import main.sprite.SpriteView;

/**
 * The Hunter enemy entity
 * Always follows the shortest path towards the Avatar
 */
public class Hunter extends Enemy {

    {
        symbol = '1';
        isHunter = true;
    }

    /**
     * Basic constructor
     * @param level Level the enemy will exist in
     */
    public Hunter(Level level) {
        super(level);
    }


    @Override
    public void onCreated(){
        super.onCreated();
        sprite = new SpriteView(getImage("sprite/enemies/hunter/0.png"),new Vec2d(-6,-8), 1.875,1.875);
        view.addNode(sprite);

    }


    @Override
    public AIBehaviour decideBehaviour() {
        if (manager.isAvatarRaged())
            return new CowardBehaviour(level, pos, manager.getAvatarPos());
        else
            return new HunterBehaviour(level, pos, manager.getAvatarPos());
    }
}