package main.entities.pickup;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import main.entities.Avatar;
import main.maploading.Level;
import main.math.Vec2d;
import main.math.Vec2i;

public class Bomb extends Pickup{

    private Circle circ;

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
        circ = new Circle(5, Color.BLACK);
        view.addNode(circ);
        view.setCentre(new Vec2d(0, 0));
    }

    @Override
    public boolean onPickupBy(Avatar avatar) {
        return avatar.pickUpBomb(this);
    }
}
