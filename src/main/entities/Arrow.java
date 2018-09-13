package main.entities;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import main.avatar.Avatar;
import main.maploading.TileMap;
import main.math.Vec2d;
import main.math.Vec2i;

public class Arrow extends Entity {

    private Rectangle rect;

    {
        symbol = '-';
    }

    public Arrow() {
        super("Arrow");
    }

    public Arrow(String name, TileMap map, Vec2i pos) {
        super(name, map, pos);
    }

    @Override
    public void onCreated(){

        rect = new Rectangle(16, 4);
        rect.setFill(Color.SALMON);
        view.addNode(rect);
        view.setCentre(new Vec2d(8, 2));
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
