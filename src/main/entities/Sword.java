package main.entities;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import main.avatar.Avatar;
import main.math.Vec2d;

public class Sword extends Entity{

    private int durability;
    private Rectangle rect;

    {
        symbol = '+';
        durability = 5;
    }

    public Sword() {
        super("Sword");
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
    public void onCreated(){

        rect = new Rectangle(16, 4);
        rect.setFill(Color.STEELBLUE);
        view.addNode(rect);
        view.setCentre(new Vec2d(8, 2));
    }


    @Override
    public boolean isPassableFor(Entity other) {
        if (other.getName().equals("Boulder") || other.getName().equals("Key")) {
            return false;
        }

        return true;
    }


    @Override
    public void onEntityEnter(Entity other) {

        if (other instanceof Avatar) {
            Avatar avatar = (Avatar) other;

            if (avatar.pickUp(this)) {
                onRemovedFromMap();
            }
        }
    }
}
