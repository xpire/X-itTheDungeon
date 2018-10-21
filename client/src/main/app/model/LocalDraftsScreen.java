package main.app.model;

import javafx.stage.Stage;
import main.app.controller.AppController;
import main.app.controller.LocalDraftsController;

/**
 * Screen of the local drafts page
 */
public class LocalDraftsScreen extends AppScreen{

    {
        title = "Local Drafts";
        fxmlName = "main/app/view/localDrafts.fxml";
    }

    /**
     * Generic constructor
     * @param stage : corresponding stage
     */
    public LocalDraftsScreen(Stage stage) { super(stage); }

    @Override
    protected AppController getController() { return new LocalDraftsController(this); }
}
