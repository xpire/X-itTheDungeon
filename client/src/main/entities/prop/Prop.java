package main.entities.prop;

import main.entities.Avatar;
import main.entities.Entity;
import main.Level;
import main.math.Vec2i;

/**
 * Abstracts the Prop entity on the Level
 */
public abstract class Prop extends Entity {

    // TODO: abstract out Pushable interface

    protected boolean isHeavy = false;
    protected boolean isProjectile = false;

    /**
     * Basic constructor
     * @param level
     */
    public Prop(Level level) {
        super(level);
    }


    @Override
    public void onDestroyed() {
        level.removeProp(getGridPos());
    }

    /**
     * check if the Prop can be pushed by an Avatar
     * @param avatar : Avatar attempting to push
     * @return true if can push, false otherwise
     */

    public boolean onPush(Avatar avatar) {
        return isPassableForAvatar(avatar);
    }

    /**
     * check if prop is heavy enough to activate a switch
     * @return true if the entity is heavy enough
     */
    public boolean isHeavy() {
        return isHeavy;
    }

    /**
     * check if the Prop is a projectile
     * @return true if the Prop is a projectile
     */
    public boolean isProjectile() { return isProjectile; }
}
