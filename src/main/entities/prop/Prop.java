package main.entities.prop;

import main.entities.Avatar;
import main.entities.Entity;
import main.entities.enemies.Enemy;
import main.entities.pickup.Pickup;
import main.entities.terrain.Terrain;
import main.maploading.Level;
import main.math.Vec2i;

public abstract class Prop extends Entity {


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

    @Override
    public boolean isPassableForProp(Prop prop) {
        return false;
    }

    public boolean onPush(Avatar avatar) {
        return false;
    }

    public boolean isHeavy() {
        return isHeavy;
    }

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
        return canStackFor(enemy);
    }

    @Override
    public boolean canStackForAvatar(Avatar avatar) {
        return canStackFor(avatar);
    }

    public boolean isProjectile() { return isProjectile; }
}
