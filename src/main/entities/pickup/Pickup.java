package main.entities.pickup;

import main.entities.Avatar;
import main.entities.Entity;
import main.entities.enemies.Enemy;
import main.entities.prop.Prop;
import main.entities.terrain.Terrain;
import main.maploading.Level;
import main.math.Vec2i;

public abstract class Pickup extends Entity {

    protected int score;

    public Pickup(Level level) {
        super(level);
    }

    public int getScore() { return this.score; }

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
    public void onEnterByAvatar(Avatar avatar) {
        if (onPickupBy(avatar)) {
            onDestroyed();
        }
    }

    public abstract boolean onPickupBy(Avatar avatar);

    @Override
    public boolean canStackFor(Entity entity) {
        return false;
    }

    @Override
    public boolean canStackForTerrain(Terrain terrain) {
        return canStackFor(terrain);
    }

    @Override
    public boolean canStackForProp(Prop prop) {
        return canStackFor(prop);
    }

    @Override
    public boolean canStackForPickup(Pickup pickup) {
        return canStackFor(pickup);
    }

    @Override
    public boolean canStackForEnemy(Enemy enemy) {
        return true;
    }

    @Override
    public boolean canStackForAvatar(Avatar avatar) {
        return true;
    }
}
