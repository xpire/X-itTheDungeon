package main.maploading;

import javafx.scene.Node;
import main.component.ViewComponent;
import main.entities.Entity;
import main.math.Vec2i;

import java.util.Iterator;
import java.util.function.BiConsumer;

/**
 * Represents the entityLayers in the level
 * @param <T>
 */
public abstract class EntityLayer <T extends Entity> {

    protected ViewComponent view;
    protected BiConsumer<Vec2i, T> onEntityEnter = (pos, entity) -> {};
    protected BiConsumer<Vec2i, T> onEntityLeave = (pos, entity) -> {};

    public abstract T getEntity(Vec2i pos);

    /**
     * Generic constructor
     * @param pos : position of entity
     * @param entity : entity in question
     */
    public void setEntity(Vec2i pos, T entity) {
        pos = new Vec2i(pos);
        removeEntity(pos);
        setEntityRaw(pos, entity);
        onEntityEnter.accept(pos, entity);
        entity.setPos(pos);
    }

    /**
     * adds entity by directly accessing the data structure
     * @param pos : position of the entity
     * @param entity : the entity
     */
    protected abstract void setEntityRaw(Vec2i pos, T entity);

    /**
     * adds an entity to a certain position
     * @param pos : position
     * @param entity : entity to be added
     */
    public void addEntity(Vec2i pos, T entity) {
        setEntity(pos, entity);
        view.addNode(entity.getView());
    }

    /**
     * Removes an entity from a position
     * @param pos : position of entity
     * @return the removed entity
     */
    public T removeEntity(Vec2i pos) {
        T entity = removeEntityRaw(pos);

        if (entity == null) return null;
        onEntityLeave.accept(pos, entity);
        view.removeNode(entity.getView());  // remove entity view
        entity.onRemovedFromLevel();        // notify entity TODO used when moving entities...

        return entity;
    }

    /**
     * Moves an entity to a given position
     * @param oldPos : original postion
     * @param newPos : new position
     * @return true if successful
     */
    public boolean moveEntity(Vec2i oldPos, Vec2i newPos) {
        T entity = removeEntityRaw(oldPos);

        if (entity == null) return false;
        setEntity(newPos, entity);
        onEntityLeave.accept(oldPos, entity);
        return true;
    }

    /**
     * Overloaded method
     * @param newPos : new position
     * @param entity : entity to be moved
     * @return true if move successful
     */
    public boolean moveEntity(Vec2i newPos, T entity) {
        return moveEntity(entity.getGridPos(), newPos);
    }

    /**
     * Removes an entity by directly accessing the data structure
     * @param pos : position of the entity
     * @return the removed entity
     */
    protected abstract T removeEntityRaw(Vec2i pos);

    /**
     * Checks if there is an entity at a given position
     * @param pos : position to check
     * @return true if entity exists
     */
    public boolean hasEntity(Vec2i pos) {
        return getEntity(pos) != null;
    }

    /**
     * returns and iterator for the layer
     * @return the iterator
     */
    public abstract Iterator<T> iterator();

    /**
     * resize the layer
     * @param newNRows : new number of rows
     * @param newNCols : new number of cols
     */
    public void resize(int newNRows, int newNCols) {
        Vec2i min = new Vec2i(0, 0);
        Vec2i max = new Vec2i(newNCols - 1, newNRows - 1);

        iterator().forEachRemaining(e -> {
            if (!e.getGridPos().within(min, max))
                removeEntity(e.getGridPos());
        });
    }

    /**
     * getter for the view of the layer
     * @return : the view
     */
    public Node getView() {
        return view.getView();
    }

    /**
     * sets the actions when an entity enters a tile
     * @param onEntityEnter : the entity entering
     */
    public void setOnEntityEnter(BiConsumer<Vec2i, T> onEntityEnter) {
        this.onEntityEnter = onEntityEnter;
    }

    /**
     * sets the actions when an entity leaves a tile
     * @param onEntityLeave : the entity leaving
     */
    public void setOnEntityLeave(BiConsumer<Vec2i, T> onEntityLeave) {
        this.onEntityLeave = onEntityLeave;
    }

    /**
     * rescales the layer
     */
    public void rescale() {
        iterator().forEachRemaining(Entity::onLevelRescaled);
    }
}
