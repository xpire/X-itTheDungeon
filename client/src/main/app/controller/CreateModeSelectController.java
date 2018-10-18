package main.app.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import main.app.model.CreateModeSelectScreen;
import main.app.model.CreativeLabScreen;
import main.app.model.MainScreen;

public class CreateModeSelectController extends AppController<CreateModeSelectScreen>{

    @FXML
    private Accordion draftsView;

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
    }

    @FXML
    public void onBackBtnPressed() {
        switchScreen(new MainScreen(screen.getStage()));
    }

}
