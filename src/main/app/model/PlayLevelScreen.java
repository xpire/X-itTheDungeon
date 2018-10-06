package main.app.model;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import main.PlayMode;
import main.app.controller.AppController;
import main.app.controller.PlayLevelController;
import main.maploading.MapLoader;

public class PlayLevelScreen extends AppScreen {

    {
        title = "X-it the Dungeon";
        fxmlName = "main/app/view/playLevel.fxml";
    }

    private PlayMode world;
    private PlayLevelController controller;

    public PlayLevelScreen(Stage stage) {
        super(stage);
        this.controller = new PlayLevelController(this);
    }


    @Override
    protected void beforeSceneDisplay(Scene scene) {
        Pane layer = controller.getDynamicLayer();

        MapLoader loader = new MapLoader();
        world = new PlayMode(scene, loader.loadLevel("map1.1", "drafts"));
        layer.getChildren().add(world.getView());
        world.startGame();
    }

    @Override
    protected AppController getController() {
        return controller;
    }
}
