package main.maploading;

import javafx.scene.Node;
import main.Level;
import main.component.ViewComponent;
import main.entities.Entity;
import main.math.Vec2i;

import java.util.Iterator;
import java.util.function.BiConsumer;

public abstract class EntityLayer <T extends Entity> {

    protected ViewComponent view;
    protected BiConsumer<Vec2i, T> onEntityEnter = (pos, entity) -> {};
    protected BiConsumer<Vec2i, T> onEntityLeave = (pos, entity) -> {};

    public abstract T getEntity(Vec2i pos);

    public void setEntity(Vec2i pos, T entity) {
        pos = new Vec2i(pos);
        removeEntity(pos);
        setEntityRaw(pos, entity);
        onEntityEnter.accept(pos, entity);
        entity.moveTo(pos);
    }

    protected abstract void setEntityRaw(Vec2i pos, T entity);

    public void addEntity(Vec2i pos, T entity) {
        setEntity(pos, entity);
        view.addNode(entity.getView());
    }

    public T removeEntity(Vec2i pos) {
        T entity = removeEntityRaw(pos);

        if (entity == null) return null;
        onEntityLeave.accept(pos, entity);
        view.removeNode(entity.getView());  // remove entity view
        entity.onRemovedFromLevel();        // notify entity TODO used when moving entities...

        return entity;
    }

    public boolean moveEntity(Vec2i oldPos, Vec2i newPos) {
        T entity = removeEntityRaw(oldPos);

        if (entity == null) return false;
        setEntity(newPos, entity);
        onEntityLeave.accept(oldPos, entity);
        return true;
    }

    public boolean moveEntity(Vec2i newPos, T entity) {
        return moveEntity(entity.getGridPos(), newPos);
    }

    protected abstract T removeEntityRaw(Vec2i pos);

    public boolean hasEntity(Vec2i pos) {
        return getEntity(pos) != null;
    }

    public abstract Iterator<T> iterator();

    public void resize(int newNRows, int newNCols) {
        Vec2i min = new Vec2i(0, 0);
        Vec2i max = new Vec2i(newNCols - 1, newNRows - 1);

        iterator().forEachRemaining(e -> {
            if (!e.getGridPos().within(min, max))
                removeEntity(e.getGridPos());
        });
    }


    public Node getView() {
        return view.getView();
    }


    public void setOnEntityEnter(BiConsumer<Vec2i, T> onEntityEnter) {
        this.onEntityEnter = onEntityEnter;
    }

    public void setOnEntityLeave(BiConsumer<Vec2i, T> onEntityLeave) {
        this.onEntityLeave = onEntityLeave;
    }
}
