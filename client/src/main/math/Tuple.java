package main.math;

/**
 * Class which sets a generic 2-tuple
 * @param <X> x value
 * @param <Y> y value
 */
public class Tuple<X, Y> {

    private final X x;
    private final Y y;

    /**
     * Generic constructor
     * @param x x value
     * @param y y value
     */
    public Tuple(X x, Y y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Getter for the x value
     * @return x value
     */
    public X getX() {
        return x;
    }

    /**
     * Getter for the y value
     * @return y value
     */
    public Y getY() {
        return y;
    }
}
