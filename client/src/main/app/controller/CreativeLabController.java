package main.app.controller;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.*;
import main.app.model.CreativeLabScreen;
import main.sound.SoundManager;

/**
 * Controller for the Creative Lab screen
 */
public class CreativeLabController extends AppController<CreativeLabScreen> {

    @FXML
    private StackPane viewPane;

    @FXML
    private GridPane currDraft;

    @FXML
    private GridPane toolbox;

    @FXML
    private GridPane optionsMenu;

    @FXML
    private GridPane objectivesBox;

    private SoundManager soundManager = SoundManager.getInstance(5);


    /**
     * Generic constructor
     * @param screen : the corresponding screen
     */
    public CreativeLabController(CreativeLabScreen screen) {
        super(screen);
    }

    @FXML
    public void initialize() {
        screen.initialiseEditor(currDraft);
        screen.initialiseToolBox(toolbox);
        screen.initialiseOptions(optionsMenu);
        screen.initialiseObjectivesBox(objectivesBox);

        Node view = screen.getView();
        viewPane.getChildren().add(0, view);

        StackPane.setAlignment(view, Pos.CENTER);
        StackPane.setAlignment(currDraft, Pos.CENTER);

        StackPane.setAlignment(toolbox, Pos.CENTER);
    }

    @FXML
    public void onPlayTestBtnPressed() {
        screen.testPlay(false);
        soundManager.playSoundEffect("Item");
    }

    /**
     * Getter for the currDraft GridPane
     * @return The currDraft GridPane
     */
    public GridPane getCurrDraft() {
        soundManager.playSoundEffect("Item");
        return currDraft;
    }

    /**
     * Getter for the toolbox GridPane
     * @return The toolbox GridPane
     */
    public GridPane getToolbox() {
        soundManager.playSoundEffect("Item");
        return toolbox;
    }

}
