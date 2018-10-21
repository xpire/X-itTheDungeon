package main.app.model;

import javafx.stage.Stage;
import main.app.controller.AppController;
import main.app.controller.SettingController;

/**
 * Screen for the settings page
 */
public class SettingScreen extends SubScreen{

    {
        title = "X-it the Dungeon";
        fxmlName = "main/app/view/setting.fxml";
    }

    /**
     * Generic constructor
     * @param stage : corresponding stage
     * @param parent : parent screen
     */
    public SettingScreen(Stage stage, AppScreen parent) {
        super(stage, parent);
    }

    @Override
    protected AppController getController() {
        return new SettingController(this);
    }
}
