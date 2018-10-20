package main.app.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import main.app.Main;
import main.app.engine.AlertHelper;
import main.app.model.AppScreen;
import main.app.model.PlayModeSelectScreen;
import main.client.structure.ReqStructure;
import main.client.util.LocalManager;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ExploreModeController extends AppController implements Initializable {

    public ExploreModeController(AppScreen screen) { super(screen); }

    @FXML
    public ListView<String> listOfHeader;

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
            ArrayList<String> render = globMaps
                    .stream()
                    .map(ReqStructure::toString)
                    .collect(Collectors.toCollection(ArrayList::new));

            ObservableList<String> buffer = FXCollections.observableArrayList(render);
            // Handle the empty map case
            if (globMaps.isEmpty()) {
                listOfHeader.setPlaceholder(new Label("There are currently no map xD, upload some!!!"));
            }
            else {
                listOfHeader.setItems(buffer);

                listOfHeader
                        .getSelectionModel()
                        .setSelectionMode(SelectionMode.SINGLE);
            }
        }
    }

    @FXML
    public void onBackBtnPressed() { switchScreen(new PlayModeSelectScreen(screen.getStage())); }

    @FXML
    public void onDownloadBtnPressed() {
        // One element only
        ObservableList<String> maps = listOfHeader.getSelectionModel().getSelectedItems();

        if (LocalManager.hasMapLocal(maps.get(0), Main.currClient.getLoggedUser())) { AlertHelper.showAlert(Alert.AlertType.INFORMATION,"Message","You already have this map!!!"); }
        else {
            // Add to local maps
            LocalManager.addMap(maps.get(0), Main.currClient.getLoggedUser());
            AlertHelper.showAlert(Alert.AlertType.INFORMATION,"Message", "Map downloaded!!!");
        }
    }
}
