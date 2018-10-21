package main.app.model;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import main.Level;
import main.Toast;
import main.app.Main;
import main.app.controller.AppController;
import main.app.controller.CreativeLabController;
import main.entities.Avatar;
import main.entities.Entity;
import main.entities.enemies.*;
import main.entities.pickup.*;
import main.entities.prop.*;
import main.entities.terrain.*;
import main.content.ObjectiveFactory;
import main.maploading.DraftBuilder;
import main.maploading.InvalidMapException;
import main.math.Vec2d;
import main.math.Vec2i;
import main.sprite.SpriteView;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class CreativeLabScreen extends AppScreen {

    {
        title = "X-it the Dungeon";
        fxmlName = "main/app/view/creativeLab.fxml";
    }

    private CreativeLabController controller;
    private DraftBuilder draftBuilder;
    private String selectedEntity;

    private boolean isKeyDoorMatching = false;
    private boolean wasKey = false;
    private Vec2i originalPos;

    public CreativeLabScreen(Stage stage, DraftBuilder draftBuilder) {
        super(stage);
        this.controller = new CreativeLabController(this);
        this.draftBuilder = draftBuilder;
    }

    /**
     * Initialises the Editor GridPlane on the scene
     */
    public void initialiseEditor(GridPane currDraft) {
        int numCols = draftBuilder.getNCols();
        int numRows = draftBuilder.getNRows();

        initialiseGridPane(currDraft, numCols, numRows, false);

        for (int i = 0 ; i < numCols ; i++) {
            for (int j = 0; j < numRows; j++) {
                editorSelectHandler(i, j);
            }
        }
    }

    /**
     * Initialises the ToolBox GridPlane on the scene
     */
    public void initialiseToolBox(GridPane toolbox) {
        int numCols = 12, numRows = 2;

        initialiseGridPane(toolbox, numCols, numRows, true);
        ArrayList<SpriteView> spriteViews = new ArrayList<>();

        Level temp = new Level(8, 8, 8, "eight");

        addSelectHandler(spriteViews, new Ground(temp), 0, 0);
        addSelectHandler(spriteViews, new Wall(temp), 1, 0);
        addSelectHandler(spriteViews, new Door(temp), 2, 0);
        addSelectHandler(spriteViews, new Pit(temp), 3, 0);
        addSelectHandler(spriteViews, new Switch(temp), 4, 0);
        addSelectHandler(spriteViews, new Exit(temp), 5, 0);
        addSelectHandler(spriteViews, new Boulder(temp), 6, 0, 1.5, 1.5);
        addSelectHandler(spriteViews, new IceBlock(temp), 7, 0);
        addSelectHandler(spriteViews, new HeatPlate(temp), 8, 0);
        addSelectHandler(spriteViews, new Hunter(temp), 9, 0);
        addSelectHandler(spriteViews, new Strategist(temp), 10, 0);
        addSelectHandler(spriteViews, new Arrow(temp), 0, 1, 1.5, 1.5);
        addSelectHandler(spriteViews, new Sword(temp), 1, 1, 2, 2);
        addSelectHandler(spriteViews, new Key(temp), 2, 1, 2.5, 2.5);
        addSelectHandler(spriteViews, new Treasure(temp), 3, 1, 2,2);
        addSelectHandler(spriteViews, new Bomb(temp), 4, 1, 2, 2);
        addSelectHandler(spriteViews, new InvincibilityPotion(temp), 5, 1, 2.5, 2.5);
        addSelectHandler(spriteViews, new HoverPotion(temp), 6, 1, 2.5,2.5);
        addSelectHandler(spriteViews, new BombPotion(temp), 7, 1, 2.5, 2.5);
        addSelectHandler(spriteViews, new Avatar(temp), 8, 1, 1.5, 1.5);
        addSelectHandler(spriteViews, new Hound(temp), 9, 1);
        addSelectHandler(spriteViews, new Coward(temp), 10, 1);

        addEraser(spriteViews);

        centreGridPaneChildren(toolbox);
    }

    /**
     * Initialises the Options GridPlane on the scene
     */
    public void initialiseOptions(GridPane optionsMenu) {
        int numCols = 1, numRows = 9;

        initialiseGridPane(optionsMenu, numCols, numRows, true);

        Button save    = new Button();
        Button saveAs  = new Button();
        Button resize  = new Button();
        Button help    = new Button();
        Button publish = new Button();
        Button exit    = new Button();

        save.setText("Save");
        saveAs.setText("Save As");
        resize.setText("Resize");
        help.setText("Help");
        publish.setText("Publish");
        exit.setText("Exit");

        save.setOnAction(e -> {
            draftBuilder.getLevel().toFile(draftBuilder.getName(), "main/drafts");
            printPrompt("Draft Saved");
        });
        help.setOnAction(e -> controller.switchScreen(new HelpManualScreen(getStage(), this)));
        exit.setOnAction(e -> controller.switchScreen(new CreateModeSelectScreen(this.getStage())));
        publish.setOnAction(e -> {
            if (draftBuilder.isTrivialLevel()) {
                Toast.messageToast(getStage(), "Sorry, level is trivial");
                return;
            }
            if (draftBuilder.containsExit() && !draftBuilder.listObjectives().contains("EXIT")) {
                Toast.messageToast(getStage(), "Exit must be the condition if Exit exists");
                return;
            }

            testPlay(true);
        });

        HBox resizeRow = new HBox();
        HBox resizeCol = new HBox();

        Label rowsLabel  = new Label("Rows: ");
        Label colsLabel  = new Label("Cols: ");
        rowsLabel.setMinWidth(15.0);
        colsLabel.setMinWidth(15.0);

        TextField newRow = new TextField(String.valueOf(draftBuilder.getNRows()));
        TextField newCol = new TextField(String.valueOf(draftBuilder.getNCols()));
        newRow.setMaxWidth(45.0);
        newCol.setMaxWidth(45.0);
        newRow.setOnAction(e -> resize.fire());
        newCol.setOnAction(e -> resize.fire());

        resizeRow.getChildren().addAll(rowsLabel, newRow);
        resizeCol.getChildren().addAll(colsLabel, newCol);

        HBox saveAsBox = new HBox();
        TextField newName = new TextField(draftBuilder.getName());
        newName.setMaxWidth(90);
        newName.setVisible(false);

        Button confirmName = new Button("OK");
        confirmName.setVisible(false);
        confirmName.setOnAction(confirm -> {
            String newDraftName = newName.getText();
            if (!newDraftName.equals("") && newDraftName.matches("[a-zA-Z0-9]+")) {

                draftBuilder.toFile(newDraftName, "main/drafts");
                draftBuilder.setName(newDraftName);
                printPrompt("Draft saved as " + newDraftName);
            }
            else {
                printPrompt("Please enter a valid draft name");
            }
        });

        saveAs.setOnAction(e -> {
            newName.setVisible(true);
            newName.requestFocus();
            confirmName.setVisible(true);
        });

        newName.setOnAction(e -> confirmName.fire());

        saveAsBox.getChildren().addAll(newName, confirmName);

        optionsMenu.add(save, 0, 0);
        optionsMenu.add(saveAs, 0, 1);
        optionsMenu.add(saveAsBox, 0, 2);
        optionsMenu.add(resize, 0, 3);
        optionsMenu.add(resizeRow, 0 ,4);
        optionsMenu.add(resizeCol, 0 , 5);
        optionsMenu.add(help, 0, 6);
        optionsMenu.add(publish, 0, 7);
        optionsMenu.add(exit, 0, 8);

        resize.setOnAction(e -> {
            try {
                int newRowSize = Integer.parseInt(newRow.getText());
                int newColSize = Integer.parseInt(newCol.getText());

                if (!new Vec2i(newColSize, newRowSize).within(new Vec2i(4, 4), new Vec2i(24, 24))) {
                    printPrompt("Size must be between 4x4 and 24x24");
                    newRow.setText(String.valueOf(draftBuilder.getNRows()));
                    newCol.setText(String.valueOf(draftBuilder.getNCols()));
                    return;
                }

                draftBuilder.resize(newRowSize, newColSize);
                updateEditorGridPane(newRowSize, newColSize);

                draftBuilder.displayLevel();
            } catch (NumberFormatException nfe) {
                nfe.printStackTrace();
                newRow.setText(String.valueOf(draftBuilder.getNRows()));
                newCol.setText(String.valueOf(draftBuilder.getNCols()));
            }
        });

        centreGridPaneChildren(optionsMenu);
    }

    /**
     * Initialises the Objectives GridPane on the scene
     */
    public void initialiseObjectivesBox(GridPane objectivesBox) {

        initialiseGridPane(objectivesBox, 2, 2, true);

        CheckBox exitCondition = new CheckBox();
        CheckBox killCondition = new CheckBox();
        CheckBox treasureCondition = new CheckBox();
        CheckBox switchCondition = new CheckBox();

        exitCondition.setText("Exit");
        treasureCondition.setText("$$$");
        killCondition.setText("Kill");
        switchCondition.setText("Flip");

        exitCondition.setAccessibleText(ObjectiveFactory.Type.EXIT.name());
        treasureCondition.setAccessibleText(ObjectiveFactory.Type.COLLECT_ALL_TREASURES.name());
        killCondition.setAccessibleText(ObjectiveFactory.Type.KILL_ALL_ENEMIES.name());
        switchCondition.setAccessibleText(ObjectiveFactory.Type.ACTIVATE_ALL_SWITCHES.name());

        for (String s : draftBuilder.listObjectives().split("\\s+")) {
            switch (s) {
                case "EXIT":
                    exitCondition.setSelected(true);
                    break;
                case "COLLECT_ALL_TREASURES":
                    treasureCondition.setSelected(true);
                    break;
                case "KILL_ALL_ENEMIES":
                    killCondition.setSelected(true);
                    break;
                case "ACTIVATE_ALL_SWITCHES":
                    switchCondition.setSelected(true);
            }
        }

        EventHandler<ActionEvent> makeMutuallyExclusive = e -> {
            CheckBox cb = (CheckBox) e.getSource();
            ArrayList<String> objectives = new ArrayList<>();

            if (cb.getText().equals("Exit")) {
                killCondition.setSelected(false);
                treasureCondition.setSelected(false);
                switchCondition.setSelected(false);

                objectives.add(exitCondition.getAccessibleText());
            } else {
                exitCondition.setSelected(false);

                if (treasureCondition.isSelected()) objectives.add(treasureCondition.getAccessibleText());
                if (killCondition.isSelected()) objectives.add(killCondition.getAccessibleText());
                if (switchCondition.isSelected()) objectives.add(switchCondition.getAccessibleText());
            }

            setObjectives(objectives);
        };

        exitCondition.setOnAction(makeMutuallyExclusive);
        killCondition.setOnAction(makeMutuallyExclusive);
        treasureCondition.setOnAction(makeMutuallyExclusive);
        switchCondition.setOnAction(makeMutuallyExclusive);

        objectivesBox.add(exitCondition, 0, 0);
        objectivesBox.add(killCondition, 0, 1);
        objectivesBox.add(treasureCondition, 1, 0);
        objectivesBox.add(switchCondition, 1, 1);

        centreGridPaneChildren(objectivesBox);
    }

    private void centreGridPaneChildren(GridPane gp) {
        for (Node n : gp.getChildren()) {
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
     * Saves the current state of the level to a temporary file and runs it in Play Mode
     */
    public void testPlay(boolean isPublishTest) {

        String saveName = (isPublishTest) ? draftBuilder.getName() : "tempSave";
        draftBuilder.toFile(saveName, "save/temp");

        controller.switchScreen(new PlayLevelScreen(this, this.getStage(), saveName, "src/save/temp", 0, isPublishTest));
    }

    /**
     * Updates the GridPane to represent the resized level
     * @param newRow : new number of rows
     * @param newCol : new number of cols
     */
    private void updateEditorGridPane(int newRow, int newCol) {
        GridPane currDraft = controller.getCurrDraft();

        removeGridPaneNodes(currDraft, newRow, newCol);
        removeConstraints(currDraft, newRow, newCol);
        addConstraints(currDraft, newRow, newCol);

        StackPane.setAlignment(currDraft, Pos.CENTER);
    }

    /**
     * Adds row and column constraints to the editor GridPane when resizing to a larger map
     * @param currDraft the editor GridPane
     * @param newRow new number of rows
     * @param newCol new number of cols
     */
    private void addConstraints(GridPane currDraft, int newRow, int newCol) {
        for (int currRow = currDraft.getRowConstraints().size(); currRow < newRow; currRow++) {
            RowConstraints rowConstraints = new RowConstraints();
            currDraft.getRowConstraints().add(rowConstraints);

            for (int i = 0; i < currDraft.getColumnConstraints().size(); i++) editorSelectHandler(i, currRow);
        }

        for (int currCol = currDraft.getColumnConstraints().size(); currCol < newCol; currCol++) {
            ColumnConstraints columnConstraints = new ColumnConstraints();
            currDraft.getColumnConstraints().add(columnConstraints);

            for (int j = 0; j < currDraft.getRowConstraints().size(); j++) editorSelectHandler(currCol, j);
        }
    }

    /**
     * Removes row and column constraints from the editor GridPane when resizing to a smaller map
     * @param currDraft the editor GridPane
     * @param newRow new number of rows
     * @param newCol new number of cols
     */
    private void removeConstraints(GridPane currDraft, int newRow, int newCol) {
        for (int currRow = currDraft.getRowConstraints().size() - 1; currRow >= newRow; currRow--)
            currDraft.getRowConstraints().remove(currRow);

        for (int currCol = currDraft.getColumnConstraints().size() - 1; currCol >= newCol; currCol--)
            currDraft.getColumnConstraints().remove(currCol);
    }

    /**
     * Clears nodes from the editor GridPane when resizing to a smaller map
     * @param currDraft the editor GridPane
     * @param newRow new number of rows
     * @param newCol new number of cols
     */
    private void removeGridPaneNodes(GridPane currDraft, int newRow, int newCol) {
        Set<Node> deleteNodes = new HashSet<>();
        for (Node child : currDraft.getChildren()) {
            Integer rowIndex = GridPane.getRowIndex(child);
            Integer colIndex = GridPane.getColumnIndex(child);

            int r = (rowIndex == null) ? 0 : rowIndex;
            int c = (colIndex == null) ? 0 : colIndex;

            if (r >= newRow || c >= newCol) deleteNodes.add(child);
        }

        currDraft.getChildren().removeAll(deleteNodes);
    }

    /**
     * Adds a selectable Pane to the editor GridPane at a given location
     * @param i : x position
     * @param j : y position
     */
    private void editorSelectHandler(int i, int j) {
        Pane pane = new Pane();
        pane.setPrefSize(30, 30);

        pane.setOnMouseClicked(e -> {
            Node source = (Node) e.getSource();

            int selectedRow = GridPane.getRowIndex(source);
            int selectedCol = GridPane.getColumnIndex(source);
            Vec2i pos = new Vec2i(selectedCol, selectedRow);

            if (!isKeyDoorMatching) {
                if (selectedEntity != null) {
                    switch (selectedEntity) {
                        case "E":
                            draftBuilder.eraseEntitiesAt(pos);
                            break;
                        case "K":
                            originalPos = pos;
                            selectedEntity = "|";
                            wasKey = true;
                            isKeyDoorMatching = true;
                            printPrompt("Please select position of matching door");
                            break;
                        case "|":
                            originalPos = pos;
                            selectedEntity = "K";
                            wasKey = false;
                            isKeyDoorMatching = true;
                            printPrompt("Please select position of matching key");
                            break;
                        default:
                            draftBuilder.editTileGUI(pos, selectedEntity);
                            draftBuilder.displayLevel();
                            break;
                    }
                }
            } else {
                if (!pos.equals(originalPos)) {
                    switch (selectedEntity) {
                        case "K":
                            if (wasKey) break;
                            draftBuilder.editTileKeyDoorGUI(pos, originalPos);
                            selectedEntity = "|";
                            break;
                        case "|":
                            if (!wasKey) break;
                            draftBuilder.editTileKeyDoorGUI(originalPos, pos);
                            selectedEntity = "K";
                            break;
                        default:
                            printPrompt("Error: selected entity was changed");
                    }
                    isKeyDoorMatching = false;
                    draftBuilder.displayLevel();
                }
            }
        });

        controller.getCurrDraft().add(pane, i, j);
    }

    /**
     * Adds a DropShadow to the currently selected entity in the toolbox
     * @param sv : ArrayList of all SpriteViews within the toolbox
     * @param spriteView : spriteView to add the DropShadow to
     */
    private void setSelectedGlow(ArrayList<SpriteView> sv, SpriteView spriteView) {
        for (SpriteView spv : sv) spv.setEffect(null);

        DropShadow dropShadow = new DropShadow();
        dropShadow.setHeight(30);
        dropShadow.setWidth(30);
        dropShadow.setColor(Color.BLACK);

        spriteView.setEffect(dropShadow);
    }

    /**
     * Creates SpriteViews for the toolbox GridPane and sets their onclick property
     * @param views : ArrayList of all current sprites in the toolbox
     * @param entity : Entity corresponding to the sprite required
     * @param x : x position
     * @param y : y position
     * @param scaleX : Additional x-scaling of sprite
     * @param scaleY : Additional y-scaling of sprite
     */
    private void addSelectHandler(ArrayList<SpriteView> views, Entity entity, int x, int y, double scaleX, double scaleY) {
        SpriteView s = entity.getSprite();

        s.magnifyScales(scaleX, scaleY);

        s.setOnMouseClicked(e -> {
            selectedEntity = String.valueOf(entity.getSymbol());
            setSelectedGlow(views, s);
        });

        views.add(s);
        controller.getToolbox().add(s, x, y);
    }

    /**
     * Special method to load in the eraser sprite to the toolbox
     * @param spriteViews : the ArrayList of all sprites in the toolbox
     */
    private void addEraser(ArrayList<SpriteView> spriteViews) {
        SpriteView eraser;
        try (FileInputStream image = new FileInputStream("./src/asset/sprite/eraser.png")) {
            eraser = new SpriteView(new Image(image), new Vec2d(-8, -8), 2, 2);
            eraser.setOnMouseClicked(e -> {
                selectedEntity = "E";
                setSelectedGlow(spriteViews, eraser);
            });
            spriteViews.add(eraser);
            controller.getToolbox().add(eraser, 11, 1, 2, 1);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Overloaded addSelectHandler method which sets the additional scaling factors to 1
     * @param views : ArrayList of SpriteViews
     * @param entity : Entity corresponding to the sprite required
     * @param x : x position
     * @param y : y position
     */
    private void addSelectHandler(ArrayList<SpriteView> views, Entity entity, int x, int y) {
        addSelectHandler(views, entity, x, y, 1, 1);
    }

    /**
     * Sets the objectives of a level to the currently selected checkboxes
     * @param objectives ArrayList of objectives
     */
    private void setObjectives(ArrayList<String> objectives) {
        try {
            draftBuilder.clearObjectives();
            draftBuilder.setObjective(objectives);
        } catch (InvalidMapException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the view for the current level so that it can be displayed while creating maps
     * @return The view of the level in progress
     */
    public Node getView() {
        return draftBuilder.getView();
    }

    /**
     * Prints prompts out on the screen when needed
     * @param prompt : msg to be printed out
     */
    private void printPrompt(String prompt) {
        Toast.messageToast(Main.primaryStage, prompt);
    }

    @Override
    protected AppController getController() {
        return controller;
    }
}
