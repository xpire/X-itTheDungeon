package main.entities;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import main.core.GridPositionComponent;
import main.math.Vec2d;

/*

Idea: tile builder - to enforce stacking rules

 */

public class Wall extends GridEntity {

    public Wall(GridPositionComponent pos) {
        super("Wall", pos);

        Rectangle rect = new Rectangle();
        rect.setFill(Color.BROWN);
        rect.setHeight(30);
        rect.setWidth(30);

        view.addNode(rect);
        view.setCentre(new Vec2d(15, 15));
    }


    public boolean isPassable() {
        return false;
    }
}
