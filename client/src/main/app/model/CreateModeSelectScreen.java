package main.app.model;

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
import java.nio.file.Paths;

public class CreateModeSelectScreen extends AppScreen{

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
                 .forEach(f -> {

                     TitledPane titledPane = new TitledPane();
                     String fileName = f.getFileName().toString();
                     String trimmedFileName = fileName.substring(0, fileName.lastIndexOf('.'));
                     titledPane.setText(trimmedFileName);

                     HBox totalView = new HBox();
                     totalView.setSpacing(10);

//                     ScrollPane viewPane = new ScrollPane();
//                     viewPane.setPrefSize(270, 270);
//                     viewPane.setMaxSize(270,270);

                     DraftBuilder previewBuilder = new DraftBuilder(
                             new MapLoader().loadLevel(trimmedFileName, "src/main/drafts", true));

                     Group preview = previewBuilder.getView();

                     Level level = previewBuilder.getLevel();
                     double scaleFactor = 270.0 / Math.max(level.getWidth(), level.getHeight());

                     level.rescale(30.0 * scaleFactor);

//                     preview.setScaleX(9.0/Math.max(previewBuilder.getNCols(), previewBuilder.getNRows()));
//                     preview.setScaleY(9.0/Math.max(previewBuilder.getNCols(), previewBuilder.getNRows()));
//                     viewPane.setBorder(new Border(new BorderStroke(Color.BLACK,
//                             BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
//                     viewPane.setContent(preview);
//                     viewPane.setPrefViewportWidth(100);

//                     int maxDim = Math.max(previewBuilder.getNCols(), previewBuilder.getNRows());
//                     int prefSize = 9;
//                     double scaleFactor = (double) prefSize/maxDim;
//
//                     System.out.println(previewBuilder.getNCols() + " " + previewBuilder.getNRows() + " " + scaleFactor);
//                     preview.setScaleX(scaleFactor);
//                     preview.setScaleY(scaleFactor);
//                     preview.setTranslateX(-(previewBuilder.getNCols() - prefSize) * 15 * scaleFactor);
//                     preview.setTranslateY(-(previewBuilder.getNRows() - prefSize) * 16.1 * scaleFactor);
//

//                     viewPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
//                     viewPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
//                     totalView.getChildren().add(viewPane);

                     totalView.getChildren().add(preview);

                     VBox optionsList = new VBox();
                     optionsList.setSpacing(10);

                     HBox nameBox = new HBox();
                     Label nameLabel = new Label("Name: ");
                     TextField nameField = new TextField(trimmedFileName);
                     Button okBtn = new Button("Ok");
                     Button renameBtn = new Button("Rename");

                     nameField.setEditable(false);
                     okBtn.setVisible(false);

                     nameField.setOnAction(e -> okBtn.fire());

                     okBtn.setOnAction(e -> {
                         String newName = String.format("%s.txt", nameField.getText());

                         if (!renameFile(f.toFile(), newName)) System.out.println("Rename failed");

                         nameField.setEditable(false);
                         okBtn.setVisible(false);
                         controller.switchScreen(new CreateModeSelectScreen(getStage()));
                     });

                     renameBtn.setOnAction(e -> {
                         nameField.setEditable(true);
                         nameField.requestFocus();
                         okBtn.setVisible(true);
                     });

                     nameBox.getChildren().addAll(nameLabel, nameField, okBtn, renameBtn);

                     Button resumeBtn = new Button();
                     resumeBtn.setText("Resume Working");
                     resumeBtn.setOnAction(e -> controller.switchScreen(
                             new CreativeLabScreen(getStage(),
                             new DraftBuilder(
                             new MapLoader().loadLevel(trimmedFileName, "src/main/drafts", true)))));

                     Button deleteBtn = new Button();
                     deleteBtn.setText("Delete");
                     deleteBtn.setOnAction(e -> {
                         if (!f.toFile().delete()) System.out.println("Delete unsuccessful");
                         controller.switchScreen(new CreateModeSelectScreen(getStage()));
                     });

                     optionsList.getChildren().addAll(nameBox, resumeBtn, deleteBtn);

                     totalView.getChildren().add(optionsList);
                     titledPane.setContent(totalView);

                     draftsView.getPanes().add(titledPane);
                 });
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private boolean renameFile(File f, String newName) {
        return f.renameTo(new File(f.getParentFile(), newName));
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
