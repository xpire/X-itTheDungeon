package main.math;

import java.util.Objects;

public class Vec2i {

    private static final Vec2i NORTH = new Vec2i(0, -1);
    private static final Vec2i SOUTH = new Vec2i(0, 1);
    private static final Vec2i WEST = new Vec2i(-1, 0);
    private static final Vec2i EAST = new Vec2i(1, 0);

    private int x;
    private int y;

    public Vec2i() {
        this(0, 0);
    }

    public Vec2i(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vec2i(Vec2i other) {
        this.x = other.x;
        this.y = other.y;
    }

    public Vec2i add(int u, int v) {
        return new Vec2i(x + u, y + v);
    }

    public Vec2i add(Vec2i other) {
        return new Vec2i(x + other.x, y + other.y);
    }

    public Vec2i sub(int u, int v) {
        return new Vec2i(x - u, y - v);
    }

    public Vec2i sub(Vec2i other) {
        return new Vec2i(x - other.x, y - other.y);
    }

    private boolean within(int value, int min, int max) {
        return min <= value && value <= max;
    }


    public Vec2i clipX(int min, int max) {
        return new Vec2i(clip(x, min, max), y);
    }

    public Vec2i clipY(int min, int max) {
        return new Vec2i(x, clip(y, min, max));
    }

    public Vec2i clip(Vec2i min, Vec2i max) {
        Vec2i clipped = new Vec2i(this);

        clipped._clipX(min.getX(), max.getX());
        clipped._clipY(min.getY(), max.getY());
        return clipped;
    }

    private int clip(int value, int min, int max) {
        value = value < min ? min : value;
        value = value > max ? max : value;

        return value;
    }


    public void _add(int u, int v) {
        x += u;
        y += v;
    }

    public void _add(Vec2i other) {
        x += other.x;
        y += other.y;
    }

    public void _sub(int u, int v) {
        x -= u;
        y -= v;
    }

    public void _sub(Vec2i other) {
        x -= other.x;
        y -= other.y;
    }

    public boolean withinX(int min, int max) {
        return within(x, min, max);
    }

    public boolean withinY(int min, int max) {
        return within(y, min, max);
    }

    public boolean within(Vec2i min, Vec2i max) {
        return withinX(min.getX(), max.getX()) && withinY(min.getY(), max.getY());
    }

    public void _clipX(int min, int max) {
        x = clip(x, min, max);
    }

    public void _clipY(int min, int max) {
        y = clip(y, min, max);
    }

    public void _clip(Vec2i min, Vec2i max) {
        clipX(min.getX(), max.getX());
        clipY(min.getY(), max.getY());
    }


    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void set(Vec2i pos) {
        this.x = pos.x;
        this.y = pos.y;
    }

    public void set(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean isDirection() {
        return equals(Vec2i.NORTH) || equals(Vec2i.EAST) || equals(Vec2i.SOUTH) || equals(Vec2i.WEST);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof Vec2i)) return false;

        Vec2i other = (Vec2i) o;
        return x == other.getX() && y == other.getY();
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return String.format("%d, %d", x, y);
    }

    /**
     *
     * @param x First coordinate
     * @param y Second coordinate
     * @return the manhatten distance from 2 nodes on the grid
     */
    public int mDist(Vec2i y) { return Math.abs(y.getX() - this.x) + Math.abs(y.getY() - this.y); }
}
