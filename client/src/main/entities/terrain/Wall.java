package main.entities.terrain;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import main.entities.Entity;
import main.Level;
import main.math.Vec2d;
import main.math.Vec2i;
import main.sprite.SpriteView;


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
//        Rectangle wall = new Rectangle(30, 30, Color.BROWN);
//        view.addNode(wall);
//        view.setCentre(new Vec2d(15, 15));
        Pane pane = new Pane();
        sprite = new SpriteView(getImage("sprite/terrain/wall/1.png"),new Vec2d(-8,-8), 1.875,1.875);
        pane.getChildren().add(sprite);
        view.addNode(pane);
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
