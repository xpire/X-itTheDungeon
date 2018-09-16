package main.entities.prop;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import main.entities.Entity;
import main.maploading.Level;
import main.math.Vec2d;
import main.math.Vec2i;

import java.util.Iterator;

public class LitBomb extends Prop{

    private Circle circ;

    private final int MAX_FUSE_LENGTH = 5;
    private int fuseLength = MAX_FUSE_LENGTH;

    public LitBomb(Level level) {
        super(level);
    }

    public LitBomb(Level level, Vec2i pos) {
        super(level, pos);
    }


    @Override
    public void onCreated(){
        circ = new Circle(5, Color.BLACK);
        view.addNode(circ);
        view.setCentre(new Vec2d(0, 0));
    }


    public void onExplosion() {
        destroyEntity(pos);
        destroyEntity(pos.add(-1, 0));
        destroyEntity(pos.add(1, 0));
        destroyEntity(pos.add(0, -1));
        destroyEntity(pos.add(0, 1));
    }

    public void destroyEntity(Vec2i pos) {
        Iterator<Entity> it = level.getEntitiesAt(pos);
        it.forEachRemaining(e -> e.onExploded());
    }

    @Override
    public void onTurnUpdate() {
        fuseLength--;
        circ.setFill(Color.rgb((int)((1 - (double)fuseLength/MAX_FUSE_LENGTH) * 255),0,0));

        if (fuseLength <= 0) {
            Prop prop = level.removeProp(getGridPos());
            onExplosion();
        }
    }

    @Override
    public boolean isPassableFor(Entity entity) {
        return true;
    }

    @Override
    public boolean isPassableForProp(Prop prop) {
        return prop.isProjectile;
    }
}
