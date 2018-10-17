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
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import main.Level;
import main.app.model.AppScreen;
import main.app.model.CreateModeSelectScreen;
import main.entities.Avatar;
import main.entities.Entity;
import main.entities.enemies.*;
import main.entities.pickup.*;
import main.entities.prop.Boulder;
import main.entities.prop.IceBlock;
import main.entities.terrain.*;
import main.maploading.DraftBuilder;
import main.maploading.MapLoader;
import main.math.Vec2d;
import main.math.Vec2i;
import main.sprite.SpriteView;

import java.util.ArrayList;

//TODO :
//Key-door mapping
//fix resize issues
//hover w/ key-door
//creative select screen
//make replacing things more intuitive?
//move options box to the right for consistency w/ play
//add toolbox sprites + imageview rather than radio buttons
//UNDO function - command pattern + memento pattern

public class CreativeLabController extends AppController {

    private String selectedEntity;
    private DraftBuilder draftBuilder;

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
    public CreativeLabController(AppScreen screen) {
        super(screen);
    }

    @FXML
    public void initialize() {
        draftBuilder = new DraftBuilder(8, 8, "testDraft");

        initialiseEditor(8, 8);
        initialiseToolBox();
        initialiseOptions();
        initialiseObjectivesBox();

        Node view = draftBuilder.getLevel().getView();
        viewPane.getChildren().add(0, view);

        StackPane.setAlignment(view, Pos.CENTER);
        StackPane.setAlignment(currDraft, Pos.CENTER);

        StackPane.setAlignment(toolbox, Pos.CENTER);
    }

