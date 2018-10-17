package main.entities.prop;

import main.entities.Entity;
import main.Level;
import main.math.Vec2i;

/**
 * Class describing the Flying Arrow entity
 */
public class FlyingArrow extends Prop{

    {
        isProjectile = true;
    }

    /**
     * Basic constructor
     * @param level
     */
    public FlyingArrow(Level level) {
        super(level);
    }

    public FlyingArrow(Level level, Vec2i pos) {
        super(level, pos);
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
