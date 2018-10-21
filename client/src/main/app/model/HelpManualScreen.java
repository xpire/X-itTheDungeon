package main.app.model;

import javafx.stage.Stage;
import main.app.controller.AppController;
import main.app.controller.HelpManualController;

/**
 * Screen for the helpManual screen
 */
public class HelpManualScreen extends SubScreen{

    {
        fxmlName = "main/app/view/helpManual.fxml";
    }

    /**
     * Generic constructor
     * @param stage : corresponding stage
     * @param parent : the parent of the stage
     */
    public HelpManualScreen(Stage stage, AppScreen parent) {
        super(stage, parent);
    }

    @Override
    protected AppController getController() {
        return new HelpManualController(this);
    }
}
