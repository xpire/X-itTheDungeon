package main.app.controller;

import javafx.fxml.FXML;
import main.app.model.HelpManualScreen;
import main.sound.SoundManager;

public class HelpManualController extends AppController<HelpManualScreen> {

    private SoundManager soundManager = SoundManager.getInstance(5);

    public HelpManualController(HelpManualScreen screen) {
        super(screen);
    }

    @FXML
    public void onBackBtnPressed() {
        switchScreen(screen.getParent());
        soundManager.playSoundEffect("Item");
    }
}
