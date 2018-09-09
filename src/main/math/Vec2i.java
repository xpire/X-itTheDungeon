package main.math;
public class Vec2i {


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

    public void add(int u, int v) {
        x += u;
        y += v;
    }

    public void sub(int u, int v) {
        x -= u;
        y -= v;
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

    private boolean within(int value, int min, int max) {
        return min <= value && value <= max;
    }


    public void clipX(int min, int max) {
        x = clip(x, min, max);
    }

    public void clipY(int min, int max) {
        y = clip(y, min, max);
    }

    public void clip(Vec2i min, Vec2i max) {
        clipX(min.getX(), max.getX());
        clipY(min.getY(), max.getY());
    }

    private int clip(int value, int min, int max) {
        value = value < min ? min : value;
        value = value > max ? max : value;

        return value;
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




    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof Vec2i)) return false;

        Vec2i other = (Vec2i) o;
        return x == other.getX() && y == other.getY();
    }



    @Override
    public String toString() {
        return String.format("%d, %d", x, y);
    }
}
