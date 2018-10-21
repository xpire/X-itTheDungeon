package main.entities.pickup;

import main.Level;
import main.entities.Avatar;
import main.math.Vec2d;
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

    /**
     * Getter for the remaining durability of a sword
     * @return the remaining durability
     */
    public int getDurability() {
        return durability;
    }
}
