package main.entities.prop;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import main.entities.Avatar;
import main.entities.Entity;
import main.Level;
import main.math.Vec2d;
import main.math.Vec2i;
import main.sprite.SpriteView;

/**
 * Class describing the Boulder entity
 */
public class Boulder extends Prop {

    {
        symbol = 'O';
        isHeavy = true;
    }

    /**
     * Basic constructor
     * @param level
     */
    public Boulder(Level level) {
        super(level);
    }

    public Boulder(Level level, Vec2i pos) {
        super(level, pos);
    }


    @Override
    public void onCreated(){
//        Circle circle = new Circle(12, Color.HONEYDEW);
//        view.addNode(circle);
        Pane pane = new Pane();
        sprite = new SpriteView(getImage("sprite/prop/boulder/0.png"),new Vec2d(-7,-7), 1.5,1.5);
        pane.getChildren().add(sprite);
        view.addNode(pane);
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
        Vec2i target = pos.sub(avatar.getGridPos()).add(pos);

        if (level.isPassableForProp(target, this)) {
            level.moveProp(target, this);
            return true;
        }

        return false;
    }
}
