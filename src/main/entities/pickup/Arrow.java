package main.entities.pickup;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import main.entities.Avatar;
import main.maploading.Level;
import main.math.Vec2d;
import main.math.Vec2i;

public class Arrow extends Pickup{

    {
        symbol = '-';
    }

    public Arrow(Level level) {
        super(level);
    }

    public Arrow(Level level, Vec2i pos) {
        super(level, pos);
    }

    @Override
    public void onCreated(){
        view.addNode(new Rectangle(16, 4, Color.SALMON));
        view.setCentre(new Vec2d(8, 2));
    }


    @Override
    public boolean onPickupBy(Avatar avatar) {
        return avatar.pickUpArrow(this);
    }
}
