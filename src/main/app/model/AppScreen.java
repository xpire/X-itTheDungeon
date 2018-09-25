package main.app.model;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.app.controller.AppController;

import java.io.IOException;

public abstract class AppScreen {

    protected Stage stage;
    protected FXMLLoader fxmlLoader;

    protected String title = "";
    protected String fxmlName = "";

    public AppScreen(Stage stage) {
        this.stage = stage;
    }

    public void start() {
        stage.setTitle(title);
        initLoader();

        try {
            Parent root = fxmlLoader.load();
            Scene sc = new Scene(root, 960, 640);

            beforeSceneDisplay();

            stage.setScene(sc);
            stage.show();

            afterSceneDisplay();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void beforeSceneDisplay(){}
    protected void afterSceneDisplay(){}

    public void onDestroyed(){}

    private void initLoader() {
        fxmlLoader = new FXMLLoader(
                getClass().getClassLoader().getResource(fxmlName));
        fxmlLoader.setController(getController());
    }

    protected abstract AppController getController();

    public Stage getStage() {
        return stage;
    }
}
