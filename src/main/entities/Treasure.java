package main.entities;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import main.avatar.Avatar;
import main.maploading.Level;
import main.math.Vec2d;
import main.math.Vec2i;

public class Treasure extends Entity {

    {
        symbol = '$';
    }

    private Circle circ;

    public Treasure() {
        super("Treasure");
    }

    public Treasure(String name) {
        super(name);
    }

    public Treasure(String name, char symbol) {
        super(name, symbol);
    }

    public Treasure(String name, Level level, Vec2i pos) {
        super(name, level, pos);
    }



    @Override
    public void onCreated(){

        circ = new Circle(5, Color.GOLD);
        view.addNode(circ);
        view.setCentre(new Vec2d(0, 0));
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
