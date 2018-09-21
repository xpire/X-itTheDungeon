package main.app;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.shape.Box;
import javafx.stage.Stage;
import main.GameLoop;
import main.GameWorld;
import main.Input;
import main.app.controller.MainMenuController;
import main.app.controller.PauseModeController;
import main.app.controller.PlayModeController;


public class Game{

    final static int WIDTH = 800;
    final static int HEIGHT = 540;

    public static AppState appState = new MainMenuState();
    public static Input input = new Input();
    public static GameWorld world;
    public GameLoop gameLoop;
    public Group previousRoot;
    public PlayModeController ctrlPlay;
    public PauseModeController ctrlPause;
    public MainMenuController ctrlMain;

    public void setPlayModeController(PlayModeController ctrl) {ctrlPlay = ctrl;}
    public PlayModeController getPlayModeController() {return ctrlPlay;}
    public void setPauseModeController(PauseModeController ctrl) {ctrlPause = ctrl;}
    public PauseModeController getPauseModeController() {return ctrlPause;}
    public void setMainMenuController(MainMenuController ctrl) {ctrlMain = ctrl; }
    public MainMenuController getMainMenuController() {return ctrlMain; }

    public Group getPreviousRoot() { return previousRoot; }

    public void runGame(Stage stage, Group root) {
        Scene scene = generateLevel(root);
        stage.setScene(scene);
        stage.show();

        gameLoop = new GameLoop(this, fps -> System.out.println("FPS: " + fps));
        gameLoop.start();
        previousRoot = root;
    }

    public void onBeforeUpdate() {}


    public void onUpdate() {
        world.update();
    }

    public void onAfterUpdate() {
        input.update();
    }

    public void stop() {
        if (gameLoop != null) gameLoop.stop();
    }

    public void resume() {if (gameLoop != null) gameLoop.start();}

    Scene generateLevel(Group root) {
        Scene scene = new Scene(root, WIDTH, HEIGHT);

        // Setup input and World
        final Node inputNode = new Box();
        inputNode.setFocusTraversable(true);
        inputNode.requestFocus();
        inputNode.setOnKeyPressed(input);
        inputNode.setOnKeyReleased(input);

        root.getChildren().add(inputNode);

        world = new GameWorld();
        root.getChildren().add(world.getView());
        return scene;
    }

    public AppState getAppState() {return appState; }
    public void setAppState(AppState state) { appState = state; }
}



