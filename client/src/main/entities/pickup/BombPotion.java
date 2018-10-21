package main.entities.pickup;

import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import main.Level;
import main.entities.Avatar;
import main.math.Vec2d;
import main.math.Vec2i;
import main.sprite.SpriteView;

/**
 * Class describing the Hover Potion entity
 */
public class BombPotion extends Pickup{

    private Group pot;

    {
        symbol = '@';
    }

    /**
     * Basic constructor
     * @param level
     */
    public BombPotion(Level level) {
        super(level);
    }

    @Override
    public void onCreated(){
        super.onCreated();
        sprite = new SpriteView(getImage("sprite/pickup/potion/3.png"),new Vec2d(-8,-8), 1,1);
        view.addNode(sprite);
    }

    @Override
    public boolean onPickupBy(Avatar avatar) {

        if (avatar.pickUpBombPotion(this)) {
            return true;
        }
        return false;
    }
}