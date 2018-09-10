package main.core;

import main.math.Vec2d;
import main.math.Vec2i;

import java.util.function.Function;

public class GridPositionComponent extends Component{

    private Function<Vec2i, Vec2d> gridToWorld;
    private Vec2i pos = new Vec2i(0, 0);

    public GridPositionComponent(Function<Vec2i, Vec2d> gridToWorld) {
        this.gridToWorld = gridToWorld;
    }

    public GridPositionComponent(Function<Vec2i, Vec2d> gridToWorld, int row, int col) {
        this(gridToWorld);
        pos.set(row, col);
    }

    public GridPositionComponent(Function<Vec2i, Vec2d> gridToWorld, Vec2i newPos) {
        this(gridToWorld, newPos.getX(), newPos.getY());
    }

    public GridPositionComponent clone() {
        return new GridPositionComponent(gridToWorld, pos);
    }

    public void moveTo(int row, int col) {
        pos.set(row, col);
    }

    public void moveTo(Vec2i newPos) {
        moveTo(newPos.getX(), newPos.getY());
    }

    public void moveBy(int dx, int dy) {
        pos._add(dx, dy);
    }

    public void moveBy(Vec2i dv) {
        moveBy(dv.getX(), dv.getY());
    }

    public Vec2i getGridPos() {
        return new Vec2i(pos);
    }

    public Vec2d getWorldPos() {
        return gridToWorld.apply(pos);
    }

    public int getRow() {
        return pos.getX();
    }

    public int getCol() {
        return pos.getY();
    }
}
