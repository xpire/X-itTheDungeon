package main.maploading;

import main.component.ViewComponent;
import main.entities.Entity;
import main.math.Vec2i;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Class representing the entity layers using hashmaps
 * @param <T> : The generic entity
 */
public class HashMapLayer<T extends Entity> extends EntityLayer<T> {

    private HashMap<Vec2i, T> entities;

    /**
     * Generic constructor
     */
    public HashMapLayer() {
        entities = new HashMap<>();
        view     = new ViewComponent();
    }

    @Override
    public T getEntity(Vec2i pos) {
        return entities.get(pos);
    }

    @Override
    protected void setEntityRaw(Vec2i pos, T entity) {
        entities.put(pos, entity);
    }

    @Override
    protected T removeEntityRaw(Vec2i pos) {
        return entities.remove(pos);
    }

    @Override
    public Iterator<T> iterator() {
        return new ArrayList<>(entities.values()).iterator();
    }

    /**
     * Getter for the entities in the layer
     * @return
     */
    public ArrayList<T> getEntities() {
        return new ArrayList<>(entities.values());
    }
}
