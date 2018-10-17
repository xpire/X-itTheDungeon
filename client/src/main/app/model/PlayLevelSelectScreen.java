package main.app.model;

import javafx.stage.Stage;
import main.app.controller.AppController;
import main.app.controller.PlayLevelSelectController;

public class PlayLevelSelectScreen extends AppScreen{

    {
        title = "Select Play Mode";
        fxmlName = "main/app/view/playLevelSelect.fxml";
    }

    public PlayLevelSelectScreen(Stage stage) {
        super(stage);
    }

    @Override
    protected AppController getController() {
        return new PlayLevelSelectController(this);
    }
}
