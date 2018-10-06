package main.app.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import main.app.model.AppScreen;
import main.app.model.PlayLevelScreen;
import main.app.model.PlayModeSelectScreen;

import java.util.ArrayList;

public class PlayLevelSelectController extends AppController {

    public PlayLevelSelectController(AppScreen screen) {
        super(screen);
    }

    @FXML
    public GridPane gridLevels;

    public ArrayList<Button> btnLevels;

    @FXML
    public void initialize() {
        Button btnLevel1 = new Button("Level1");
        Button btnLevel2 = new Button("Level2");
        Button btnLevel3 = new Button("Level3");

        gridLevels.add(btnLevel1, 0, 0);
        gridLevels.add(btnLevel2, 1, 0);
        gridLevels.add(btnLevel3, 3, 0);

        btnLevel1.addEventHandler(MouseEvent.MOUSE_CLICKED, evt -> this.onLevelSelected("level01"));
        btnLevel2.addEventHandler(MouseEvent.MOUSE_CLICKED, evt -> this.onLevelSelected("level02"));
        btnLevel3.addEventHandler(MouseEvent.MOUSE_CLICKED, evt -> this.onLevelSelected("level03"));
    }

//    @FXML
//    public void onLevelBtnSelected() {
//        System.out.println("Play Level!");
//    }

    public void onLevelSelected(String filename) {
        switchScreen(new PlayLevelScreen(screen.getStage(), filename));
    }


    @FXML
    public void onBackBtnPressed() {
        switchScreen(new PlayModeSelectScreen(screen.getStage()));
    }
}
