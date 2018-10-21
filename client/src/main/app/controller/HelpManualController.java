package main.app.controller;

import javafx.fxml.FXML;
import main.app.model.HelpManualScreen;
import main.sound.SoundManager;

/**
 * Controller for the Help Manual screen
 */
public class HelpManualController extends AppController<HelpManualScreen> {

    private SoundManager soundManager = SoundManager.getInstance(5);

    /**
     * Generic constructor
     * @param screen : the corresponding screen
     */
    public HelpManualController(HelpManualScreen screen) {
        super(screen);
    }

    @FXML
    public void onBackBtnPressed() {
        switchScreen(screen.getParent());
        soundManager.playSoundEffect("Item");
    }
}
