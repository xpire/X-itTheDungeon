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




    public Vec2d add(double u, double v) {
        return new Vec2d(x + u, y + v);
    }

    public Vec2d add(Vec2d other) {
        return new Vec2d(x + other.x, y + other.y);
    }

    public Vec2d sub(double u, double v) {
        return new Vec2d(x - u, y - v);
    }

    public Vec2d sub(Vec2d other) {
        return new Vec2d(x - other.x, y - other.y);
    }

    public void _add(double u, double v) {
        x += u;
        y += v;
    }

    public void _add(Vec2d other) {
        x -= other.x;
        y -= other.y;
    }

    public void _sub(double u, double v) {
        x -= u;
        y -= v;
    }

    public void _sub(Vec2d other) {
        x -= other.x;
        y -= other.y;
    }





    public Vec2d clipX(double min, double max) {
        return new Vec2d(clip(x, min, max), y);
    }

    public Vec2d clipY(double min, double max) {
        return new Vec2d(x, clip(y, min, max));
    }

    public Vec2d clip(Vec2d min, Vec2d max) {
        Vec2d clipped = new Vec2d(this);

        clipped._clipX(min.getX(), max.getX());
        clipped._clipY(min.getY(), max.getY());
        return clipped;
    }

    private double clip(double value, double min, double max) {
        value = value < min ? min : value;
        value = value > max ? max : value;

        return value;
    }




    public void _clipX(double min, double max) {
        x = clip(x, min, max);
    }

    public void _clipY(double min, double max) {
        y = clip(y, min, max);
    }

    public void _clip(Vec2d min, Vec2d max) {
        clipX(min.getX(), max.getX());
        clipY(min.getY(), max.getY());
    }



    public boolean isWithinX(double min, double max) {
        return isWithin(x, min, max);
    }

    public boolean isWithinY(double min, double max) {
        return isWithin(y, min, max);
    }

    public boolean isWithin(Vec2d min, Vec2d max) {
        return isWithinX(min.getX(), max.getX()) && isWithinY(min.getY(), max.getY());
    }
    private boolean isWithin(double value, double min, double max) {
        return min <= value && value <= max;
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

    public void set(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void set(Vec2d other) {
        x = other.x;
        y = other.y;
    }





    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof Vec2d)) return false;

        Vec2d other = (Vec2d) o;
        return x == other.getX() && y == other.getY();
    }

    @Override
    public String toString() {
        return String.format("%f, %f", x, y);
    }
}
