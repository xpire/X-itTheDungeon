package main.app.model;

import javafx.stage.Stage;
import main.app.controller.AppController;
import main.app.controller.MainController;
import main.app.controller.TrophyController;

public class TrophyScreen extends AppScreen{

    {
        fxmlName = "main/app/view/trophy.fxml";
    }

    public TrophyScreen(Stage stage) {
        super(stage);
    }


    @Override
    protected AppController getController() {
        return new TrophyController(this);
    }
}
