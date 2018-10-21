package main.entities.pickup;

import javafx.scene.shape.Circle;
import main.entities.Avatar;
import main.events.TreasureEvent;
import main.Level;
import main.math.Vec2d;
import main.sprite.SpriteView;

/**
 * Class describing the Treasure entity
 */
public class Treasure extends Pickup{

    private Circle coin;

    {
        symbol = '$';
    }

    /**
     * Basic constructor
     * @param level : current level
     */
    public Treasure(Level level) {
        super(level);
    }

    @Override
    public void onCreated(){
        super.onCreated();
        sprite = new SpriteView(getImage("sprite/pickup/treasure/5.png"),new Vec2d(-8,-8), 1,1);
        view.addNode(sprite);

        level.postEvent(new TreasureEvent(TreasureEvent.TREASURE_CREATED));
    }

    @Override
    public void destroy() {
        super.destroy();
        level.postEvent(new TreasureEvent(TreasureEvent.TREASURE_COLLECTED));
    }

    @Override
    public boolean onPickupBy(Avatar avatar) {
        return avatar.pickUpTreasure(this);
    }
}
