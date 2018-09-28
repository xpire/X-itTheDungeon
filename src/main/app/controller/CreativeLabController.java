package main.app.controller;

import javafx.fxml.FXML;
import main.app.model.AppScreen;
import main.app.model.CreateModeSelectScreen;

public class CreativeLabController extends AppController {

    private String selectedEntity;

    public CreativeLabController(AppScreen screen) {
        super(screen);
    }

    @FXML
    public void initialize() {
    }

    @FXML
    public void onTileSelection() {

    }

    @FXML
    public void onEntitySelection() {

    }

    @FXML
    public void onExitBtnPressed() {
        switchScreen(new CreateModeSelectScreen(screen.getStage()));
    }

}
