package main.app.model;

import javafx.stage.Stage;
import main.app.controller.AppController;
import main.app.controller.PlayModeSelectController;

/**
 * Screen for the Play Mode
 */
public class PlayModeSelectScreen extends AppScreen{

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
