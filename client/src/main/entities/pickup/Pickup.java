package main.entities.pickup;

import main.entities.Avatar;
import main.entities.Entity;
import main.entities.enemies.Enemy;
import main.entities.prop.Prop;
import main.Level;

/**
 * Abstracts the Pickup entities on the level
 */
public abstract class Pickup extends Entity {

    protected int score = 0; //TODO: define it for each pickup

    /**
     * Basic Constructor
     * @param level : current level
     */
    public Pickup(Level level) {
        super(level);
    }

    @Override
    public void destroy() {
        level.removePickup(getGridPos());
    }

    @Override
    public boolean isPassableFor(Entity entity) {
        return false;
    }

    @Override
    public boolean isPassableForProp(Prop prop) {
        return prop.isProjectile();
    }

    @Override
    public boolean isPassableForEnemy(Enemy enemy) {
        return true;
    }

    @Override
    public boolean isPassableForAvatar(Avatar avatar) {
        return true;
    }

    @Override
    public void onEnterByAvatar(Avatar avatar) {
        if (onPickupBy(avatar))
            destroy();
    }

    /**
     * checks if item can be picked up by an avatar
     * @param avatar Avatar
     * @return true if can be picked up, false otherwise
     */
    public abstract boolean onPickupBy(Avatar avatar);

    @Override
    public boolean canStackFor(Entity entity) {
        return false;
    }

    @Override
    public boolean canStackForEnemy(Enemy enemy) {
        return true;
    }

    @Override
    public boolean canStackForAvatar(Avatar avatar) {
        return true;
    }


    /**
     * Getter for the Pickups score
     * @return the score
     */
    public int getScore() { return score; }
}
