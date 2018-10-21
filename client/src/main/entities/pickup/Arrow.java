package main.entities.pickup;

import main.Level;
import main.entities.Avatar;
import main.math.Vec2d;
import main.sprite.SpriteView;

/**
 * Class describing the Arrow entity
 */
public class Arrow extends Pickup{

    {
        symbol = '-';
    }

    /**
     * Basic constructor
     * @param level current Level
     */
    public Arrow(Level level) {
        super(level);
    }


    @Override
    public void onCreated(){
        super.onCreated();
        sprite = new SpriteView(getImage("sprite/pickup/arrow/0.png"),new Vec2d(-8,-8), 1,1);
        view.addNode(sprite);
    }

    @Override
    public boolean onPickupBy(Avatar avatar) {
        return avatar.pickUpArrow(this);
    }
}
