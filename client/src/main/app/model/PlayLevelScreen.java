package main.app.model;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import main.app.controller.AppController;
import main.app.controller.PlayLevelController;
import main.LevelPlayer;


public class PlayLevelScreen extends AppScreen {

//    private final String FILEPATH = "src/asset/level";
    private String filePath;
    private String filename;
    private AppScreen parentScreen;
    private int levelNum;
    private boolean isPublishTest;

    {
        title = "X-it the Dungeon";
        fxmlName = "main/app/view/playLevel.fxml";
    }

    private LevelPlayer world;
    private PlayLevelController controller;

    public PlayLevelScreen(AppScreen parent, Stage stage, String filename, String filePath, int levelNum, boolean isPublishTest) {
        super(stage);
        this.parentScreen = parent;
        this.filename = filename;
        this.filePath = filePath;
        this.levelNum = levelNum;
        this.isPublishTest = isPublishTest;
        this.controller = new PlayLevelController(this);
    }

    private Scene scene;

    public void restart() {
        Pane layer = controller.getDynamicLayer();

        if (world != null)
            layer.getChildren().remove(world.getView());

        world = new LevelPlayer.Builder(filename, filePath)
                .input(scene)
                .levelNum(levelNum)
                .build();

        layer.getChildren().add(world.getView());
        StackPane.setAlignment(world.getView(), Pos.CENTER);
        world.startGame();
    }

    public void backBtnPressed() {
        controller.switchScreen(parentScreen);
    }

    @Override
    protected void beforeSceneDisplay(Scene scene) {
        this.scene = scene;
        restart();
    }

    @Override
    protected AppController getController() {
        return controller;
    }
//
//    private void addPublishHandler(LevelPlayer world, String filename) {
//        if (isPublishTest) {
//            world.addEventHandler(LevelEvent.LEVEL_PASSED, e -> {
//                LocalManager.LocalDraftAdd(filename, world.getLevel().toFile(filename, "asset/buffer/"));
//                controller.switchScreen(new CreateModeSelectScreen(this.getStage()));
//            });
//        }
//    }
}
