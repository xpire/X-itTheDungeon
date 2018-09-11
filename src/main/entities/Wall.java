package main.entities;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import main.math.Vec2d;
import main.math.Vec2i;

import java.util.function.Function;

/*

Idea: tile builder - to enforce stacking rules

 */

public class Wall extends Entity {

    public Wall() {
        super("Wall");
    }

    public Wall(String name) {
        super(name);
    }

    public Wall(String name, Function<Vec2i, Vec2d> gridToWorld) {
        super(name, gridToWorld);
    }

    public Wall(String name, Function<Vec2i, Vec2d> gridToWorld, Vec2i pos) {
        super(name, gridToWorld, pos);
    }

    @Override
    public void onCreated() {
        Rectangle rect = new Rectangle();
        rect.setFill(Color.BROWN);
        rect.setHeight(30);
        rect.setWidth(30);

        view.addNode(rect);
        view.setCentre(new Vec2d(15, 15));
    }

    @Override
    public boolean isPassable() {
        return false;
    }
}
