package main.entities.terrain;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import main.entities.Avatar;
import main.entities.Entity;
import main.entities.enemies.Enemy;
import main.entities.pickup.Pickup;
import main.entities.prop.Prop;
import main.maploading.Level;
import main.math.Vec2d;
import main.math.Vec2i;

public class Pit extends Terrain{

    private Rectangle rect;

    {
        symbol = '#';
    }

    public Pit(Level level) {
        super(level);
    }

    public Pit(Level level, Vec2i pos) {
        super(level, pos);
    }


    @Override
    public void onCreated(){
        rect = new Rectangle(30, 30, Color.BLACK);
        view.addNode(rect);
        view.setCentre(new Vec2d(15, 15));
    }


    @Override
    public boolean isPassableFor(Entity entity) {
        return true;
    }

    @Override
    public boolean isPassableForEnemy(Enemy enemy) {
        return false;
    }

    @Override
    public void onEnterByProp(Prop prop) {
        prop.onDestroyed();
    }

    @Override
    public void onEnterByAvatar(Avatar avatar) {
        if (!avatar.isHovering()) {
            avatar.onDestroyed();
        }
    }

    @Override
    public boolean canStackFor(Entity entity) {
        return false;
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