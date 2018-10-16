package main.math;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


/**
 * 'Generic' Vector to Integer class which represents a 2-tuple for convenience
 * in the rest of the code base
 */
public class Vec2i {

    public static final Vec2i NORTH = new Vec2i(0, -1);
    public static final Vec2i SOUTH = new Vec2i(0, 1);
    public static final Vec2i WEST  = new Vec2i(-1, 0);
    public static final Vec2i EAST  = new Vec2i(1, 0);
    public static final List<Vec2i> DIRECTIONS =
            Collections.unmodifiableList(Arrays.asList(NORTH, SOUTH, WEST, EAST));


    private int x;
    private int y;


    /**
     * Default constructor, sets the 0 vector
     */
    public Vec2i() {
        this(0, 0);
    }

    /**
     * Constructor which sets a specific 2-tuple
     * @param x x coord
     * @param y y coord
     */
    public Vec2i(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Constructor which copies another Vec2i
     * @param other Vec2i to be copied
     */
    public Vec2i(Vec2i other) {
        this.x = other.x;
        this.y = other.y;
    }

    /**
     * translates a vector by a positive constant amount
     * @param u constant to add to x-coord
     * @param v constant to add to y-coord
     * @return the translated vector
     */
    public Vec2i add(int u, int v) {
        return new Vec2i(x + u, y + v);
    }

    /**
     * Adds 2 vectors together
     * @param other Vector to be added to current
     * @return the translated vector
     */
    public Vec2i add(Vec2i other) {
        return new Vec2i(x + other.x, y + other.y);
    }

    /**
     * translates a vector by a negative constant amount
     * @param u constant to sub from x-coord
     * @param v constant to sub from y-coord
     * @return the translated vector
     */
    public Vec2i sub(int u, int v) {
        return new Vec2i(x - u, y - v);
    }

    /**
     * subtracts a vector from another
     * @param other the vector to subtract
     * @return the translated vector
     */
    public Vec2i sub(Vec2i other) {
        return new Vec2i(x - other.x, y - other.y);
    }

    /**
     * Checks if an int is within given bounds
     * @param value int to check
     * @param min min bound
     * @param max max bound
     * @return true if value is within, false otherwise
     */
    private boolean within(int value, int min, int max) {
        return min <= value && value <= max;
    }


    /**
     * checks if the x value of a vector is within given bounds
     * @param min min bound
     * @param max max bound
     * @return true if vector is within, false otherwise
     */
    public boolean withinX(int min, int max) {
        return within(x, min, max);
    }

    /**
     * checks if the y value of a vector is within given bounds
     * @param min min bound
     * @param max max bound
     * @return true if vector is within, false otherwise
     */
    public boolean withinY(int min, int max) {
        return within(y, min, max);
    }

    /**
     * checks if a vector is within 2 other vectors
     * @param min min bounding vector
     * @param max max bounding vector
     * @return true if vector is within, false otherwise
     */
    public boolean within(Vec2i min, Vec2i max) {
        return withinX(min.x, max.x) && withinY(min.y, max.y);
    }

    /**
     * Getter for x-coord of a vector
     * @return x-coord
     */
    public int getX() {
        return x;
    }

    /**
     * Getter for y-coord of a vector
     * @return y-coord
     */
    public int getY() {
        return y;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vec2i)) return false;

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

    public int norm1() {
        return Math.abs(x) + Math.abs(y);
    }

    public double length() {
        return Math.sqrt(x * x + y * y);
    }

    /**
     * Calculates the Manhattan Distance of 2 vectors
     *
     * @param other Second coordinate
     * @return the Manhattan distance from 2 nodes on the grid
     */
    public int manhattan(Vec2i other) { return Math.abs(other.x - this.x) + Math.abs(other.y - this.y); } // TODO

    public boolean isAdjacent(Vec2i other) {
        return this.sub(other).norm1() <= 1;
    }

    public boolean isDirection() {
        return norm1() == 1;
    }

//    public boolean isNorthOf(Vec2i other) {
//        return this.sub(other).equals(NORTH);
//    }
//
//    public boolean isSouthOf(Vec2i other) {
//        Vec2i dir = this.sub(other);
//        return dir.getX() == 0 && dir.getY() < 0;
//    }
//
//    public boolean isWestOf(Vec2i other) {
//        return this.sub(other).equals(WEST);
//    }
//
//    public boolean isEastOf(Vec2i other) {
//        return this.sub(other).equals(EAST);
//    }
}
