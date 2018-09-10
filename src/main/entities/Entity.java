package main.entities;

import javafx.scene.Node;
import main.component.GridPositionComponent;
import main.component.ViewComponent;
import main.math.Vec2d;

public class Entity {

    protected String name;

    protected ViewComponent view;
    protected GridPositionComponent pos;

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

    public Vec2d getCentre() {
        return view.getCentre();
    }

    public void moveTo(Vec2d pos) {
        view.moveTo(pos);
    }

    public char getSymbol() {
        return symbol;
    }

}
