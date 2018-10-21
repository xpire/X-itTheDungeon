package main.entities.terrain;

import main.Level;
import main.entities.Avatar;
import main.entities.Entity;

/**
 * Abstracts the Terrain entities of the Level
 */
public abstract class Terrain extends Entity {

    /**
     * Basic constructor
     * @param level : level the terrain belongs to
     */
    public Terrain(Level level) {
        super(level);
    }

    @Override
    public void destroy() {
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
