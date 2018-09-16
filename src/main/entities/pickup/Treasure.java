package main.entities.pickup;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import main.entities.Avatar;
import main.maploading.Level;
import main.math.Vec2i;

public class Treasure extends Pickup{

    private Circle coin;

    {
        symbol = '$';
    }

    public Treasure(Level level) {
        super(level);
    }

    public Treasure(Level level, Vec2i pos) {
        super(level, pos);
    }

    @Override
    public void onCreated(){
        coin = new Circle(5, Color.GOLD);
        view.addNode(coin);
    }

    @Override
    public boolean onPickupBy(Avatar avatar) {
        return avatar.pickUpTreasure(this);
    }
}
