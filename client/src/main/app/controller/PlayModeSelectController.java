package main.app.controller;

import javafx.fxml.FXML;
import main.app.model.AppScreen;
import main.app.model.MainScreen;
import main.app.model.PlayLevelSelectScreen;
import main.sound.SoundManager;
import main.app.model.*;

/**
 * Controller for the PlayMode Select screen
 */
public class PlayModeSelectController extends AppController {

    private SoundManager soundManager = SoundManager.getInstance(5);

    /**
     * Generic constructor
     * @param screen : the corresponding screen
     */
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

    @FXML
    public void onExplorePressed() {
        switchScreen(new ExploreModeScreen(screen.getStage()));
        soundManager.playSoundEffect("Item");
    }

    @FXML
    public void onLocalBtnPressed() {
        switchScreen(new LocalLevelScreen(screen.getStage()));
        soundManager.playSoundEffect("Item");
    }

    @FXML
    public void onDraftBtnPressed() {
        switchScreen(new LocalDraftsScreen(screen.getStage()));
        soundManager.playSoundEffect("Item");
    }
}