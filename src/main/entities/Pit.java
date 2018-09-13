package main.entities;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import main.avatar.Avatar;
import main.maploading.TileMap;
import main.math.Vec2d;
import main.math.Vec2i;

public class Pit extends Entity {

    private Rectangle rect;


    {
        symbol = '#';
    }


    public Pit() {
        super("Switch");
    }

    public Pit(String name) {
        super(name);
    }

    public Pit(String name, TileMap map, Vec2i pos) {
        super(name, map, pos);
    }


    @Override
    public void onCreated(){

        rect = new Rectangle(30, 30, Color.BLACK);
        view.addNode(rect);
        view.setCentre(new Vec2d(15, 15));
    }

    @Override
    public void onEntityEnter(Entity other) {
        if (other.getName().equals("Avatar")) {
            ((Avatar) other).onDeath();
        }
    }
}