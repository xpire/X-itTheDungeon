package main.app.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import main.app.Main;
import main.app.engine.AlertHelper;
import main.app.model.AppScreen;
import main.app.model.PlayModeSelectScreen;
import main.client.util.LocalManager;
import main.sound.SoundManager;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class LocalLevelsController extends AppController {
    public LocalLevelsController(AppScreen screen) { super(screen); }

    private SoundManager soundManager = SoundManager.getInstance(5);

    @FXML
    Accordion localView;

    @FXML
    public void initialize() {
        ArrayList<LocalManager.LocalStructure> localMaps;
        // Is the user logged in?
        if (!Main.currClient.isLoggedin()) {
            AlertHelper.showAlert(Alert.AlertType.ERROR,"Error","Please login to see downloaded maps.");
            return;
        }
        else { localMaps = LocalManager.fetchLocal(Main.currClient.getLoggedUser()); }

        if (localMaps == null || localMaps.isEmpty()) {
            //TODO handle the return message
        }
        else {
            for (LocalManager.LocalStructure x: localMaps) {
                TitledPane curr = new TitledPane();
                curr.setText(x.username);
                Button Downloadbtn = new Button();

                Downloadbtn.setOnAction(e -> {
                    soundManager.playSoundEffect("Item");
                    String map = x.mapContent;
                    // TODO launch the play test mode
                });

                Downloadbtn.setText("Play this map.");
                VBox box = new VBox();
                box.getChildren().addAll(new Label("Author: " + x.username), Downloadbtn);
                curr.setContent(box);

                localView.getPanes().add(curr);
            }
        }
    }

    @FXML
    public void onBackBtnPressed() {
        switchScreen(new PlayModeSelectScreen(screen.getStage()));
        soundManager.playSoundEffect("Item");
    }
}
