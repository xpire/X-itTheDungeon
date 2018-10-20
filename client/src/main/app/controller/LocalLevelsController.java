package main.app.controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import main.app.Main;
import main.app.model.AppScreen;
import main.app.model.PlayModeSelectScreen;
import main.client.util.LocalManager;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class LocalLevelsController extends AppController implements Initializable {
    public LocalLevelsController(AppScreen screen) { super(screen); }

    @FXML
    ListView localView;

    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Is the user logged in?
        if (Main.currClient.isLoggedin()) { localView.setPlaceholder(new Label("Sign in to download maps. ")); }
        else {
            ArrayList<String> localMaps = LocalManager.fetchLocal(Main.currClient.getLoggedUser());

            if (localMaps == null || localMaps.isEmpty()) { localView.setPlaceholder(new Label("There's no maps downloaded yet, go  some!!!")); }
            else {
                localView.getItems().addAll(localMaps);
                localView
                        .getSelectionModel()
                        .setSelectionMode(SelectionMode.SINGLE);
            }
        }
    }


    @FXML
    public void onBackBtnPressed() { switchScreen(new PlayModeSelectScreen(screen.getStage())); }

    @FXML
    public void onPlayBtnPressed() {
        // Providing the text of the map
        ObservableList<String> maps = localView.getSelectionModel().getSelectedItems();


        // Lanuch the game
    }
}
