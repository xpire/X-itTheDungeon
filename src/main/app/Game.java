package main.app;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.shape.Box;
import javafx.stage.Stage;
import main.GameLoop;
import main.GameWorld;
import main.Input;


public class Game{

    final static int WIDTH = 800;
    final static int HEIGHT = 540;

    public static AppState appState = new MainMenuState();
    public static Input input = new Input();
    public static GameWorld world;
    public GameLoop gameLoop;


    public void runGame(Stage stage, Group root) {
        Scene scene = generateLevel(root);
        stage.setScene(scene);
        stage.show();

        gameLoop = new GameLoop(this, fps -> System.out.println("FPS: " + fps));
        gameLoop.start();
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



