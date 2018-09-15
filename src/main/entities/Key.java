package main.entities;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import main.avatar.Avatar;
import main.maploading.Level;
import main.maploading.TileMap;
import main.math.Vec2i;

public class Key extends Entity {

    private Door door;
    private Circle circ;

    public Key() {
        super("Key");
    }

    public Key(Vec2i pos) {
        super("Key");
        this.pos = pos;
    }

    public void setPos(Vec2i pos) {
        this.pos = pos;
    }

    public Key(String name, Level map, Vec2i pos) {
        super(name, map, pos);
    }

    @Override
    public void onCreated(){
        symbol = 'K';

        circ = new Circle(5);
        circ.setFill(Color.HOTPINK);
        view.addNode(circ);
//        view.setCentre(new Vec2d(3, 15));
    }


    // IDEA: give tags - item, entity, etc. to define the type

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


    public Door getMatchingDoor() {
        return door;
    }

    public void setMatchingDoor(Door door) {
        this.door = door;
    }
}
