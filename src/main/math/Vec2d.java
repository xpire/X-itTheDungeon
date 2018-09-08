package main.math;

public class Vec2d {


    private double x;
    private double y;


    public Vec2d(){
        this(0,0);
    }

    public Vec2d(double x, double y) {
        this.x = x;
        this.y = y;
    }


    public Vec2d(Vec2d other) {
        this.x = other.x;
        this.y = other.y;
    }

    public void add(double u, double v) {
        x += u;
        y += v;
    }

    public void sub(double u, double v) {
        x -= u;
        y -= v;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
