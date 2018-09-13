package main.entities;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import main.maploading.TileMap;
import main.math.Vec2d;
import main.math.Vec2i;

public class Switch extends Entity{

    private boolean isOn;
    private Rectangle rect;


    public Switch() {
        this("Switch");
    }

    public Switch(String name) {
        super(name);
    }

    public Switch(String name, TileMap map, Vec2i pos) {
        super(name, map, pos);
    }


    public void onActivated() {
        System.out.println("Switch Activated");
        isOn = true;
        rect.setFill(Color.GREEN);
    }

    public void onDeactivated() {
        isOn = false;
        rect.setFill(Color.RED);
    }

    @Override
    public void onCreated(){
        symbol = '/';

        rect = new Rectangle();
        rect.setWidth(30);
        rect.setHeight(30);

        onDeactivated();

        view.addNode(rect);
        view.setCentre(new Vec2d(15, 15));
    }

    @Override
    public void onEntityEnter(Entity other) {
        if (other.getName().equals("Boulder")) {
            onActivated();
        }
    }

    @Override
    public void onEntityLeave(Entity other) {
        if (other.getName().equals("Boulder")) {
            onDeactivated();
        }
    }
}
