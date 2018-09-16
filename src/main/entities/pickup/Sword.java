package main.entities.pickup;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import main.entities.Avatar;
import main.maploading.Level;
import main.math.Vec2d;
import main.math.Vec2i;

public class Sword extends Pickup {

    private int durability;

    {
        symbol = '+';
        durability = 5;
    }

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


    public void reduceDurability() {
        durability--;
    }

    public boolean isBroken() {
        return durability <= 0;
    }


    @Override
    public boolean onPickupBy(Avatar avatar) {
        return avatar.pickUpSword(this);
    }
}
