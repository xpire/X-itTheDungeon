package main.entities.terrain;

import main.Level;
import main.entities.Avatar;
import main.entities.Entity;
import main.math.Vec2i;

/**
 * Abstracts the Terrain entities of the Level
 */
public abstract class Terrain extends Entity {

    /**
     * Basic constructor
     * @param level
     */
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

    /**
     * check if the Terrain can be pushed by an Avatar
     * @param avatar : the Avatar attempting to push
     * @return true if can be pushed, false otherwise
     */
    public boolean onPush(Avatar avatar) {
        return isPassableForAvatar(avatar);
    }
}