    /**
     * Initialises the Editor GridPlane on the scene
     */
    private void initialiseEditor(int numCols, int numRows) {

        initialiseGridPane(currDraft, numCols, numRows, false);

        for (int i = 0 ; i < numCols ; i++) {
            for (int j = 0; j < numRows; j++) {
                Pane pane = new Pane();
                pane.setPrefSize(30, 30);

                pane.setOnMouseClicked(e -> {
                    Node source = (Node) e.getSource();

                    int selectedRow = GridPane.getRowIndex(source);
                    int selectedCol = GridPane.getColumnIndex(source);

                    if (selectedEntity != null) {
                        Vec2i pos = new Vec2i(selectedCol, selectedRow);

                        if (selectedEntity.equals("E")) draftBuilder.eraseEntitiesAt(pos);

                        draftBuilder.editTileGUI(pos, selectedEntity);
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
        int numCols = 11, numRows = 2;

        initialiseGridPane(toolbox, numCols, numRows, true);
        ArrayList<SpriteView> spriteViews = new ArrayList<>();

        addSelectHandler(spriteViews, new Ground(draftBuilder.getLevel()), 0, 0);
        addSelectHandler(spriteViews, new Wall(draftBuilder.getLevel()), 1, 0);
        addSelectHandler(spriteViews, new Door(draftBuilder.getLevel()), 2, 0);
        addSelectHandler(spriteViews, new Pit(draftBuilder.getLevel()), 3, 0);
        addSelectHandler(spriteViews, new Switch(draftBuilder.getLevel()), 4, 0);
        addSelectHandler(spriteViews, new Exit(draftBuilder.getLevel()), 5, 0);
        addSelectHandler(spriteViews, new Boulder(draftBuilder.getLevel()), 6, 0, 1.5, 1.5);
        addSelectHandler(spriteViews, new IceBlock(draftBuilder.getLevel()), 7, 0);
//        addSelectHandler(spriteViews, new HeatPlate(draftBuilder.getLevel()), 8, 0);
        addSelectHandler(spriteViews, new Hunter(draftBuilder.getLevel()), 9, 0);
        addSelectHandler(spriteViews, new Strategist(draftBuilder.getLevel()), 10, 0);
        addSelectHandler(spriteViews, new Arrow(draftBuilder.getLevel()), 0, 1, 1.5, 1.5);
        addSelectHandler(spriteViews, new Sword(draftBuilder.getLevel()), 1, 1, 2, 2);
        addSelectHandler(spriteViews, new Key(draftBuilder.getLevel()), 2, 1, 2.5, 2.5);
        addSelectHandler(spriteViews, new Treasure(draftBuilder.getLevel()), 3, 1, 2,2);
        addSelectHandler(spriteViews, new Bomb(draftBuilder.getLevel()), 4, 1, 2, 2);
        addSelectHandler(spriteViews, new InvincibilityPotion(draftBuilder.getLevel()), 5, 1, 2.5, 2.5);
        addSelectHandler(spriteViews, new HoverPotion(draftBuilder.getLevel()), 6, 1, 2.5,2.5);
        addSelectHandler(spriteViews, new BombPotion(draftBuilder.getLevel()), 7, 1, 2.5, 2.5);
        addSelectHandler(spriteViews, new Avatar(draftBuilder.getLevel()), 8, 1, 1.5, 1.5);
        addSelectHandler(spriteViews, new Hound(draftBuilder.getLevel()), 9, 1);
        addSelectHandler(spriteViews, new Coward(draftBuilder.getLevel()), 10, 1);

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

        initialiseGridPane(optionsMenu, numCols, numRows, true);

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

        exit.setOnAction(e -> switchScreen(new CreateModeSelectScreen(screen.getStage())));

        save.setOnAction(e -> draftBuilder.saveMap(draftBuilder.getName(), "drafts"));

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
            try {
                int newRowSize = Integer.parseInt(newRow.getText());
                int newColSize = Integer.parseInt(newCol.getText());

                if (newRowSize < 0 || newColSize < 0) {
                    System.out.println("Positive numbers please");
                    newRow.clear();
                    newCol.clear();
                    return;
                }

                draftBuilder.resize(newRowSize, newColSize);
                updateEditorGridPane(newRowSize, newColSize);

                draftBuilder.displayLevel();
            } catch (NumberFormatException nfe) {
                System.out.println(nfe.getMessage());
                newRow.clear();
                newCol.clear();
            }
        });

        for (Node n : optionsMenu.getChildren()) {
            GridPane.setHalignment(n, HPos.CENTER);
            GridPane.setValignment(n, VPos.CENTER);
        }

    }

    /**
     * Initialises the Objectives GridPane on the scene
     */
    private void initialiseObjectivesBox() {

        initialiseGridPane(objectivesBox, 2, 2, true);

        CheckBox exitCondition = new CheckBox();
        CheckBox killCondition = new CheckBox();
        CheckBox treasureCondition = new CheckBox();
        CheckBox switchCondition = new CheckBox();

        exitCondition.setText("A");
        treasureCondition.setText("B");
        killCondition.setText("C");
        switchCondition.setText("D");

        EventHandler<ActionEvent> makeMutuallyExclusive = e -> {
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
    private void initialiseGridPane(GridPane gp, int numCols, int numRows, boolean setGrow) {
        for (int i = 0; i < numCols; i++) {
            ColumnConstraints colConstraints = new ColumnConstraints();
            if (setGrow) colConstraints.setHgrow(Priority.SOMETIMES);

            gp.getColumnConstraints().add(colConstraints);
        }

        for (int i = 0 ; i < numRows ; i++) {
            RowConstraints rowConstraints = new RowConstraints();
            if (setGrow) rowConstraints.setVgrow(Priority.SOMETIMES);

            gp.getRowConstraints().add(rowConstraints);
        }
    }

    /**
     * Updates the GridPane to represent the resized level
     * @param newRow : new number of rows
     * @param newCol : new number of cols
     */
    private void updateEditorGridPane(int newRow, int newCol) {
        System.out.println("testing" + newCol + " " + newCol);
        for (int currRow = currDraft.getRowConstraints().size() - 1; currRow >= newRow; currRow--)
            currDraft.getRowConstraints().remove(currRow);

        for (int currCol = currDraft.getColumnConstraints().size() - 1; currCol >= newCol; currCol--)
            currDraft.getColumnConstraints().remove(currCol);


        for (int currRow = currDraft.getRowConstraints().size(); currRow < newRow; currRow++) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setVgrow(Priority.SOMETIMES);
            currDraft.getRowConstraints().add(rowConstraints);
        }

        for (int currCol = currDraft.getColumnConstraints().size(); currCol < newCol; currCol++) {
            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setHgrow(Priority.SOMETIMES);
            currDraft.getColumnConstraints().add(columnConstraints);
        }
    }

    private void setSelectedGlow(ArrayList<SpriteView> sv, SpriteView spriteView) {
        for (SpriteView spv : sv) spv.setEffect(null);

        DropShadow dropShadow = new DropShadow();
        dropShadow.setHeight(30);
        dropShadow.setWidth(30);
        dropShadow.setColor(Color.BLACK);

        spriteView.setEffect(dropShadow);
    }

    private void addSelectHandler(ArrayList<SpriteView> views, Entity entity, int x, int y) {
        addSelectHandler(views, entity, x, y, 1, 1);
    }

    private void addSelectHandler(ArrayList<SpriteView> views, Entity entity, int x, int y, double scaleX, double scaleY) {
        SpriteView s = entity.getSprite();

        s.magnifyScales(scaleX, scaleY);

        s.setOnMouseClicked(e -> {
            selectedEntity = String.valueOf(entity.getSymbol());
            setSelectedGlow(views, s);
        });

        views.add(s);
        toolbox.add(s, x, y);
    }

}
