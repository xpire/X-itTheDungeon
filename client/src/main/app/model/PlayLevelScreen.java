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
import main.client.util.LocalManager;
import main.events.LevelEvent;

/**
 * Screen for Playing a level
 */
public class PlayLevelScreen extends AppScreen {

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

    /**
     * Basic constructor
     * @param parent : screen which this game from
     * @param stage : the corresponding stage
     * @param filename : name of the level being played
     * @param filePath : path of the level being played
     * @param levelNum : pre-defined level progression index
     * @param isPublishTest : whether or not the level is being played as a pre-publish test
     */
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

    /**
     * Restarting a level within the same screen
     */
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

        if (isPublishTest) addPublishHandler(world, filename);

        layer.getChildren().add(world.getView());
        StackPane.setAlignment(world.getView(), Pos.CENTER);
        world.startGame();
    }

    /**
     * When the back button on the screen is pressed
     */
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

    /**
     * Adds an event handler to the level if it is being evaluated for publishing
     * The event listens for the level's successful completion, and publishes if it hears this
     * @param world : the PlayMode instance
     * @param filename : levels name
     */
    private void addPublishHandler(LevelPlayer world, String filename) {
        if (isPublishTest) {
            world.addEventHandler(LevelEvent.LEVEL_PASSED, e -> {
                LocalManager.LocalDraftAdd(filename, world.getLevel().toFile(filename, "asset/buffer/"));
                controller.switchScreen(new CreateModeSelectScreen(this.getStage()));
            });
        }
    }
}
