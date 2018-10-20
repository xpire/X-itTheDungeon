package main.entities.prop;

import main.entities.Entity;
import main.Level;
import main.math.Vec2d;
import main.math.Vec2i;
import main.sprite.SpriteAnimation;
import main.sprite.SpriteView;

/**
 * Class describing the Flying Arrow entity
 */
public class FlyingArrow extends Prop{

    Vec2i direction;

    {
        isProjectile = true;
//        Vec2i direction;
    }

    /**
     * Basic constructor
     * @param level
     */
    public FlyingArrow(Level level, Vec2i dir) {
        super(level);
        direction = dir;
    }

    public FlyingArrow(Level level, Vec2i pos, Vec2i dir) {
        super(level, pos);
        direction = dir;
    }

    @Override
    public void onCreated() {
        sprite = new SpriteView(getImage("sprite/prop/flyingarrow/0.png"), new Vec2d(-8,-3), 1, 1);
        sprite.addState("Left",getImage("sprite/prop/flyingarrow/0.png"));
        sprite.addState("Right",getImage("sprite/prop/flyingarrow/0.png"));
        sprite.addState("Up",getImage("sprite/prop/flyingarrow/3.png"));
        sprite.addState("Down",getImage("sprite/prop/flyingarrow/8.png"));

    }

    @Override
    public boolean isPassableFor(Entity entity) {
        return false;
    }

    @Override
    public boolean canStackFor(Entity entity) {
        return false;
    }
}
