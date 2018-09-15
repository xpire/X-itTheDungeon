package main.entities;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import main.avatar.Avatar;
import main.maploading.Level;
import main.maploading.TileMap;
import main.math.Vec2d;
import main.math.Vec2i;


public class Door extends Entity{

    private boolean isOpen;
    private Rectangle rect;

    public Door() {
        super("Door");
    }

    public Door(String name) {
        super(name);
    }

    public void setPos(Vec2i pos) {
        this.pos = pos;
    }

    public Door(String name, Level map, Vec2i pos) {
        super(name, map, pos);
    }

    @Override
    public void onCreated(){
        symbol = '|';

        rect = new Rectangle();
        rect.setHeight(30);
        rect.setWidth(6);
        rect.setFill(Color.BLACK);
        view.addNode(rect);
        view.setCentre(new Vec2d(3, 15));

        isOpen = false;
    }

    public void onOpen() {
        isOpen = true;
        rect.setFill(Color.WHITE);
    }


    @Override
    public boolean isPassable() {
        return isOpen;
    }


    @Override
    public boolean isPassableFor(Entity other) {
        if (other.getName().equals("Boulder")) {
            return false;
        }

        return isOpen;
    }


    @Override
    public boolean onEntityPush(Entity other) {

        // Instead:
        // other.hasComponent(KeyHolder)
        if (other instanceof Avatar) {

            Avatar avatar = (Avatar) other;

            if (avatar.hasKeyFor(this)) {
                onOpen();
                avatar.useKey();
                return true;
            }
        }

        return false;
    }
}
