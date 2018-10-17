package main.maploading;

import main.Level;
import main.entities.Entity;
import main.math.Vec2i;

// TODO may be do this for level class as well
public abstract class LayerBuilder <T extends Entity> {

    protected Level level;
    protected EntityFactory<T> factory;

    public LayerBuilder(Level level, EntityFactory<T> factory) {
        this.level = level;
        this.factory = factory;
    }

    public boolean canMakeEntity(char entityCode) {
        return factory.canMakeEntity(entityCode);
    }

    public final boolean canAttach(Vec2i pos, T entity) {
        return canPlaceEntity(pos, entity);
    }

    public final void attach(Vec2i pos, T entity) throws InvalidMapException {
        if (!canAttach(pos, entity)) throw new InvalidMapException("Entity cannot be placed at: " + pos);
        addEntity(pos, entity);
    }

    public final void attachForcefully(Vec2i pos, T entity, boolean replaceWithDefault) {
        if (!canReplaceEntity(pos, entity))
            level.removeAllAt(pos, replaceWithDefault);
        addEntity(pos, entity);
    }

    public final void makeAndAttach(Vec2i pos, char entityCode) throws InvalidMapException {
        attach(pos, factory.getEntity(entityCode));
    }

    public final void makeAndAttachForcefully(Vec2i pos, char entityCode, boolean replaceWithDefault) throws InvalidMapException {
        attachForcefully(pos, factory.getEntity(entityCode), replaceWithDefault);
    }

    protected abstract boolean canPlaceEntity(Vec2i pos, T entity);
    protected abstract boolean canReplaceEntity(Vec2i pos, T entity);
    protected abstract void addEntity(Vec2i pos, T entity);
}
