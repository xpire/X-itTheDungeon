package main.app.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import main.app.Main;
import main.app.engine.AlertHelper;
import main.app.model.AppScreen;
import main.app.model.PlayModeSelectScreen;
import main.client.structure.ReqStructure;
import main.client.util.LocalManager;
import main.sound.SoundManager;

import java.util.ArrayList;

/**
 * Controller for the ExploreMode screen
 */
public class ExploreModeController extends AppController {

    private SoundManager soundManager = SoundManager.getInstance(5);

    /**
     * Generic constructor
     * @param screen : the corresponding screen
     */
    public ExploreModeController(AppScreen screen) { super(screen); }

    @FXML
    public Accordion headerList;

    @FXML
    public void initialize() {
        if (Main.currClient.isLoggedin()) {

            String pull = Main.currClient.pullGlobalMaps();

            System.out.println(pull);

            if (pull == null || pull.isEmpty()) {return; }

            ArrayList<ReqStructure> globMaps = new Gson()
                    .fromJson(pull,
                            new TypeToken<ArrayList<ReqStructure>>() {
                            }.getType()
                    );

            if (globMaps == null || globMaps.isEmpty()) {
                AlertHelper.showAlert(Alert.AlertType.ERROR,"Error","There's no maps downloaded yet, go download some.");

            }

            for (ReqStructure x: globMaps) {
                    TitledPane curr = new TitledPane();
                    curr.setText(x.mapname);
                    Button Downloadbtn = new Button();
                    Downloadbtn.setOnAction(e -> {
                        soundManager.playSoundEffect("Item");
                        if (LocalManager.hasMapLocal(x, "downloads")) { AlertHelper.showAlert(Alert.AlertType.INFORMATION,"Message","You already have this map!!!"); }
                        else {
                            // Add to local maps
                            LocalManager.addMap(x, Main.currClient.getLoggedUser());
                            AlertHelper.showAlert(Alert.AlertType.INFORMATION,"Message", "Map downloaded!!!");
                        }
                    });

                    Downloadbtn.setText("Download this map.");
                    VBox box = new VBox();
                    box.getChildren().addAll(new Label("Author: " + x.name), Downloadbtn);
                    curr.setContent(box);

                    headerList.getPanes().add(curr);
            }
        }
    }

    @FXML
    public void onBackBtnPressed() {
        switchScreen(new PlayModeSelectScreen(screen.getStage()));
        soundManager.playSoundEffect("Item");
    }
}
