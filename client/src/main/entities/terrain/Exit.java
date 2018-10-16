package main.entities.terrain;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import main.entities.Avatar;
import main.entities.Entity;
import main.events.ExitEvent;
import main.Level;
import main.math.Vec2d;
import main.math.Vec2i;

/**
 * Class describing the Exit entity
 */
public class Exit extends Terrain{

    {
        symbol = 'X';
    }

    /**
     * Basic constructor
     * @param level current level
     */
    public Exit(Level level) {
        super(level);
    }

    public Exit(Level level, Vec2i pos) {
        super(level, pos);
    }

    @Override
    public void onCreated() {
        Rectangle exit = new Rectangle(30, 30, Color.GOLD);
        view.addNode(exit);
        view.setCentre(new Vec2d(15, 15));
    }

    @Override
    public boolean isPassableFor(Entity entity) {
        return false;
    }

    @Override
    public boolean isPassableForAvatar(Avatar avatar) {
        return true;
    }

    @Override
    public boolean canStackFor(Entity entity) {
        return false;
    }

    @Override
    public void onEnterByAvatar(Avatar avatar) {
        level.postEvent(new ExitEvent(ExitEvent.EXIT_SUCCESS));
    }
}