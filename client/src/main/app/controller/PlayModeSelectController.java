package main.app.controller;

import javafx.fxml.FXML;
import main.app.model.AppScreen;
import main.app.model.MainScreen;
import main.app.model.PlayLevelSelectScreen;
import main.sound.SoundManager;

public class PlayModeSelectController extends AppController {

    SoundManager soundManager = SoundManager.getInstance(5);

    public PlayModeSelectController(AppScreen screen) {
        super(screen);
    }

    @FXML
    public void initialize() {

    }

    @FXML
    public void onStoryBtnPressed() {
        switchScreen(new PlayLevelSelectScreen(screen.getStage()));
        soundManager.playSoundEffect("Item");
    }

    @FXML
    public void onBackBtnPressed() {
        switchScreen(new MainScreen(screen.getStage()));
        soundManager.playSoundEffect("Item");
    }

}