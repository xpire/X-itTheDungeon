package main.entities.terrain;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import main.entities.Entity;
import main.Level;
import main.math.Vec2d;
import main.math.Vec2i;
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
//        Rectangle tile = new Rectangle(30, 30, Color.rgb(222, 222, 222));
//        tile.setStrokeType(StrokeType.INSIDE);
//        tile.setStroke(Color.BLACK);
//        tile.setStrokeWidth(0.2);
//        view.addNode(tile);
//        view.setCentre(new Vec2d(15, 15));
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
