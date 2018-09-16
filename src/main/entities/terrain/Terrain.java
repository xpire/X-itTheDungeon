package main.entities.terrain;

import main.entities.Avatar;
import main.entities.Entity;
import main.maploading.Level;
import main.math.Vec2i;

public abstract class Terrain extends Entity {

    public Terrain(Level level) {
        super(level);
    }

    public Terrain(Level level, Vec2i pos) {
        super(level, pos);
    }

    @Override
    public void onDestroyed() {
        level.removeTerrain(getGridPos(), true);
    }

    @Override
    public boolean canStackForTerrain(Terrain terrain) {
        return false;
    }

    public boolean onPush(Avatar avatar) {
        return false;
    }
}
