package main.math;

public class Vec2f {


    private float x;
    private float y;


    public Vec2f() {
        this(0,0);
    }

    public Vec2f(float x, float y) {
        this.x = x;
        this.y = y;
    }


    public Vec2f(Vec2f other) {
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

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}
