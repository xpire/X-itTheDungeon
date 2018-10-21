package main.app.model;

import javafx.stage.Stage;
import main.app.controller.AppController;
import main.app.controller.TrophyController;

/**
 * Screen for the trophy page
 */
public class TrophyScreen extends AppScreen{

    {
        fxmlName = "main/app/view/trophy.fxml";
    }

    /**
     * Generic constructor
     * @param stage : the corresponding stage
     */
    public TrophyScreen(Stage stage) {
        super(stage);
    }


    @Override
    protected AppController getController() {
        return new TrophyController(this);
    }
}
