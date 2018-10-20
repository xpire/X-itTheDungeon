package main.app.model;

import javafx.stage.Stage;
import main.app.controller.AppController;
import main.app.controller.LocalLevelsController;

public class LocalLevelScreen extends AppScreen {

    {
        title = "Local Levels";
        fxmlName = "main/app/view/localLevel.fxml";
    }
    public LocalLevelScreen(Stage stage) {
        super(stage);
    }

    @Override
    protected AppController getController() { return new LocalLevelsController(this); }
}
