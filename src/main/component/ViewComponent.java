package main.component;

import javafx.scene.Group;
import javafx.scene.Node;
import main.math.Vec2d;

public class ViewComponent extends Component {

    private Group view;
    private Vec2d centre = new Vec2d(0, 0);

    public ViewComponent() {
        view = new Group();
    }

    public ViewComponent(Node node) {
        this();
        addNode(node);
    }

    public void addNode(Node node) {
        view.getChildren().add(node);
    }

    public boolean removeNode(Node node) {
        return view.getChildren().remove(node);
    }

    public Node getView() {
        return view;
    }

    public void moveTo(double x, double y) {
        view.setTranslateX(x);
        view.setTranslateY(y);
    }

    public void moveTo(Vec2d pos) {
        moveTo(pos.getX(), pos.getY());
    }

    public Vec2d getCentre() {
        return centre;
    }

    public void setCentre(Vec2d centre) {
        this.centre = centre;
    }
}
