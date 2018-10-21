package main.maploading;

import main.component.ViewComponent;
import main.entities.Entity;
import main.math.Vec2i;

import java.util.Collections;
import java.util.Iterator;

/**
 * Class representing the Layer of the level containing a single entity (the Avatar)
 * @param <T> Any class which extends entity
 */
public class SingletonLayer<T extends Entity> extends EntityLayer<T> {

    private T entity;

    /**
     * Generic constructor
     */
    public SingletonLayer() {
        view = new ViewComponent();
    }

    /**
     * Getter for the entity
     * @return
     */
    public T get() {
        return entity;
    }

    /**
     * Removes the entity from the view
     * @return
     */
    public T remove() {
        if (entity != null) {
            view.removeNode(entity.getView());  // remove entity view
            entity.onRemovedFromLevel();        // notify entity
        }

        T copy = entity;
        entity = null;
        return copy;
    }

    /**
     * moves the entity
     * @param pos position to move to
     */
    public void move(Vec2i pos) {
        entity.setPos(pos);
    }


    @Override
    public T getEntity(Vec2i pos) {
        if (entity == null) return null;
        return entity.getGridPos().equals(pos) ? entity : null;
    }

    /* TODO: could violate the Liskov principle */
    @Override
    public void setEntityRaw(Vec2i pos, T entity) {
        remove();
        this.entity = entity;
    }

    @Override
    protected T removeEntityRaw(Vec2i pos) {
        if (!hasEntity(pos))
            return null;

        T copy = entity;
        entity = null;
        return copy;
    }

    @Override
    public Iterator<T> iterator() {
        if (entity == null) {
            return Collections.emptyIterator();
        }
        return Collections.singletonList(entity).iterator();
    }
}
