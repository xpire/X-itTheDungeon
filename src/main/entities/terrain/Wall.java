package main.entities.terrain;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import main.entities.Entity;
import main.maploading.Level;
import main.math.Vec2d;
import main.math.Vec2i;


/**
 * Class describing the Wall entity
 */
public class Wall extends Terrain {

    {
        symbol = '*';
    }

    /**
     * Basic constructor
     * @param level current level
     */
    public Wall(Level level) {
        super(level);
    }

    public Wall(Level level, Vec2i pos) {
        super(level, pos);
    }

    @Override
    public void onCreated() {
        Rectangle wall = new Rectangle(30, 30, Color.BROWN);
        view.addNode(wall);
        view.setCentre(new Vec2d(15, 15));
    }

    @Override
    public boolean isPassableFor(Entity entity) {
        return false;
    }

    @Override
    public boolean canStackFor(Entity entity) {
        return false;
    }
}
