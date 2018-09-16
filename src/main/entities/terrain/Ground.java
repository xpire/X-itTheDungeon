package main.entities.terrain;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import main.entities.Entity;
import main.maploading.Level;
import main.math.Vec2d;
import main.math.Vec2i;

public class Ground extends Terrain {

    {
        symbol = '.';
    }

    public Ground(Level level) { super(level); }

    public Ground(Level level, Vec2i pos) {
        super(level, pos);
    }

    @Override
    public void onCreated() {
        Rectangle tile = new Rectangle(30, 30, Color.rgb(222, 222, 222));
        tile.setStrokeType(StrokeType.INSIDE);
        tile.setStroke(Color.BLACK);
        tile.setStrokeWidth(0.2);
        view.addNode(tile);
        view.setCentre(new Vec2d(15, 15));
    }

    @Override
    public boolean isPassableFor(Entity entity) { return true; }

    @Override
    public boolean canStackFor(Entity entity) {
        return true;
    }
}
