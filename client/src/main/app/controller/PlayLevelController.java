package main.app.controller;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import main.PlayMode;
import main.PlayModeUILocator;
import main.app.model.AppScreen;
import main.app.model.PlayLevelScreen;
import main.app.model.PlayModeSelectScreen;
import main.sound.SoundManager;

public class PlayLevelController extends AppController<PlayLevelScreen> {

    public static PlayModeUILocator locator;
    SoundManager soundManager = SoundManager.getInstance(5);

    @FXML
    private StackPane dynamicLayer;

    // TODO create a class InventoryItemView
    @FXML
    private Group invSword;

    @FXML
    private Group invArrow;

    @FXML
    private Group invBomb;

    @FXML
    private Group invKey;

    @FXML
    private Group invGold;

    @FXML
    private VBox vbxObjectives;

    public PlayLevelController(PlayLevelScreen screen) {
        super(screen);
    }


    @FXML
    public void initialize() {
        vbxObjectives.getChildren().clear();

        locator = new PlayModeUILocator.Builder()
                .sword(invSword)
                .arrow(invArrow)
                .bomb(invBomb)
                .key(invKey)
                .gold(invGold)
                .objectives(vbxObjectives)
                .build();
    }

    @FXML
    public void onBackBtnPressed() {
        soundManager.playSoundEffect("Item");
        soundManager.playBGM("Main Menu");

        screen.backBtnPressed();
    }


    @FXML
    public void onRestartBtnPressed() {
        screen.restart();
        soundManager.playSoundEffect("Item");
    }

    @FXML
    public void onHelpBtnPressed() {
        soundManager.playSoundEffect("Item");

    }

    @FXML
    public void onSettingsBtnPressed() {
        soundManager.playSoundEffect("Item");

    }


    public StackPane getDynamicLayer() {
        return dynamicLayer;
    }
}
