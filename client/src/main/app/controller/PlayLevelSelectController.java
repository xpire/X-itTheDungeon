package main.app.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import main.app.Main;
import main.app.model.AppScreen;
import main.app.model.PlayLevelScreen;
import main.app.model.PlayModeSelectScreen;
import main.sound.SoundManager;

import java.util.ArrayList;

/**
 * Controller Class for the Level Select screen
 */
public class PlayLevelSelectController extends AppController {

    /**
     * Generic constructor
     * @param screen : the corresponding screen
     */
    public PlayLevelSelectController(AppScreen screen) {
        super(screen);
    }

    @FXML
    private Button btnLevel1;
    @FXML
    private Button btnLevel2;
    @FXML
    private Button btnLevel3;
    @FXML
    private Button btnLevel4;

    private SoundManager soundManager = SoundManager.getInstance(5);

    @FXML
    public void initialize() {

        ArrayList<LevelMeta> levelMetas = new ArrayList<>();
        levelMetas.add(new LevelMeta("Start of Journey", 1, btnLevel1));
        levelMetas.add(new LevelMeta("Boulder Puzzle", 2, btnLevel2));
        levelMetas.add(new LevelMeta("Big Map", 3, btnLevel3));
        levelMetas.add(new LevelMeta("Brutal", 4, btnLevel4));

        levelMetas.forEach(meta -> {
            Button btn = meta.button;
            btn.setDisable(Main.gameConfig.getMaxLevelCompleted() + 1 < meta.levelNum);
            btn.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> onLevelSelected(meta.filename, meta.levelNum));
        });
    }

    private class LevelMeta {

        private String filename;
        private int levelNum;
        private Button button;

        private LevelMeta(String filename, int levelNum, Button button) {
            this.filename = filename;
            this.levelNum = levelNum;
            this.button   = button;
        }
    }

    @FXML
    private void onLevelSelected(String filename, int levelNum) {
        soundManager.playSoundEffect("Item");
        soundManager.playBGM("" + levelNum);

        switchScreen(new PlayLevelScreen(screen, screen.getStage(), filename, "src/asset/level", levelNum, false));
    }


    @FXML
    public void onBackBtnPressed() {
        switchScreen(new PlayModeSelectScreen(screen.getStage()));
        soundManager.playSoundEffect("Item");
    }
}
