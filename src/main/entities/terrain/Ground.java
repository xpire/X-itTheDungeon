package main.entities.terrain;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import main.entities.Avatar;
import main.entities.Entity;
import main.entities.enemies.Enemy;
import main.entities.pickup.Pickup;
import main.entities.prop.Prop;
import main.maploading.Level;
import main.math.Vec2d;
import main.math.Vec2i;

public class Ground extends Terrain {

    {
        symbol = '.';
    }

    public Ground(Level level) {
        super(level);

        Rectangle rect = new Rectangle(30, 30, Color.rgb(222, 222, 222));
        rect.setStrokeType(StrokeType.INSIDE);
        rect.setStroke(Color.BLACK);
        rect.setStrokeWidth(0.2);
        view.addNode(rect);
        view.setCentre(new Vec2d(15, 15));
    }

    public Ground(Level level, Vec2i pos) {
        super(level, pos);
    }

    @Override
    public boolean canStackFor(Entity entity) {
        return true;
    }

    @Override
    public boolean canStackForProp(Prop prop) {
        return canStackFor(prop);
    }

    @Override
    public boolean canStackForPickup(Pickup pickup) {
        return canStackFor(pickup);
    }

    @Override
    public boolean canStackForEnemy(Enemy enemy) {
        return canStackFor(enemy);
    }

    @Override
    public boolean canStackForAvatar(Avatar avatar) {
        return canStackFor(avatar);
    }
}
