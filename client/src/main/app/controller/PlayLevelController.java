package main.app.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import main.InventoryView;
import main.PlayModeUILocator;
import main.app.model.PlayLevelScreen;
import main.sound.SoundManager;

public class PlayLevelController extends AppController<PlayLevelScreen> {

    public static PlayModeUILocator locator;
    SoundManager soundManager = SoundManager.getInstance(5);

    @FXML
    private StackPane dynamicLayer;

    @FXML
    private ImageView imgSword;

    @FXML
    private ImageView imgArrow;

    @FXML
    private ImageView imgBomb;

    @FXML
    private ImageView imgKey;

    @FXML
    private ImageView imgGold;

    @FXML
    private Label lblSword;

    @FXML
    private Label lblArrow;

    @FXML
    private Label lblBomb;

    @FXML
    private Label lblGold;

    @FXML
    private Label lblTitle;

    @FXML
    private Label lblSubtitle;

    @FXML
    private VBox vbxObjectives;

    public PlayLevelController(PlayLevelScreen screen) {
        super(screen);
    }


    @FXML
    public void initialize() {
        vbxObjectives.getChildren().clear();

        locator = new PlayModeUILocator.Builder()
                .sword(new InventoryView(imgSword, lblSword))
                .arrow(new InventoryView(imgArrow, lblArrow))
                .bomb(new InventoryView(imgBomb, lblBomb))
                .key(new InventoryView(imgKey, null))
                .gold(new InventoryView(imgGold, lblGold))
                .title(lblTitle)
                .subtitle(lblSubtitle)
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
