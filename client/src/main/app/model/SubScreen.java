package main.app.model;

import javafx.stage.Stage;
import main.app.controller.AppController;
import main.app.controller.TrophyController;

public abstract class SubScreen extends AppScreen{

    private AppScreen parent;

    public SubScreen(Stage stage, AppScreen parent) {
        super(stage);
        this.parent = parent;
    }

    public AppScreen getParent() {
        return parent;
    }
}
