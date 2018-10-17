package main.app.model;

import javafx.stage.Stage;
import main.app.controller.AppController;
import main.app.controller.CreateModeSelectController;

public class CreateModeSelectScreen extends AppScreen{

    {
        title = "Select Create Mode";
        fxmlName = "main/app/view/createModeSelect.fxml";
    }

    public CreateModeSelectScreen(Stage stage) {
        super(stage);
    }

    @Override
    protected AppController getController() {
        return new CreateModeSelectController(this);
    }

}
