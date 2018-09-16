package main.entities.pickup;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import main.entities.Avatar;
import main.maploading.Level;
import main.math.Vec2d;
import main.math.Vec2i;

public class Sword extends Pickup {

    private int durability;
    private Rectangle rect;

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

        rect = new Rectangle(16, 4, Color.STEELBLUE);
        view.addNode(rect);
        view.setCentre(new Vec2d(8, 2));
    }


    public int getDurability() {
        return durability;
    }

    public void reduceDurability() {
        durability--;
        if (durability == 0) {
            System.out.println("Sword has broken");
        }
    }

    public boolean isBroken() {
        return durability <= 0;
    }


    @Override
    public boolean onPickupBy(Avatar avatar) {
        return avatar.pickUpSword(this);
    }
}
