package main.app.controller;

import javafx.fxml.FXML;
import main.app.model.AppScreen;
import main.app.model.MainScreen;
import main.app.model.PlayLevelSelectScreen;

public class PlayModeSelectController extends AppController {

    public PlayModeSelectController(AppScreen screen) {
        super(screen);
    }

    @FXML
    public void initialize() {

    }

    @FXML
    public void onStoryBtnPressed() {
        switchScreen(new PlayLevelSelectScreen(screen.getStage()));
    }

    @FXML
    public void onBackBtnPressed() {
        switchScreen(new MainScreen(screen.getStage()));
    }

}