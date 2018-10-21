package main.app.model;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import main.LevelPlayer;
import main.app.Main;
import main.app.controller.AppController;
import main.app.controller.PlayLevelController;


public class PlayLevelScreen extends AppScreen {

//    private final String FILEPATH = "src/asset/level";
    private String filePath;
    private String filename;
    private AppScreen parentScreen;
    private Scene scene;
    private int levelNum;

    private boolean isPublishTest;

    private String title;
    private String subtitle;

    {
        title = "X-it the Dungeon";
        fxmlName = "main/app/view/playLevel.fxml";
    }

    private LevelPlayer world;
    private PlayLevelController controller;

    public PlayLevelScreen(AppScreen parent, Stage stage, String filename, String filePath, int levelNum, boolean isPublishTest) {
        this(parent, stage, filename, filePath, levelNum, isPublishTest, filename, "Level " + levelNum);
    }

    public PlayLevelScreen(AppScreen parent, Stage stage, String filename, String filePath, int levelNum, boolean isPublishTest, String title, String subtitle) {
        super(stage);
        this.parentScreen = parent;
        this.filename = filename;
        this.filePath = filePath;
        this.levelNum = levelNum;
        this.isPublishTest = isPublishTest;
        this.controller = new PlayLevelController(this);
        this.title = title;
        this.subtitle = subtitle;
    }

    public void restart() {
        Pane layer = controller.getDynamicLayer();

        if (world != null)
            layer.getChildren().remove(world.getView());

        world = new LevelPlayer.Builder(filename, filePath)
                .input(scene)
                .levelNum(levelNum)
                .title(title)
                .subtitle(subtitle)
                .addBus(Main.achievementSystem.getBus())
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
}
