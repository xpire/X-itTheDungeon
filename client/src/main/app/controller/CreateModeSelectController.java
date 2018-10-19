package main.app.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import main.app.model.CreateModeSelectScreen;
import main.app.model.CreativeLabScreen;
import main.app.model.MainScreen;
import main.sound.SoundManager;

public class CreateModeSelectController extends AppController<CreateModeSelectScreen>{

    @FXML
    private Accordion draftsView;

    SoundManager soundManager = SoundManager.getInstance(5);

    public CreateModeSelectController(CreateModeSelectScreen screen) {
        super(screen);
    }

    @FXML
    public void initialize() {
        screen.initialiseDraftList(draftsView);
    }

    @FXML
    public void onEnterLabBtnPressed() {
        switchScreen(new CreativeLabScreen(screen.getStage()));
        soundManager.playSoundEffect("Item");

        screen.initialiseNewDraft();
    }

    @FXML
    public void onBackBtnPressed() {
        switchScreen(new MainScreen(screen.getStage()));
        soundManager.playSoundEffect("Item");
    }

}
