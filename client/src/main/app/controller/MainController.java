package main.app.controller;

import javafx.fxml.FXML;
import main.app.model.AppScreen;
import main.app.model.CreateModeSelectScreen;
import main.app.model.PlayModeSelectScreen;
import main.sound.SoundManager;

public class MainController extends AppController{

//    @FXML
//    private Button btnStart;
    SoundManager soundManager = SoundManager.getInstance(5);

    public MainController(AppScreen screen) {
        super(screen);
    }

//
//    @FXML
//    public void initialize() {
//
//    }
//
//
    @FXML
    public void onPlayBtnPressed() {
        switchScreen(new PlayModeSelectScreen(screen.getStage()));
        soundManager.playSoundEffect("Item");
    }

    @FXML
    public void onMakeBtnPressed() {
        switchScreen(new CreateModeSelectScreen(screen.getStage()));
        soundManager.playSoundEffect("Item");
    }
}