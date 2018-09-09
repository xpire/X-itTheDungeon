package main.core;

import javafx.scene.Node;
import main.math.Vec2d;

public abstract class Entity {

    protected String name;

    protected ViewComponent view;

    public Entity(String name) {
        this.name = name;
        this.view = new ViewComponent();
    }

    public String getName() {
        return name;
    }


    public boolean isPassable() {
        return true;
    }

    public Node getView() {
        return view.getView();
    }

    public void moveTo(Vec2d pos) {
        view.moveTo(pos);
    }
}
