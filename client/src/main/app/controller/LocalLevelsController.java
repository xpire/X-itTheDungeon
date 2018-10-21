package main.app.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import main.app.Main;
import main.app.engine.AlertHelper;
import main.app.model.AppScreen;
import main.app.model.PlayLevelScreen;
import main.app.model.PlayModeSelectScreen;
import main.client.util.LocalManager;
import main.sound.SoundManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class LocalLevelsController extends AppController {
    public LocalLevelsController(AppScreen screen) { super(screen); }

    private SoundManager soundManager = SoundManager.getInstance(5);

    public static String PATH = "src/asset/buffer/";

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
                Button playBtn = new Button();
                playBtn.setText("Play this map.");

                playBtn.setOnAction(e -> {

                    String map = x.mapContent;
                    System.out.println(x.mapContent);

                    try (PrintWriter writer = new PrintWriter(new File(PATH))){ writer.print(map); }
                    catch (IOException s) { s.printStackTrace(); }

                    switchScreen(new PlayLevelScreen(
                            screen,
                            screen.getStage(),
                            "buffer",
                            PATH,
                            0 ,
                            false
                    ));
                });


                VBox box = new VBox();
                box.getChildren().addAll(new Label("Author: " + x.username), playBtn);
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
