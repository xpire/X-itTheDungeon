package main.app.model;

import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.app.controller.AppController;
import main.app.controller.CreateModeSelectController;

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
                     vBox.getChildren().add(new Label("testing"));
                     vBox.getChildren().add(new Label("testing1"));
                     vBox.getChildren().add(new Label("testing2"));

                     Button button = new Button();
                     button.setText("Resume");
                     button.setOnAction(e -> controller.switchScreen(new CreativeLabScreen(this.getStage(), trimmedFileName)));

                     vBox.getChildren().add(button);

                     titledPane.setContent(vBox);

                     draftsView.getPanes().add(titledPane);
                 });
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    protected AppController getController() {
        return new CreateModeSelectController(this);
    }
}
