package main.app.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import main.app.model.AppScreen;
import main.app.model.CreateModeSelectScreen;
import main.maploading.DraftBuilder;
import main.math.Vec2i;

import java.util.ArrayList;

public class CreativeLabController extends AppController {

    private String selectedEntity;
    private DraftBuilder draftBuilder;

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
    public CreativeLabController(AppScreen screen) {
        super(screen);
    }

    @FXML
    public void initialize() {
        initialiseEditor(8, 8);
        initialiseToolBox();
        initialiseOptions();
        initialiseObjectivesBox();

        draftBuilder = new DraftBuilder(8, 8, "testDraft");
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
                        draftBuilder.editTileGUI(new Vec2i(selectedCol, selectedRow), selectedEntity);
                        draftBuilder.displayLevel();
                    }

                    System.out.println(selectedRow + " " + selectedCol + selectedEntity);
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
        Button resize  = new Button();
        Button publish = new Button();
        Button exit    = new Button();

        Label rowsLabel  = new Label("Rows: ");
        rowsLabel.setMinWidth(15.0);
        TextField newRow = new TextField();
        newRow.setMaxWidth(45.0);

        Label colsLabel  = new Label("Cols: ");
        colsLabel.setMinWidth(15.0);
        TextField newCol = new TextField();
        newCol.setMaxWidth(45.0);

        save.setText("Save");
        saveAs.setText("Save As");
        resize.setText("Resize");
        publish.setText("Publish");
        exit.setText("Exit");

        exit.setOnAction(this::onExitBtnPressed);
        save.setOnAction(this::onSaveBtnPressed);
        publish.setOnAction(this::onPublishBtnPressed);

        optionsMenu.add(save, 0, 0);
        optionsMenu.add(saveAs, 0, 1);
        optionsMenu.add(resize, 0, 2);
        optionsMenu.add(publish, 0, 5);
        optionsMenu.add(exit, 0, 6);

        HBox resizeRow = new HBox();
        resizeRow.getChildren().addAll(rowsLabel, newRow);

        HBox resizeCol = new HBox();
        resizeCol.getChildren().addAll(colsLabel, newCol);

        optionsMenu.add(resizeRow, 0 ,3);
        optionsMenu.add(resizeCol, 0 , 4);

        resize.setOnAction(e -> {
            int newRowSize = Integer.parseInt(newRow.getText());
            int newColSize = Integer.parseInt(newCol.getText());

            draftBuilder.resize(newRowSize, newColSize);
            draftBuilder.displayLevel();
        });

        for (Node n : optionsMenu.getChildren()) {
            GridPane.setHalignment(n, HPos.CENTER);
            GridPane.setValignment(n, VPos.CENTER);
        }

    }

    private void initialiseObjectivesBox() {

        initialiseGridPane(objectivesBox, 2, 2);

        CheckBox exitCondition = new CheckBox();
        CheckBox killCondition = new CheckBox();
        CheckBox treasureCondition = new CheckBox();
        CheckBox switchCondition = new CheckBox();

        exitCondition.setText("A");
        treasureCondition.setText("B");
        killCondition.setText("C");
        switchCondition.setText("D");

        EventHandler makeMutuallyExclusive = e -> {
            CheckBox cb = (CheckBox) e.getSource();
            ArrayList<String> objectives = new ArrayList<>();

            if (cb.getText().equals("A")) {
                killCondition.setSelected(false);
                treasureCondition.setSelected(false);
                switchCondition.setSelected(false);

                objectives.add(cb.getText());
            } else {
                exitCondition.setSelected(false);

                if (treasureCondition.isSelected()) objectives.add("B");
                if (killCondition.isSelected()) objectives.add("C");
                if (switchCondition.isSelected()) objectives.add("D");
            }

            draftBuilder.setObjective(objectives);
            draftBuilder.displayLevel();

        };

        exitCondition.setOnAction(makeMutuallyExclusive);
        killCondition.setOnAction(makeMutuallyExclusive);
        treasureCondition.setOnAction(makeMutuallyExclusive);
        switchCondition.setOnAction(makeMutuallyExclusive);

        objectivesBox.add(exitCondition, 0, 0);
        objectivesBox.add(killCondition, 0, 1);
        objectivesBox.add(treasureCondition, 1, 0);
        objectivesBox.add(switchCondition, 1, 1);

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

    private void onSaveBtnPressed(ActionEvent actionEvent) {
        draftBuilder.saveMap(draftBuilder.getName(), "drafts");
    }

    private void onPublishBtnPressed(ActionEvent actionEvent) {
        //when published : play then save into levels

    }

}
