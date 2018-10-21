package main.entities.pickup;

import main.Level;
import main.entities.Avatar;
import main.math.Vec2d;
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


    @Override
    public void onCreated(){
        super.onCreated();
        sprite = new SpriteView(getImage("sprite/pickup/bomb/1.png"),new Vec2d(-8,-8), 1,1);
        view.addNode(sprite);
    }

    @Override
    public boolean onPickupBy(Avatar avatar) {
        return avatar.pickUpBomb(this);
    }
}
