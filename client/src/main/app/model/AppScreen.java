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
            System.out.println("before load");
            Parent root = fxmlLoader.load();
            System.out.println("after load");
            Scene sc = new Scene(root, 960, 640);

            beforeSceneDisplay(sc);

            stage.setScene(sc);
            stage.show();

            afterSceneDisplay();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void beforeSceneDisplay(Scene scene){}
    protected void afterSceneDisplay(){}

    public void onDestroyed(){}

    private void initLoader() {
        System.out.println(fxmlName);
        fxmlLoader = new FXMLLoader(
                getClass().getClassLoader().getResource(fxmlName));
        fxmlLoader.setController(getController());
    }

    protected abstract AppController getController();

    public Stage getStage() {
        return stage;
    }
}
