package main.app.model;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import main.app.controller.AppController;
import main.app.controller.CreateModeSelectController;
import main.maploading.DraftBuilder;
import main.maploading.MapLoader;

import javax.xml.soap.Text;
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

                     VBox vBox = new VBox();

                     HBox nameBox = new HBox();
                     Label nameLabel = new Label("Name: ");
                     TextField nameField = new TextField(trimmedFileName);
                     Button okBtn = new Button("Ok");
                     Button renameBtn = new Button("Rename Working");

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
                     resumeBtn.setText("Resume");
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

                     vBox.getChildren().addAll(nameBox, resumeBtn, deleteBtn);
                     titledPane.setContent(vBox);
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
