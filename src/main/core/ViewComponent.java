package main.core;

import javafx.scene.Group;
import javafx.scene.Node;
import main.math.Vec2d;

public class ViewComponent extends Component {

    private Group view;

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

    public Node getView() {
        return view;
    }


    public void setPosition(double x, double y) {
        view.setTranslateX(x);
        view.setTranslateY(y);
    }

    public void moveTo(Vec2d pos) {
        view.setTranslateX(pos.getX());
        view.setTranslateY(pos.getY());
    }
}
