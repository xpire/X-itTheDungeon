package main.entities.prop;

import main.entities.Avatar;
import main.entities.Entity;
import main.maploading.Level;
import main.math.Vec2i;

public abstract class Prop extends Entity {

    // TODO: abstract out Pushable interface

    protected boolean isHeavy = false;
    protected boolean isProjectile = false;

    public Prop(Level level) {
        super(level);
    }

    public Prop(Level level, Vec2i pos) {
        super(level, pos);
    }

    @Override
    public void onDestroyed() {
        level.removeProp(getGridPos());
    }

    public boolean onPush(Avatar avatar) {
        return false;
    }

    public boolean isHeavy() {
        return isHeavy;
    }

    public boolean isProjectile() { return isProjectile; }
}
