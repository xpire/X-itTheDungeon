package main.app.controller;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.*;
import main.app.model.CreativeLabScreen;

//TODO :
//Key-door mapping
//hover w/ key-door
//creative select screen
//make replacing things more intuitive?
//UNDO function - command pattern + memento pattern

//bomb is broken on dev??
//resizing which removes an enemy/avatar breaks down (arrayOutOfBounds Exception)

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


    /**
     * Basic constructor for the CreativeLab Controller
     * @param screen : the corresponding screen
     */
    public CreativeLabController(CreativeLabScreen screen) {
        super(screen);
    }

    @FXML
    public void initialize() {

        screen.initialiseEditor(currDraft, 8, 8);
        screen.initialiseToolBox(toolbox);
        screen.initialiseOptions(optionsMenu);
        screen.initialiseObjectivesBox(objectivesBox);

        Node view = screen.initialiseView();
        viewPane.getChildren().add(0, view);

        StackPane.setAlignment(view, Pos.CENTER);
        StackPane.setAlignment(currDraft, Pos.CENTER);

        StackPane.setAlignment(toolbox, Pos.CENTER);
    }

    /**
     * Getter for the currDraft GridPane
     * @return The currDraft GridPane
     */
    public GridPane getCurrDraft() {
        return currDraft;
    }

    /**
     * Getter for the toolbox GridPane
     * @return The toolbox GridPane
     */
    public GridPane getToolbox() {
        return toolbox;
    }


}
