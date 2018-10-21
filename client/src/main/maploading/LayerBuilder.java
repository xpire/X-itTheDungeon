package main.maploading;

import main.Level;
import main.entities.Entity;
import main.math.Vec2i;

/**
 * Abstract class of the LayerBuilder
 * @param <T>
 */
public abstract class LayerBuilder <T extends Entity> {

    protected Level level;
    protected EntityFactory<T> factory;

    /**
     * Generic constructor
     * @param level : corresponding level
     * @param factory : corresponding factory
     */
    public LayerBuilder(Level level, EntityFactory<T> factory) {
        this.level = level;
        this.factory = factory;
    }

    /**
     * Whether or not the entity can be made
     * @param entityCode : entity in question
     * @return true if can make
     */
    public boolean canMakeEntity(char entityCode) {
        return factory.canMakeEntity(entityCode);
    }

    /**
     * Whether or not the entity can be attached
     * @param pos : position to attach
     * @param entity : entity to attach
     * @return true if can attach
     */
    public final boolean canAttach(Vec2i pos, T entity) {
        return canPlaceEntity(pos, entity);
    }

    /**
     * attach an entity to a position
     * @param pos : position to attach to
     * @param entity : entity to attach
     * @throws InvalidMapException : if the map is invalid
     */
    public final void attach(Vec2i pos, T entity) throws InvalidMapException {
        if (!canAttach(pos, entity)) throw new InvalidMapException("Entity cannot be placed at: " + pos);
        addEntity(pos, entity);
    }

    /**
     * Force attach an entity to a position, replaces everything necessary
     * @param pos : position to attach to
     * @param entity : the entity to attach
     * @param replaceWithDefault : should terrain be replaced with ground
     */
    public final void attachForcefully(Vec2i pos, T entity, boolean replaceWithDefault) {
        if (!canReplaceEntity(pos, entity))
            level.removeAllAt(pos, replaceWithDefault);
        addEntity(pos, entity);
    }

    /**
     * make an entity and attach it to a position
     * @param pos : position to attach
     * @param entityCode : entity to attach
     * @throws InvalidMapException : invalid map
     */
    public final void makeAndAttach(Vec2i pos, char entityCode) throws InvalidMapException {
        attach(pos, factory.getEntity(entityCode));
    }

    /**
     * Make an entity and forcefully attach it
     * @param pos : position to make and attach
     * @param entityCode : entity ot make and attach
     * @param replaceWithDefault : should terrain be repalced with ground
     * @throws InvalidMapException : invalid map
     */
    public final void makeAndAttachForcefully(Vec2i pos, char entityCode, boolean replaceWithDefault) throws InvalidMapException {
        attachForcefully(pos, factory.getEntity(entityCode), replaceWithDefault);
    }

    /**
     * Checks if entity can be placed
     * @param pos : position to place
     * @param entity : entity to place
     * @return true if can place
     */
    protected abstract boolean canPlaceEntity(Vec2i pos, T entity);

    /**
     * Checks if entity can replace
     * @param pos : position to replace
     * @param entity : entity to replace with
     * @return true if can replace
     */
    protected abstract boolean canReplaceEntity(Vec2i pos, T entity);

    /**
     * Adds an entity to a position
     * @param pos : position to add to
     * @param entity : entity to add
     */
    protected abstract void addEntity(Vec2i pos, T entity);
}
