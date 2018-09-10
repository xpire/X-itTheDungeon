package main;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import main.Entities.Entity;

/*

Idea: tile builder - to enforce stacking rules

 */

public class Wall extends Entity {

    public Wall(String name) {
        super(name);

        Rectangle rect = new Rectangle();
        rect.setFill(Color.BROWN);
        rect.setHeight(30);
        rect.setWidth(30);

        view.addNode(rect);
    }


    public boolean isPassable() {
        return false;
    }
}
