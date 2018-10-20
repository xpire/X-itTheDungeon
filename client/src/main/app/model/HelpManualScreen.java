package main.app.model;

import javafx.stage.Stage;
import main.app.controller.AppController;
import main.app.controller.HelpManualController;

public class HelpManualScreen extends SubScreen{

    {
        fxmlName = "main/app/view/helpManual.fxml";
    }

    public HelpManualScreen(Stage stage, AppScreen parent) {
        super(stage, parent);
    }

    @Override
    protected AppController getController() {
        return new HelpManualController(this);
    }
}
