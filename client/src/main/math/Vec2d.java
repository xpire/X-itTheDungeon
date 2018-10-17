package main.math;

/**
 * 'Generic' Vector to Double class which represents a 2-tuple for convenience
 * in the rest of the code base
 */
public class Vec2d {

    private double x;
    private double y;

    /**
     * Default constructor, sets the 0 vector
     */
    public Vec2d(){
        this(0,0);
    }

    /**
     * Constructor which sets a specific 2-tuple
     * @param x x-coord
     * @param y y-coord
     */
    public Vec2d(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Constructor which copies another Vec2d
     * @param other Vec2d to be copied
     */
    public Vec2d(Vec2d other) {
        this.x = other.x;
        this.y = other.y;
    }



    /**
     * translates a vector by a positive constant amount
     * @param u constant to add to x-coord
     * @param v constant to add to y-coord
     * @return the translated vector
     */
    public Vec2d add(double u, double v) {
        return new Vec2d(x + u, y + v);
    }

    /**
     * Adds 2 vectors together
     * @param other Vector to be added to current
     * @return the translated vector
     */
    public Vec2d add(Vec2d other) {
        return new Vec2d(x + other.x, y + other.y);
    }

    /**
     * translates a vector by a negative constant amount
     * @param u constant to sub from x-coord
     * @param v constant to sub from y-coord
     * @return the translated vector
     */
    public Vec2d sub(double u, double v) {
        return new Vec2d(x - u, y - v);
    }

    /**
     * subtracts a vector from another
     * @param other the vector to subtract
     * @return the translated vector
     */
    public Vec2d sub(Vec2d other) {
        return new Vec2d(x - other.x, y - other.y);
    }

    /**
     * translates a vector in place by a positive constant
     * @param u constant to add to x-coord
     * @param v constant to add to y-coord
     */
    public void _add(double u, double v) {
        x += u;
        y += v;
    }

    /**
     * adds two vectors together in place
     * @param other vector to be added
     */
    public void _add(Vec2d other) {
        x -= other.x;
        y -= other.y;
    }

    /**
     * translation by neg constant in place
     * @param u const to be subtracted from x-coord
     * @param v const to be subtracted from y-coord
     */
    public void _sub(double u, double v) {
        x -= u;
        y -= v;
    }

    /**
     * subtract two vectors in place
     * @param other vector to be subtracted
     */
    public void _sub(Vec2d other) {
        x -= other.x;
        y -= other.y;
    }


    /**
     * checks if the x value of a vector is within given bounds
     * @param min min bound
     * @param max max bound
     * @return true if vector is within, false otherwise
     */
    public boolean isWithinX(double min, double max) {
        return isWithin(x, min, max);
    }

    /**
     * checks if the y value of a vector is within given bounds
     * @param min min bound
     * @param max max bound
     * @return true if vector is within, false otherwise
     */
    public boolean isWithinY(double min, double max) {
        return isWithin(y, min, max);
    }

    /**
     * checks if a vector is within 2 other vectors
     * @param min min bounding vector
     * @param max max bounding vector
     * @return true if vector is within, false otherwise
     */
    public boolean isWithin(Vec2d min, Vec2d max) {
        return isWithinX(min.getX(), max.getX()) && isWithinY(min.getY(), max.getY());
    }

    /**
     * Checks if a double is within given bounds
     * @param value int to check
     * @param min min bound
     * @param max max bound
     * @return true if value is within, false otherwise
     */
    private boolean isWithin(double value, double min, double max) {
        return min <= value && value <= max;
    }




    /**
     * Getter for x-coord of a vector
     * @return x-coord
     */
    public double getX() {
        return x;
    }

    /**
     * Setter for x-coord of a vector
     * @param x x-coord to be set
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Getter for y-coord of a vector
     * @return y-coord
     */
    public double getY() {
        return y;
    }

    /**
     * Setter for y-coord of a vector
     * @return y-coord to be set
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Setter for the x and y-coords for a vector
     * @param x x-coord
     * @param y y-coord
     */
    public void set(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Sets a vector to equal another
     * @param other vector to be equal to
     */
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
