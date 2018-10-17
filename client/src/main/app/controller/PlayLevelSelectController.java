package main.app.controller;

import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import main.app.Main;
import main.app.model.AppScreen;
import main.app.model.PlayLevelScreen;
import main.app.model.PlayModeSelectScreen;
import main.content.GameConfig;
import main.content.IntStat;
import main.content.LevelConfig;

import java.util.ArrayList;
import java.util.Iterator;

public class PlayLevelSelectController extends AppController {

    public PlayLevelSelectController(AppScreen screen) {
        super(screen);
    }

    @FXML
    public GridPane gridLevels;

    public ArrayList<Button> btnLevels;

    @FXML
    public void initialize() {

//        Gson gson = new Gson();

        GameConfig config = new GameConfig();

        Iterator<LevelConfig> it = config.levels();

        for (int i = 0; it.hasNext(); i++) {
            LevelConfig level = it.next();

            Button btn = new Button(level.getName());
            gridLevels.add(btn, i, 0);

            System.out.println(i + " " + level.getLevelNum());

            btn.setDisable(Main.gameConfig.getIntStat().getStat(IntStat.Key.MAX_LEVEL_CONQUERED).get() < i);
            btn.addEventHandler(MouseEvent.MOUSE_CLICKED, evt -> this.onLevelSelected(level.getFilename(), level.getLevelNum() + 1));
        }

        System.out.println(Main.gameConfig.getIntStat().getStat(IntStat.Key.MAX_LEVEL_CONQUERED).get());
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
