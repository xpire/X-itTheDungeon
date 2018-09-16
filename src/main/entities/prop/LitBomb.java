package main.entities.prop;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import main.entities.Entity;
import main.maploading.Level;
import main.math.Vec2i;

import java.util.Iterator;

public class LitBomb extends Prop{

    private Circle bomb;

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


    public void onExplosion() {
        destroyEntity(pos);
        destroyEntity(pos.add(-1, 0));
        destroyEntity(pos.add(1, 0));
        destroyEntity(pos.add(0, -1));
        destroyEntity(pos.add(0, 1));

        onDestroyed();
    }


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
