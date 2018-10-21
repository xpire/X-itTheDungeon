package main.app.model;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import main.app.controller.AppController;
import main.app.controller.MainController;
import main.app.controller.SettingController;

public class SettingScreen extends SubScreen{

    {
        title = "X-it the Dungeon";
        fxmlName = "main/app/view/setting.fxml";
    }

    public SettingScreen(Stage stage, AppScreen parent) {
        super(stage, parent);
    }


    @Override
    protected AppController getController() {
        return new SettingController(this);
    }
}
