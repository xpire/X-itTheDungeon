package main;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.shape.Box;
import javafx.stage.Stage;
import main.maploading.Level;


public class Game extends Application{


    final static int WIDTH = 800;
    final static int HEIGHT = 540;

    public static Input input = new Input();
    public static GameWorld world;


    private Level map;


    public static void main(String[] args) {
        launch(args);
    }


    // Before a set of updates
    public void onBeforeUpdate() {
        // Handle inputs later here
        // since they may be ignored when onUpdate doesn't get called
        input.processInputs();
    }

    public void onUpdate(double delta) {
        world.update();
    }


    // After a set of updates
    public void onAfterUpdate() {
        input.update();

    }


    public Level getMap() {
        return map;
    }


    @Override
    public void start(Stage stage) {

        stage.setTitle("Basic JavaFX demo");

        Group root = new Group();
        Scene scene = new Scene(root, WIDTH, HEIGHT);


        final Node inputNode = new Box();
        inputNode.setFocusTraversable(true);
        inputNode.requestFocus();
        inputNode.setOnKeyPressed(input);
        inputNode.setOnKeyReleased(input);

        root.getChildren().add(inputNode);


        world = new GameWorld();
        root.getChildren().add(world.getView());


//        Level map = new Level(16, 16, 30);
//        ArrayList<String> obj = new ArrayList<>();
//        obj.add("B");
//        obj.add("D");
//        map.setObj(obj);


        stage.setScene(scene);
        stage.show();


        GameLoop loop = new GameLoop(this, fps -> System.out.println("FPS: " + fps));
        loop.start();
    }
}
