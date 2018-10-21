package main.app.model;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.app.controller.AppController;

import java.io.IOException;

/**
 * Abstract class for the Sceens of the application
 */
public abstract class AppScreen {

    protected Stage stage;
    protected FXMLLoader fxmlLoader;

    protected String title = "X-it the Dungeon";
    protected String fxmlName = "";

    /**
     * Basic constructor
     * @param stage : corresponding stage
     */
    public AppScreen(Stage stage) {
        this.stage = stage;
    }

    /**
     * Initialises the screen
     */
    public void start() {
        stage.setTitle(title);
        initLoader();

        try {
            Parent root = fxmlLoader.load();
            Scene sc = new Scene(root, 960, 640);

            beforeSceneDisplay(sc);

            stage.setScene(sc);
            stage.show();

            afterSceneDisplay();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Actions before a scene is displayed on the screen
     * @param scene : corresponding scene
     */
    protected void beforeSceneDisplay(Scene scene){}

    /**
     * Actions after a scene is displayed on the screen
     */
    protected void afterSceneDisplay(){}

    /**
     * Actions when the screen is destroyedd
     */
    public void onDestroyed(){}

    /**
     * Loads the associated fxml page
     */
    private void initLoader() {
        fxmlLoader = new FXMLLoader(
                getClass().getClassLoader().getResource(fxmlName));
        fxmlLoader.setController(getController());
    }

    /**
     * Getter for the controller corresponding to the screen
     * @return the corresponding controller
     */
    protected abstract AppController getController();

    /**
     * Getter for the stage
     * @return : the corresponding stage
     */
    public Stage getStage() {
        return stage;
    }
}
