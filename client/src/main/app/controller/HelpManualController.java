package main.app.controller;

import javafx.fxml.FXML;
import main.app.model.HelpManualScreen;

public class HelpManualController extends AppController<HelpManualScreen> {

    public HelpManualController(HelpManualScreen screen) {
        super(screen);
    }

    @FXML
    public void onBackBtnPressed() {
        switchScreen(screen.getParent());
    }
}
