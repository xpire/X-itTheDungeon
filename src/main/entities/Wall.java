package main.entities;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import main.maploading.TileMap;
import main.math.Vec2d;
import main.math.Vec2i;

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

    public Wall(String name, TileMap map, Vec2i pos) {
        super(name, map, pos);
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
