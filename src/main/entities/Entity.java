package main.entities;

import javafx.scene.Node;
import main.component.GridPositionComponent;
import main.component.ViewComponent;
import main.math.Vec2d;
import main.math.Vec2i;

public class Entity {

    protected String name;

    protected ViewComponent view;
    protected GridPositionComponent pos;

    protected char symbol;



    public Entity(String name) {
        this.name = name;
        this.view = new ViewComponent();
    }

    public Entity(String name, GridPositionComponent pos) {
        this(name);
        this.pos = pos;
    }


    public Node getView() {
        return view.getView();
    }

    public Vec2d getCentre() {
        return view.getCentre();
    }


    // Positions

    public void moveTo(int row, int col) {
        pos.moveTo(row, col);
        view.moveTo(pos.getWorldPos().sub(view.getCentre()));
    }

    public void moveTo(Vec2i newPos) {
        moveTo(newPos.getX(), newPos.getY());
    }

    public void moveBy(int dx, int dy) {
        moveTo(pos.getCol() + dx, pos.getRow() + dy);
    }

    public void moveBy(Vec2i dv) {
        moveBy(dv.getX(), dv.getY());
    }

    public Vec2i getGridPos() {
        return pos.getGridPos();
    }

    public Vec2d getWorldPos() {
        return pos.getWorldPos();
    }

    public int getRow() {
        return pos.getRow();
    }

    public int getCol() {
        return pos.getCol();
    }






    public char getSymbol() {
        return symbol;
    }


    public String getName() {
        return name;
    }

    public boolean isPassable() {
        return true;
    }


    public void setGridPositionComponent(GridPositionComponent pos){
        this.pos = pos;
    }
}
