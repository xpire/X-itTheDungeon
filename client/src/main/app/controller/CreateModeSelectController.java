package main.app.controller;

import javafx.fxml.FXML;
import main.app.model.AppScreen;
import main.app.model.CreativeLabScreen;
import main.app.model.MainScreen;

public class CreateModeSelectController extends AppController{

    public CreateModeSelectController(AppScreen screen) {
        super(screen);
    }

    @FXML
    public void initialize() {

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
