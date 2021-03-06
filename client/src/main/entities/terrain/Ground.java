package main.entities.terrain;

import main.Level;
import main.entities.Entity;
import main.math.Vec2d;
import main.sprite.SpriteView;

/**
 * Class describing the Ground entity
 */
public class Ground extends Terrain {

    {
        symbol = '.';
    }

    /**
     * Basic constructor
     * @param level : current level
     */
    public Ground(Level level) { super(level); }

    @Override
    public void onCreated() {
        super.onCreated();
        sprite = new SpriteView(getImage("sprite/terrain/ground/8.png"),new Vec2d(-8,-8), 1.875,1.875);
        view.addNode(sprite);
    }

    @Override
    public boolean isPassableFor(Entity entity) { return true; }

    @Override
    public boolean canStackFor(Entity entity) {
        return true;
    }
}
