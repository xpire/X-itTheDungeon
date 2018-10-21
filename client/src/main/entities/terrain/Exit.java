package main.entities.terrain;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import main.entities.Avatar;
import main.entities.Entity;
import main.events.ExitEvent;
import main.Level;
import main.math.Vec2d;
import main.math.Vec2i;
import main.sprite.SpriteView;

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

    @Override
    public void onCreated() {
        super.onCreated();
        sprite = new SpriteView(getImage("sprite/terrain/exit/5.png"),new Vec2d(-8,-8), 1.875,1.875);
        view.addNode(sprite);
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