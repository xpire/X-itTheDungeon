package main.entities.pickup;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import main.entities.Avatar;
import main.Level;
import main.math.Vec2d;
import main.math.Vec2i;
import main.sprite.SpriteView;

/**
 * Class describing the sword entity
 */
public class Sword extends Pickup {

    private int durability;

    {
        symbol = '+';
        durability = 5;
    }

    /**
     * Basic constructor
     * @param level : current level
     */
    public Sword(Level level) {
        super(level);
    }


    @Override
    public void onCreated(){
        super.onCreated();
        sprite = new SpriteView(getImage("sprite/pickup/sword/0.png"),new Vec2d(-8,-8), 1,1);
        view.addNode(sprite);
    }


    /**
     * reduce the durability of the sword after a successful swing
     */
    public void reduceDurability() {
        durability--;
    }

    /**
     * check if the sword has broken
     * @return true if sword has broken, false otherwise
     */
    public boolean isBroken() {
        return durability <= 0;
    }


    @Override
    public boolean onPickupBy(Avatar avatar) {
        return avatar.pickUpSword(this);
    }

    public int getDurability() {
        return durability;
    }
}
