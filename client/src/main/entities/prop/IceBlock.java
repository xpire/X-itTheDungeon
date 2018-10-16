package main.entities.prop;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import main.Level;
import main.entities.Avatar;
import main.entities.Entity;
import main.math.Vec2i;

/**
 * Class describing the Boulder entity
 */
public class IceBlock extends Prop {

    // Melts when on a heat plate

    {
        symbol = 'I';
        isHeavy = true;
    }

    /**
     * Basic constructor
     * @param level
     */
    public IceBlock(Level level) {
        super(level);
    }

    public IceBlock(Level level, Vec2i pos) {
        super(level, pos);
    }


    @Override
    public void onCreated(){
        Circle circle = new Circle(12, Color.LIGHTBLUE);
        view.addNode(circle);
    }


    @Override
    public void onExploded() {
        onDestroyed();
    }


    @Override
    public boolean isPassableFor(Entity entity) {
        return false;
    }

    @Override
    public boolean canStackFor(Entity entity) {
        return false;
    }

    @Override
    public boolean onPush(Avatar avatar) {
        Vec2i dir = pos.sub(avatar.getGridPos());
        Vec2i target = pos.add(dir);

        if (!level.isPassableForProp(target, this))
            return false;

        while (level.isPassableForProp(target, this)) {
            level.moveProp(target, this);
            target = pos.add(dir);
        }

        return true;
    }
}
