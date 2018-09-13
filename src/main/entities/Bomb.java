package main.entities;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import main.avatar.Avatar;
import main.maploading.TileMap;
import main.math.Vec2d;
import main.math.Vec2i;

public class Bomb extends Entity {

    private Circle circ;

    private int fuseLength = 3;
    private boolean isLit = false;

    {
        symbol = '!';
    }

    public Bomb() {
        super("Bomb");
    }

    public Bomb(String name) {
        super(name);
    }

    public Bomb(String name, char symbol) {
        super(name, symbol);
    }

    public Bomb(String name, TileMap map, Vec2i pos) {
        super(name, map, pos);
    }


    public void onLit() {
        isLit = true;
    }

    public void onExplode() {
        System.out.println("KABOOMBA!");
    }

    public void onTurnUpdate() {
        if (!isLit) return;

        fuseLength--;
        if (fuseLength <= 0) {
            onExplode();
            isLit = false; // safety measure for now
        }
    }



    @Override
    public void onCreated(){
        circ = new Circle(5, Color.BLACK);
        view.addNode(circ);
        view.setCentre(new Vec2d(0, 0));
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
