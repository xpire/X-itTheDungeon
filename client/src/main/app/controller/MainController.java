package main.app.controller;

import javafx.fxml.FXML;
import main.app.model.CreateModeSelectScreen;
import main.app.model.MainScreen;
import main.app.model.LoginScreen;
import main.app.model.PlayModeSelectScreen;
import main.app.model.TrophyScreen;
import main.sound.SoundManager;

public class MainController extends AppController<MainScreen> {

//    @FXML
//    private Button btnStart;
    SoundManager soundManager = SoundManager.getInstance(5);

    public MainController(MainScreen screen) {
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

    @FXML
    public void onTrophyBtnPressed() {
        switchScreen(new TrophyScreen(screen.getStage()));
    }

    @FXML
    public void onLoginBtnPressed() { switchScreen(new LoginScreen(screen.getStage())); }
}