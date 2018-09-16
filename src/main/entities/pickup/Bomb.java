package main.entities.pickup;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import main.entities.Avatar;
import main.maploading.Level;
import main.math.Vec2i;

public class Bomb extends Pickup{

    {
        symbol = '!';
    }

    public Bomb(Level level) {
        super(level);
    }

    public Bomb(Level level, Vec2i pos) {
        super(level, pos);
    }


    @Override
    public void onCreated(){
        view.addNode(new Circle(5, Color.BLACK));
    }

    @Override
    public boolean onPickupBy(Avatar avatar) {
        return avatar.pickUpBomb(this);
    }
}
