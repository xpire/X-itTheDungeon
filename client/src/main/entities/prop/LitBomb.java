package main.entities.prop;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import main.entities.Entity;
import main.Level;
import main.math.Vec2i;

import java.util.Iterator;

/**
 * Class describing the Lit Bomb entity
 */
public class LitBomb extends Prop{

    private Circle bomb;

    private final int MAX_FUSE_LENGTH = 5;
    private int fuseLength = MAX_FUSE_LENGTH;
    private int radius = 1;

    /**
     * Basic Constructor
     * @param level
     */
    public LitBomb(Level level) {
        super(level);
    }

    public LitBomb(Level level, int radius) {
        super(level);
        this.radius = radius;
    }

    public LitBomb(Level level, Vec2i pos) {
        super(level, pos);
    }


    @Override
    public void onCreated(){
        bomb = new Circle(5, Color.BLACK);
        view.addNode(bomb);
    }


    @Override
    public void onTurnUpdate() {
        fuseLength--;

        int count = MAX_FUSE_LENGTH - fuseLength;
        int redness = Math.min( (int)(((double)count)/MAX_FUSE_LENGTH * 255), 255);
        bomb.setFill(Color.rgb(redness, 0, 0));

        if (fuseLength <= 0)
            onExplosion();
    }


    /**
     * Logic when the bomb explodes, killing everythings in the
     * plus shape around the bomb
     */
    public void onExplosion() {
        destroyEntity(pos);

        for (Vec2i dir : Vec2i.DIRECTIONS) {
            Vec2i target = new Vec2i(pos);
            for (int i = 1; i <= radius; i++) {
                target = target.add(dir);
                destroyEntity(target);
            }
        }

        onDestroyed();
    }


    /**
     * Destroying entities on a certain position
     * @param pos : position to destroy entities
     */
    public void destroyEntity(Vec2i pos) {
        Iterator<Entity> it = level.getEntitiesAt(pos);
        it.forEachRemaining(Entity::onExploded);
    }


    @Override
    public boolean isPassableFor(Entity entity) {
        return true;
    }

    @Override
    public boolean isPassableForProp(Prop prop) {
        return prop.isProjectile;
    }

    @Override
    public boolean canStackFor(Entity entity) {
        return true;
    }
}
