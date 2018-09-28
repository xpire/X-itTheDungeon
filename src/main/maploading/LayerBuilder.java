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

//    public final boolean canAttach(Vec2i pos, T entity) {
//        return canPlaceEntity(pos, entity);
//    }

    public final boolean attach(Vec2i pos, T entity) {
        if (canPlaceEntity(pos, entity)) {
            getLayer().addEntity(pos, entity);
            return true;
        }

        return false;
    }

    public final void attachForcefully(Vec2i pos, T entity, boolean replaceWithDefault) {
        if (!canReplaceEntity(pos, entity))
            level.removeAllAt(pos, replaceWithDefault);

        getLayer().addEntity(pos, entity);
    }

    public final void makeAndAttach(Vec2i pos, char entityCode) throws InvalidMapException {
        if (!attach(pos, factory.getEntity(entityCode)))
            throw new InvalidMapException("Entity cannot be placed at: " + pos);
    }
//
//    public final void makeAndAttachForcefully(Vec2i pos, char entityCode, boolean replaceWithDefault) throws InvalidMapException {
//        attachForcefully(pos, factory.getEntity(entityCode), replaceWithDefault);
//
//    }
//
////    public abstract void makeAndAttach(Vec2i pos, char entityCode) throws InvalidMapException;
//    public abstract void makeAndAttachForcefully(Vec2i pos, char entityCode) throws InvalidMapException;


    protected abstract boolean canPlaceEntity(Vec2i pos, T entity);
    protected abstract boolean canReplaceEntity(Vec2i pos, T entity);
    protected abstract EntityLayer<T> getLayer();
}
