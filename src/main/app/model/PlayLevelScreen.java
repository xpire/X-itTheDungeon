package main.app.model;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import main.PlayMode;
import main.app.controller.AppController;
import main.app.controller.PlayLevelController;

public class PlayLevelScreen extends AppScreen {

    private final String FILEPATH = "src/asset/level";
    private String filename;
    private int levelNum;

    {
        title = "X-it the Dungeon";
        fxmlName = "main/app/view/playLevelRightAligned.fxml";
    }

    private PlayMode world;
    private PlayLevelController controller;

    public PlayLevelScreen(Stage stage, String filename, int levelNum) {
        super(stage);
        this.filename = filename;
        this.levelNum = levelNum;
        this.controller = new PlayLevelController(this);
    }

    private Scene scene;

    public void restart() {
        Pane layer = controller.getDynamicLayer();
        layer.getChildren().remove(world.getView());
        world = new PlayMode(scene, filename, FILEPATH);
        world.setLevelNum(levelNum);
        layer.getChildren().add(world.getView());
        StackPane.setAlignment(world.getView(), Pos.CENTER);
        world.startGame();
    }

    @Override
    protected void beforeSceneDisplay(Scene scene) {
        this.scene = scene;

        Pane layer = controller.getDynamicLayer();

        world = new PlayMode(scene, filename, FILEPATH);
        world.setLevelNum(levelNum);
        layer.getChildren().add(world.getView());
        StackPane.setAlignment(world.getView(), Pos.CENTER);
        world.startGame();
    }

    @Override
    protected AppController getController() {
        return controller;
    }
}
