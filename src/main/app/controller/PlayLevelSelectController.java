package main.app.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import main.app.Main;
import main.app.model.AppScreen;
import main.app.model.PlayLevelScreen;
import main.app.model.PlayModeSelectScreen;
import main.stat.StatisticType;

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

        ArrayList<LevelWrapper> levels = new ArrayList<>();
        levels.add(new LevelWrapper("Level 1", "level01"));
        levels.add(new LevelWrapper("Level 2", "level02"));
        levels.add(new LevelWrapper("Level 3", "level03"));

        for (int i = 0; i < levels.size(); i++) {
            LevelWrapper level = levels.get(i);

            Button btn = new Button(level.levelName);
            gridLevels.add(btn, i, 0);

            btn.setDisable(Main.stats.getStat(StatisticType.MAX_LEVEL_CONQUERED).get() < i);
            btn.addEventHandler(MouseEvent.MOUSE_CLICKED, evt -> this.onLevelSelected(level.fileName, levels.indexOf(level) + 1));
        }
    }

    private class LevelWrapper {

        private String levelName;
        private String fileName;

        public LevelWrapper(String levelName, String fileName){
            this.levelName = levelName;
            this.fileName = fileName;
        }
    }

//    @FXML
//    public void onLevelBtnSelected() {
//        System.out.println("Play Level!");
//    }

    public void onLevelSelected(String filename, int levelNum) {
        switchScreen(new PlayLevelScreen(screen.getStage(), filename, levelNum));
    }


    @FXML
    public void onBackBtnPressed() {
        switchScreen(new PlayModeSelectScreen(screen.getStage()));
    }
}
