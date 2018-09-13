package main.entities;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import main.avatar.Avatar;

public class HoverPot extends Entity {

    private Group pot;

    {
        symbol = '^';
    }

    public HoverPot() {
        super("Hover Pot");
    }

    @Override
    public void onCreated(){

        Circle potBottom                = new Circle(5, Color.GREENYELLOW);
        Rectangle potPipe               = new Rectangle(4, 10, Color.GREENYELLOW);
        potPipe.setTranslateX(-2);
        potPipe.setTranslateY(-10);

        pot = new Group();
        pot.getChildren().add(potPipe);
        pot.getChildren().add(potBottom);
        pot.setTranslateY(3);

        view.addNode(pot);
    }

    @Override
    public boolean isPassableFor(Entity other) {
        if (other.getName().equals("Boulder")) {
            return false;
        }

        return true;
    }


    @Override
    public void onEntityEnter(Entity other) {

        if (other instanceof Avatar) {
            Avatar avatar = (Avatar) other;

            avatar.onHoverStart();
            onRemovedFromMap();
        }
    }
}