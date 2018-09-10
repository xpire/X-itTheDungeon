package main.entities;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import main.math.Vec2d;

public class Boulder extends Entity{


    public Boulder() {
        super("Boulder");

        Circle circle = new Circle();
        circle.setFill(Color.HONEYDEW);
        circle.setRadius(12);

        view.addNode(circle);
        view.setCentre(new Vec2d(0, 0));
    }

    public boolean isPassable() {
        return false;
    }


}
