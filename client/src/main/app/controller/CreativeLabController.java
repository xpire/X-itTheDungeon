package main.app.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import main.app.model.AppScreen;
import main.app.model.CreateModeSelectScreen;
import main.app.model.CreativeLabScreen;
import main.entities.Avatar;
import main.entities.Entity;
import main.entities.enemies.*;
import main.entities.pickup.*;
import main.entities.prop.Boulder;
import main.entities.prop.IceBlock;
import main.entities.terrain.*;
import main.maploading.DraftBuilder;
import main.math.Vec2i;
import main.sprite.SpriteView;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

//TODO :
//Key-door mapping
//hover w/ key-door
//creative select screen
//make replacing things more intuitive?
//UNDO function - command pattern + memento pattern

public class CreativeLabController extends AppController<CreativeLabScreen> {

    private String selectedEntity;

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
//        draftBuilder = new DraftBuilder(8, 8, "testDraft");

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

    public GridPane getCurrDraft() {
        return currDraft;
    }

    public GridPane getToolbox() {
        return toolbox;
    }

}
