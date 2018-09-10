package main.Entities;

import javafx.scene.Node;
import main.core.ViewComponent;
import main.math.Vec2d;

public abstract class Entity {

    protected String name;

    protected ViewComponent view;
    protected char symbol;

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

    public char getSymbol() {
        return symbol;
    }

}
