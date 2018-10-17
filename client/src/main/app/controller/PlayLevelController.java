package main.app.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import main.app.model.AppScreen;
import main.app.model.PlayModeSelectScreen;

public class PlayLevelController extends AppController {


    @FXML
    private Pane dynamicLayer;

    public PlayLevelController(AppScreen screen) {
        super(screen);
    }


    @FXML
    public void initialize() {
    }

    @FXML
    public void onBackBtnPressed() {
        switchScreen(new PlayModeSelectScreen(screen.getStage()));
    }

    public Pane getDynamicLayer() {
        return dynamicLayer;
    }
}
