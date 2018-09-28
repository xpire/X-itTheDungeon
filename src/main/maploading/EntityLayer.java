package main.maploading;

import javafx.scene.Node;
import main.Level;
import main.component.ViewComponent;
import main.entities.Entity;
import main.math.Vec2i;

import java.util.Iterator;
import java.util.function.Function;

public abstract class EntityLayer <T extends Entity> {

    protected Level level;
    protected ViewComponent view;

    public abstract T getEntity(Vec2i pos);

    public abstract void setEntity(Vec2i pos, T entity);

    public abstract void addEntity(Vec2i pos, T entity);

    public abstract T removeEntity(Vec2i pos, boolean replaceWithDefault);

    public T removeEntity(Vec2i pos) {
        return removeEntity(pos, false);
    }


    public boolean hasEntity(Vec2i pos) {
        return getEntity(pos) != null;
    }


    public abstract Iterator<T> iterator();

    public final boolean isPassableFor(Vec2i pos, Entity other) {
        if (!hasEntity(pos)) return true;
        return other.canPassThrough(getEntity(pos));
    }

    public final boolean isStackableFor(Vec2i pos, Entity other) {
        if (!hasEntity(pos)) return true;
        return other.canStackOn(getEntity(pos));
    }

    public final <R> R applyIfPresent(Vec2i pos, Function<T, R> action, R defaultValue) {
        if (hasEntity(pos))
            return action.apply(getEntity(pos));
        return defaultValue;
    }

    public Node getView() {
        return view.getView();
    }

    protected final void moveEntityTo(Vec2i pos, T entity) {
        entity.moveTo(pos);
    }
}
