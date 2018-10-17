package main.entities.pickup;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import main.entities.Avatar;
import main.events.TreasureEvent;
import main.Level;
import main.math.Vec2i;

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

    public Treasure(Level level, Vec2i pos) {
        super(level, pos);
    }

    @Override
    public void onCreated(){
        super.onCreated(); //TODO: do this for all

        coin = new Circle(5, Color.GOLD);
        view.addNode(coin);

        level.postEvent(new TreasureEvent(TreasureEvent.TREASURE_CREATED));
    }

    @Override
    public void onDestroyed() {
        super.onDestroyed();
        level.postEvent(new TreasureEvent(TreasureEvent.TREASURE_COLLECTED));
    }

    @Override
    public boolean onPickupBy(Avatar avatar) {
        return avatar.pickUpTreasure(this);
    }
}
