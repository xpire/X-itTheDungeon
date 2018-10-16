package main.app.model;

import javafx.stage.Stage;
import main.app.controller.AppController;
import main.app.controller.PlayModeSelectController;

public class PlayModeSelectScreen extends AppScreen{

    //TODO: ask deep about whether this is okay, or we should
    // TODO: enforce this by making an abstract getter like getController
    // TODO: or store getController in a variable and reuse it
    {
        title = "Select Play Mode";
        fxmlName = "main/app/view/playModeSelect.fxml";
    }

    public PlayModeSelectScreen(Stage stage) {
        super(stage);
    }

    @Override
    protected AppController getController() {
        return new PlayModeSelectController(this);
    }

}
