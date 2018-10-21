package main.app.model;

import javafx.stage.Stage;
import main.app.controller.AppController;
import main.app.controller.MainController;

/**
 * Screen for the main page
 */
public class MainScreen extends AppScreen{

    {
        title = "X-it the Dungeon";
        fxmlName = "main/app/view/main.fxml";
    }

    /**
     * Generic constructor
     * @param stage : corresponding stage
     */
    public MainScreen(Stage stage) {
        super(stage);
    }

    @Override
    protected AppController getController() {
        return new MainController(this);
    }
}
