package main.entities.terrain;

import main.Level;
import main.entities.Avatar;
import main.entities.Entity;
import main.entities.enemies.Enemy;
import main.entities.prop.Prop;
import main.math.Vec2d;
import main.sprite.SpriteView;

/**
 * Class describing the Pit entity
 */
public class Pit extends Terrain{

    {
        symbol = '#';
    }

    /**
     * Basic constructor
     * @param level : level pit belongs to
     */
    public Pit(Level level) {
        super(level);
    }

    @Override
    public void onCreated(){
        super.onCreated();
        sprite = new SpriteView(getImage("sprite/terrain/pit/0.png"),new Vec2d(-8,-8), 1.875,1.875);
        view.addNode(sprite);
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
        prop.destroy();
    }

    @Override
    public void onEnterByAvatar(Avatar avatar) {
        avatar.onThreatenedByPit();
    }
}