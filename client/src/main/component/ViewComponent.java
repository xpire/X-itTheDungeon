package main.component;

import javafx.scene.Group;
import javafx.scene.Node;
import main.math.Vec2d;


/**
 * class describing the graphical component of the world
 */
public class ViewComponent {

    private Group view;
    private Vec2d centre = new Vec2d(0, 0);

    /**
     * basic constructor
     */
    public ViewComponent() {
        view = new Group();
    }

    public ViewComponent(Node node) {
        this();
        addNode(node);
    }

    /**
     * add to the view
     * @param node node to be added
     */
    public void addNode(Node node) {
        view.getChildren().add(node);
    }

    /**
     * remove from the view
     * @param node node to be removed
     * @return the removed node
     */
    public boolean removeNode(Node node) {
        return view.getChildren().remove(node);
    }

    /**
     * getter for the view
     * @return the view
     */
    public Group getView() {
        return view;
    }

    /**
     * translates the view to a specified screen position,
     * relative to its parent
     * - by a constant amount
     * @param x - change in x
     * @param y - change in y
     */
    public void moveTo(double x, double y) {
        view.setTranslateX(x);
        view.setTranslateY(y);
    }

    /**
     * as above but by a vector
     * @param pos : vector to change by
     */
    public void moveTo(Vec2d pos) {
        moveTo(pos.getX(), pos.getY());
    }

    /**
     * getter for the centre of the view
     * @return centre vector
     */
    public Vec2d getCentre() {
        return centre;
    }

    /**
     * setter for the centre of the view
     * @param centre centre vector
     */
    public void setCentre(Vec2d centre) {
        this.centre = centre;
    }
}
