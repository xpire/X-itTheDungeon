package main.app.model;

import javafx.stage.Stage;
import main.app.controller.AppController;
import main.app.controller.LocalDraftsController;

public class LocalDraftsScreen extends AppScreen{

    {
        title = "Local Drafts";
        fxmlName = "main/app/view/exploreLocked.fxml";
    }

    public LocalDraftsScreen(Stage stage) {super(stage); }

    @Override
    protected AppController getController() { return new LocalDraftsController(this); }
}
