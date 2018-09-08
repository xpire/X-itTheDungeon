package main.math;

public class Vec2i {


    private int x;
    private int y;

    public Vec2i(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vec2i(Vec2i other) {
        this.x = other.x;
        this.y = other.y;
    }

    public void add(float u, float v) {
        x += u;
        y += v;
    }

    public void sub(float u, float v) {
        x -= u;
        y -= v;
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
}
