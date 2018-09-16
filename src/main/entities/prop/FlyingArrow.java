package main.entities.prop;

import main.entities.Entity;
import main.maploading.Level;
import main.math.Vec2i;

public class FlyingArrow extends Prop{

    {
        isProjectile = true;
    }

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
