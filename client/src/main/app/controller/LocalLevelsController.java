package main.app.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import main.app.Main;
import main.app.engine.AlertHelper;
import main.app.model.AppScreen;
import main.app.model.LocalLevelScreen;
import main.app.model.PlayLevelScreen;
import main.app.model.PlayModeSelectScreen;
import main.client.util.LocalManager;
import main.sound.SoundManager;

import java.io.*;
import java.util.ArrayList;

/**
 * Controller for the LocalLevels screen
 */
public class LocalLevelsController extends AppController {

    /**
     * Generic constructor
     * @param screen : the corresponding screen
     */
    public LocalLevelsController(AppScreen screen) { super(screen); }

    private SoundManager soundManager = SoundManager.getInstance(5);

    private static String PATH = "src/asset/buffer/";

    @FXML
    private Accordion localView;

    @FXML
    public void initialize() {
        ArrayList<LocalManager.LocalStructure> localMaps;

        if (!Main.currClient.isLoggedin()) {
            AlertHelper.showAlert(Alert.AlertType.ERROR,"Error","Please login to see downloaded maps.");
            return;
        }
        else { localMaps = LocalManager.fetchLocal(Main.currClient.getLoggedUser()); }
        if (localMaps == null || localMaps.isEmpty()) {
            AlertHelper.showAlert(Alert.AlertType.ERROR,"Error","There's no maps downloaded yet, go download some.");
        }
        else {
            for (LocalManager.LocalStructure x: localMaps) {
                TitledPane curr = new TitledPane();
                curr.setText(x.mapname);
                Button playBtn = new Button();
                playBtn.setText("Play this map.");

                playBtn.setOnAction(e -> {

                    String map = x.mapContent;
                    System.out.println(x.mapContent);

                    File f = new File(PATH + "buffer.txt");
                    try {
                        PrintWriter writer = new PrintWriter(f);
                        writer.print("");
                        writer.close();

                        FileWriter writer2 = new FileWriter(f);
                        writer2.write(map);
                        writer2.close();
                    }
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

                Button deleteBtn = new Button();
                deleteBtn.setText("Delete this map");

                deleteBtn.setOnAction(e -> {
                    LocalManager.delMap(x);
                    AlertHelper.showAlert(Alert.AlertType.INFORMATION,"Message", "Map deleted");
                    switchScreen(new LocalLevelScreen(screen.getStage()));
                });


                VBox box = new VBox();
                box.getChildren().addAll(new Label("Author: " + x.username), playBtn, deleteBtn);
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
