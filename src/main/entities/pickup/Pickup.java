package main.entities.pickup;

import main.entities.Avatar;
import main.entities.Entity;
import main.entities.enemies.Enemy;
import main.entities.prop.Prop;
import main.maploading.Level;
import main.math.Vec2i;

public abstract class Pickup extends Entity {

    protected int score = 0; //TODO: define it for each pickup

    public Pickup(Level level) {
        super(level);
    }

    public Pickup(Level level, Vec2i pos) {
        super(level, pos);
    }

    @Override
    public void onDestroyed() {
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

    @Override
    public void onEnterByAvatar(Avatar avatar) {
        if (onPickupBy(avatar))
            onDestroyed();
    }

    public abstract boolean onPickupBy(Avatar avatar);

    public int getScore() { return score; }
}
