package main.app.model;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import main.Level;
import main.app.controller.AppController;
import main.app.controller.CreateModeSelectController;
import main.maploading.DraftBuilder;
import main.maploading.MapLoader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CreateModeSelectScreen extends AppScreen{

    private static final double PREVIEW_SIZE = 270.0;

    {
        title = "Select Create Mode";
        fxmlName = "main/app/view/createModeSelect.fxml";
    }

    private CreateModeSelectController controller;

    public CreateModeSelectScreen(Stage stage) {
        super(stage);
        this.controller = new CreateModeSelectController(this);
    }

    public void initialiseDraftList(Accordion draftsView) {
        try {
            Files.walk(Paths.get("src/main/drafts"))
                 .filter(Files::isRegularFile)
                 .forEach(path -> draftsView.getPanes().add(makeTitlePane(path)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private TitledPane makeTitlePane(Path path) {
        File file       = path.toFile();
        String filename = getFilenameWithoutExt(file);

        TitledPane title = new TitledPane();
        title.setText(filename);

        // Content
        HBox totalView = new HBox();
        totalView.setSpacing(10);

        // Preview
        Pane preview = makePreview(filename);

        // Options Box
        VBox boxOptions = new VBox();
        boxOptions.setSpacing(10);

        HBox boxName     = makeNameBox(file, filename);
        Button deleteBtn = makeDeleteBtn(file);
        Button resumeBtn = makeResumeBtn(filename);

        // Overall
        boxOptions.getChildren().addAll(boxName, resumeBtn, deleteBtn);
        totalView.getChildren().addAll(preview, boxOptions);
        title.setContent(totalView);
        return title;
    }

    private HBox makeNameBox(File file, String filename) {
        HBox boxName       = new HBox();
        Label lblName      = new Label("Name: ");
        TextField txtName  = new TextField(filename);
        Button btnOk       = new Button("OK");
        Button btnRename   = new Button("Rename");

        txtName.setEditable(false);
        btnOk.setVisible(false);

        txtName.setOnAction(e -> btnOk.fire());
        btnOk.setOnAction(e -> {
            String newName = String.format("%s.txt", txtName.getText());

            if (!renameFile(file, newName))
                System.out.println("Rename failed"); // TODO!

            txtName.setEditable(false);
            btnOk.setVisible(false);
            controller.switchScreen(new CreateModeSelectScreen(getStage()));
        });

        btnRename.setOnAction(e -> {
            txtName.setEditable(true);
            txtName.requestFocus();
            btnOk.setVisible(true);
        });

        boxName.getChildren().addAll(lblName, txtName, btnOk, btnRename);
        return boxName;
    }

    private Button makeDeleteBtn(File file) {
        Button deleteBtn = new Button();
        deleteBtn.setText("Delete");
        deleteBtn.setOnAction(e -> {
            if (!file.delete())
                System.out.println("Delete unsuccessful");
            controller.switchScreen(new CreateModeSelectScreen(getStage()));
        });
        return deleteBtn;
    }

    private Button makeResumeBtn(String filename) {
        Button resumeBtn = new Button();
        resumeBtn.setText("Resume Working");
        resumeBtn.setOnAction(e -> controller.switchScreen(
                new CreativeLabScreen(getStage(),
                        new DraftBuilder(
                                new MapLoader().loadLevel(filename, "src/main/drafts", true)))));
        return resumeBtn;
    }

    private String getFilenameWithoutExt(File file) {
        String filename = file.getName();
        return filename.substring(0, filename.lastIndexOf('.'));
    }

    private Pane makePreview(String filename) {
        StackPane pane = new StackPane();

        pane.setPadding(Insets.EMPTY);
        pane.setPrefWidth(PREVIEW_SIZE);
        pane.setPrefHeight(PREVIEW_SIZE);
        pane.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        pane.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));

        DraftBuilder builder = new DraftBuilder(new MapLoader().loadLevel(filename, "src/main/drafts", true));
        Level level = builder.getLevel();

        double scaleFactor = PREVIEW_SIZE / Math.max(level.getWidth(), level.getHeight());
        level.rescale(level.getSize() * scaleFactor);

        Group view = builder.getView();
        StackPane.setMargin(view, Insets.EMPTY);
        StackPane.setAlignment(view, Pos.CENTER);
        pane.getChildren().add(view);
        return pane;
    }

    private boolean renameFile(File file, String newName) {
        return file.renameTo(new File(file.getParentFile(), newName));
    }

    public void initialiseNewDraft(String draftName) {
        DraftBuilder draftBuilder = new DraftBuilder(8, 8, draftName);
        controller.switchScreen(new CreativeLabScreen(this.getStage(), draftBuilder));
    }

    @Override
    protected AppController getController() {
        return new CreateModeSelectController(this);
    }
}
