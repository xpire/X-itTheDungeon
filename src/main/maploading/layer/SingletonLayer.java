package main.maploading.layer;

import main.component.ViewComponent;
import main.entities.Entity;
import main.math.Vec2i;

import java.util.Collections;
import java.util.Iterator;

public class SingletonLayer<T extends Entity> extends EntityLayer<T> {

    private T entity;

    public SingletonLayer() {
        view = new ViewComponent();
    }

    public T get() {
        return entity;
    }

    public T remove() {
        if (entity != null) {
            view.removeNode(entity.getView());  // remove entity view
            entity.onRemovedFromLevel();        // notify entity
        }
        return entity;
    }

    public void move(Vec2i pos) {
        entity.moveTo(pos);
    }


    @Override
    public T getEntity(Vec2i pos) {
        if (entity == null) return null;
        return entity.getGridPos().equals(pos) ? entity : null;
    }

    /* TODO: could violate the Liskov principle */
    @Override
    public void setEntityRaw(Vec2i pos, T entity) {
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
