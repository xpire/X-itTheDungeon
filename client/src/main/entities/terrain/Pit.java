package main.entities.terrain;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import main.Level;
import main.entities.Avatar;
import main.entities.Entity;
import main.entities.enemies.Enemy;
import main.entities.prop.Prop;
import main.math.Vec2d;
import main.math.Vec2i;

/**
 * Class describing the Pit entity
 */
public class Pit extends Terrain{

    private Rectangle hole;

    {
        symbol = '#';
    }

    /**
     * Basic constructor
     * @param level
     */
    public Pit(Level level) {
        super(level);
    }

    public Pit(Level level, Vec2i pos) {
        super(level, pos);
    }


    @Override
    public void onCreated(){
        hole = new Rectangle(30, 30, Color.BLACK);
        view.addNode(hole);
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
    public boolean canStackFor(Entity entity) {
        return false;
    }

    @Override
    public void onEnterByProp(Prop prop) {
        prop.onDestroyed();
    }

    @Override
    public void onEnterByAvatar(Avatar avatar) {
        avatar.onThreatenedByPit(this);
    }
}