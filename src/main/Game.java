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
        world.update(delta);


    }


    // After a set of updates
    public void onAfterUpdate() {
        world.render();
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

//        Circle circle = new Circle();
//        circle.setRadius(100);
//        circle.setCenterX(100);
//        circle.setCenterY(100);
//        root.getChildren().add(circle);

        Level map = new Level(16, 16, 30);

        world = new GameWorld(map);


//        root.getChildren().add(new AnchorPane())
//        main.avatar.Avatar avatar = new main.avatar.Avatar();
//        root.getChildren().add(avatar.getView());
//
//        Wall wall = new Wall(230, 320);
//        root.getChildren().add(wall.getView());


        root.getChildren().add(world.getView());

        stage.setScene(scene);
        stage.show();

        GameLoop loop = new GameLoop(this, fps -> System.out.println("FPS: " + fps));
        loop.start();
    }
}
