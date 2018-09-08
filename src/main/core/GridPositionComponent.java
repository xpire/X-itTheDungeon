package main.core;

import main.math.Vec2i;

public class GridPositionComponent extends Component{

    private Vec2i pos;


    public GridPositionComponent(int row, int col) {
        this.pos = new Vec2i(row, col);
    }

    public void moveTo(int row, int col) {
        pos.setX(row);
        pos.setY(col);
    }

    public void moveBy(int dx, int dy) {
        pos.add(dx, dy);
    }

    public int getRow() {
        return pos.getX();
    }

    public int getCol() {
        return pos.getY();
    }
}
