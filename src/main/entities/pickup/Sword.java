package main.entities.pickup;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import main.entities.Avatar;
import main.Level;
import main.math.Vec2d;
import main.math.Vec2i;

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

    public Sword(Level level, Vec2i pos) {
        super(level, pos);
    }


    @Override
    public void onCreated(){
        Rectangle stick = new Rectangle(16, 2, Color.STEELBLUE);
        stick.setRotate(-45);
        view.addNode(stick);
        view.setCentre(new Vec2d(8, 2));
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


    public int getDurability() {
        return durability;
    }


    @Override
    public boolean onPickupBy(Avatar avatar) {
        return avatar.pickUpSword(this);
    }
}
