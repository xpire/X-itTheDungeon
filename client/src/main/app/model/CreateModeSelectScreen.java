package main.app.model;

import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.app.controller.AppController;
import main.app.controller.CreateModeSelectController;
import main.maploading.DraftBuilder;
import main.maploading.MapLoader;

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

                     Button button = new Button();
                     button.setText("Resume");
                     button.setOnAction(e -> controller.switchScreen(
                             new CreativeLabScreen(this.getStage(),
                             new DraftBuilder(
                             new MapLoader().loadLevel(trimmedFileName, "src/main/drafts", true)))));

                     vBox.getChildren().add(button);

                     titledPane.setContent(vBox);

                     draftsView.getPanes().add(titledPane);
                 });
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void initialiseNewDraft() {
        DraftBuilder draftBuilder = new DraftBuilder(8, 8, "newDraft");
        controller.switchScreen(new CreativeLabScreen(this.getStage(), draftBuilder));
    }

    @Override
    protected AppController getController() {
        return new CreateModeSelectController(this);
    }
}
