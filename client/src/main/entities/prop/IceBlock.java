package main.entities.prop;

import javafx.animation.TranslateTransition;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import main.Level;
import main.entities.Avatar;
import main.entities.Entity;
import main.math.Vec2d;
import main.math.Vec2i;
import main.sprite.SpriteView;

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
//        Circle circle = new Circle(12, Color.LIGHTBLUE);
//        view.addNode(circle);
        sprite = new SpriteView(getImage("sprite/prop/boulder/0.png"),new Vec2d(-8,-8), 1.875,1.875);
        view.addNode(sprite);
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
        Vec2i originalPos = new Vec2i(pos);
        Vec2i dir = pos.sub(avatar.getGridPos());
        Vec2i target = pos.add(dir);

        if (!level.isPassableForProp(target, this))
            return false;

        while (level.isPassableForProp(target, this)) {
//            level.moveProp(target, this);
            target = target.add(dir);
        }
        TranslateTransition transition = new TranslateTransition();
        transition.setDuration(Duration.millis(1000));
        transition.setNode(sprite);
        if (dir.getY() == 0)
            transition.setToX(originalPos.getX() + (target.getX()-originalPos.getX())*30);
        if (dir.getX() == 0)
            transition.setByY(originalPos.getY() + (target.getY()-originalPos.getY())*30);
        transition.play();
        target = pos.add(dir);

        while (level.isPassableForProp(target, this)) {
            level.moveProp(target, this);
            target = pos.add(dir);
        }

        return true;
    }
}
