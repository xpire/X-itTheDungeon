package main.app.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import main.app.Main;
import main.app.engine.AlertHelper;
import main.app.model.AppScreen;
import main.app.model.PlayModeSelectScreen;
import main.client.structure.ReqStructure;
import main.client.util.LocalManager;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ExploreModeController extends AppController implements Initializable {

    public ExploreModeController(AppScreen screen) { super(screen); }

    @FXML
    public Accordion headerList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (Main.currClient.isLoggedin()) {
            ArrayList<ReqStructure> globMaps = new Gson()
                    .fromJson(Main
                                    .currClient
                                    .pullGlobalMaps(),
                            new TypeToken<ArrayList<ReqStructure>>()
                            {}.getType()
                    );

            for (ReqStructure x: globMaps) {
                    TitledPane curr = new TitledPane();
                    curr.setText(x.mapname);
                    Button Downloadbtn = new Button();
                    Downloadbtn.setOnAction(e -> {
                        if (LocalManager.hasMapLocal(x)) { AlertHelper.showAlert(Alert.AlertType.INFORMATION,"Message","You already have this map!!!"); }
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
    public void onBackBtnPressed() { switchScreen(new PlayModeSelectScreen(screen.getStage())); }
}
