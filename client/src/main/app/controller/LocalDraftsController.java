package main.app.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import main.app.Main;
import main.app.model.AppScreen;
import main.app.model.PlayModeSelectScreen;
import main.client.util.LocalManager;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class LocalDraftsController extends AppController implements Initializable {

    public LocalDraftsController(AppScreen screen) { super(screen); }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ArrayList<LocalManager.LocalStructure> currData = new ArrayList<>();

        // If the user has logged in ?
        if (Main.currClient.isLoggedin()) {
            currData = LocalManager.fetchLocalDraft(Main.currClient.getLoggedUser());
        }
        else {

        }
    }

    @FXML
    public void onBackBtnPressed() { switchScreen(new PlayModeSelectScreen(screen.getStage())); }
}
