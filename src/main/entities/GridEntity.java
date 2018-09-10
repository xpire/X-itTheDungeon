package main.entities;

import main.core.GridPositionComponent;
import main.math.Vec2d;
import main.math.Vec2i;

/**
 * For entities whose positions are restricted to a grid world
 */
public class GridEntity extends Entity{

    protected String name;
    protected char symbol;

    protected GridPositionComponent pos;

    public GridEntity(String name, GridPositionComponent pos) {
        super(name);
        this.pos = pos;
    }

    public void moveTo(int row, int col) {
        pos.moveTo(row, col);
        view.moveTo(pos.getWorldPos().sub(view.getCentre()));
    }

    public void moveTo(Vec2i newPos) {
        moveTo(newPos.getX(), newPos.getY());
    }

    public void moveBy(int dx, int dy) {
        pos.moveTo(pos.getCol() + dx, pos.getRow() + dy);
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
}
