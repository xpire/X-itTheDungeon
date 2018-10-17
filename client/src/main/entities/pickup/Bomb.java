package main.entities.pickup;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import main.entities.Avatar;
import main.Level;
import main.math.Vec2d;
import main.math.Vec2i;
import main.sprite.SpriteView;

/**
 * Class describing the Bomb entity
 */
public class Bomb extends Pickup{

    {
        symbol = '!';
    }

    /**
     * Basic constructor
     * @param level : current Level
     */
    public Bomb(Level level) {
        super(level);
    }

    public Bomb(Level level, Vec2i pos) {
        super(level, pos);
    }


    @Override
    public void onCreated(){
//        view.addNode(new Circle(5, Color.BLACK));
        sprite = new SpriteView(getImage("sprite/pickup/bomb/0.png"),new Vec2d(-8,-8), 1,1);
        view.addNode(sprite);
    }

    @Override
    public boolean onPickupBy(Avatar avatar) {
        return avatar.pickUpBomb(this);
    }
}
