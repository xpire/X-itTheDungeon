package main.core;

import javafx.scene.Node;

public class ViewComponent extends Component {

    private Node view;

    public ViewComponent(Node view) {
        this.view = view;
    }

    public void setView(Node view) {
        this.view = view;
    }

    public Node getView() {
        return view;
    }

    public void setPosition(double x, double y) {
        view.setTranslateX(x);
        view.setTranslateY(y);
    }
}
