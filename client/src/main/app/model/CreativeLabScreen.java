package main.app.model;

import javafx.stage.Stage;
import main.app.controller.AppController;
import main.app.controller.CreativeLabController;

public class CreativeLabScreen extends AppScreen {

    {
        title = "X-it the Dungeon";
        fxmlName = "main/app/view/creativeLab.fxml";
    }

    public CreativeLabScreen(Stage stage) {
        super(stage);
    }

    @Override
    protected AppController getController() {
        return new CreativeLabController(this);
    }

}
