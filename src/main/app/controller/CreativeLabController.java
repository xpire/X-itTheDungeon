package main.app.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.*;
import main.app.model.AppScreen;
import main.app.model.CreateModeSelectScreen;

public class CreativeLabController extends AppController {

    private String selectedEntity;

    @FXML
    private GridPane currDraft;

    @FXML
    private GridPane toolbox;

    @FXML
    private GridPane optionsMenu;

    /**
     * Basic constructor for the CreativeLab Controller
     * @param screen : the corresponding screen
     */
    public CreativeLabController(AppScreen screen) {
        super(screen);
    }

    @FXML
    public void initialize() {
        initialiseEditor(8, 8);
        initialiseToolBox();
        initialiseOptions();
    }

    /**
     * Initialises the Editor GridPlane on the scene
     */
    private void initialiseEditor(int numCols, int numRows) {

        initialiseGridPane(currDraft, numCols, numRows);

        for (int i = 0 ; i < numCols ; i++) {
            for (int j = 0; j < numRows; j++) {
                Pane pane = new Pane();
                pane.setOnMouseClicked(e -> {
                    Node source = (Node) e.getSource();

                    int selectedRow = GridPane.getRowIndex(source);
                    int selectedCol = GridPane.getColumnIndex(source);

                    if (selectedEntity != null) {
                        //edit tile w/ selectedRow, Col
                    }


                    System.out.println(selectedRow);
                    System.out.println(selectedCol);
                });
                currDraft.add(pane, i, j);
            }
        }
    }

    /**
     * Initialises the ToolBox GridPlane on the scene
     */
    private void initialiseToolBox() {
        int numCols = 7, numRows = 3;

        initialiseGridPane(toolbox, numCols, numRows);

        ToggleGroup entityGroup = new ToggleGroup();
        entityGroup.selectedToggleProperty().addListener((ov, old_toggle, new_toggle) -> {
            if (entityGroup.getSelectedToggle() != null)
                selectedEntity = entityGroup.getSelectedToggle().getUserData().toString();
        });

        for (int i = 0; i < numCols; i++) {
            for (int j = 0; j < numRows; j++) {
                RadioButton rb = new RadioButton(getEntitySymbol(i, j));

                rb.setToggleGroup(entityGroup);
                rb.setUserData(getEntitySymbol(i, j));

                toolbox.add(rb, i, j);
            }
        }

        for (Node n : toolbox.getChildren()) {
            GridPane.setHalignment(n, HPos.CENTER);
            GridPane.setValignment(n, VPos.CENTER);
        }
    }

    /**
     * Initialises the Options GridPlane on the scene
     */
    private void initialiseOptions() {
        int numCols = 1, numRows = 7;

        initialiseGridPane(optionsMenu, numCols, numRows);

        //TODO : make this nicer
        Button save    = new Button();
        Button saveAs  = new Button();
        Button publish = new Button();
        Button exit    = new Button();

        save.setText("Save");
        saveAs.setText("Save As");
        publish.setText("Publish");
        exit.setText("Exit");

        exit.setOnAction(this::onExitBtnPressed);

        optionsMenu.add(save, 0, 0);
        optionsMenu.add(saveAs, 0, 1);
        optionsMenu.add(publish, 0, 5);
        optionsMenu.add(exit, 0, 6);

        for (Node n : optionsMenu.getChildren()) {
            GridPane.setHalignment(n, HPos.CENTER);
            GridPane.setValignment(n, VPos.CENTER);
        }

    }

    /**
     * Initialises a default GridPlane with row/col constraints
     * @param gp : GridPlane to be initialised
     * @param numCols : number of rows required
     * @param numRows : number of cols required
     */
    private void initialiseGridPane(GridPane gp, int numCols, int numRows) {
        for (int i = 0; i < numCols; i++) {
            ColumnConstraints colConstraints = new ColumnConstraints();
            colConstraints.setHgrow(Priority.SOMETIMES);
            gp.getColumnConstraints().add(colConstraints);
        }

        for (int i = 0 ; i < numRows ; i++) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setVgrow(Priority.SOMETIMES);
            gp.getRowConstraints().add(rowConstraints);
        }
    }

    /**
     * Hard coded method to name radio buttons in the Toolbox GridPlane
     * @param x : x - coord of the radio button
     * @param y : y - coord of the radio button
     * @return the corresponding string to the radio buttons location
     */
    private String getEntitySymbol(int x, int y) {
        String[][] entities = {
                {".", "*", "|", "#", "/", "X", "O"},
                {"-", "+", "K", "$", "!", ">", "^"},
                {"P", "1", "2", "3", "4", " ", " "}
        };

        return entities[y][x];
    }

    /**
     * Goes back to the previous screen
     * @param actionEvent : not used, here to keep the calling method happy
     */
    private void onExitBtnPressed(ActionEvent actionEvent) {
        switchScreen(new CreateModeSelectScreen(screen.getStage()));
    }
}
