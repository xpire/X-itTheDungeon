package main.app.model;

import javafx.stage.Stage;
import main.app.Main;
import main.app.controller.AppController;
import main.app.controller.ExploreModeController;

public class ExploreModeScreen extends AppScreen {

    {
        title = "Explore Global maps";
        fxmlName = !Main.currClient.isLoggedin() ? "main/app/view/exploreLocked.fxml" :  "main/app/view/exploreMode.fxml";
    }

    public ExploreModeScreen(Stage stage) { super(stage); }

    @Override
    protected AppController getController() { return new ExploreModeController(this); }

}
