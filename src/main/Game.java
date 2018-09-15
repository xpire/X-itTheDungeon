package main;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.shape.Box;
import javafx.stage.Stage;
import main.maploading.Level;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


public class Game extends Application{


    final static int WIDTH = 800;
    final static int HEIGHT = 540;

    public static Input input = new Input();
    public static GameWorld world;
    public static ApplicationBehaviour appBehaviour = new MainMenuBehaviour();
    public static Stage stageInstance;

    public void setStageInstance(Stage stage) { Game.stageInstance = stage; }
    public Stage getStageInstance() {return Game.stageInstance; }

    private Level map;

    public Level getMap() { return map; }
    public void setApplicationBehaviour(ApplicationBehaviour a) { appBehaviour = a; }


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


    private static Game instance;
    public Game() {
        instance = this;
    }
    //static method to get instance of game
    public static Game getInstance() {
        return instance;
    }

    @Override
    public void start(Stage stage) throws Exception {
        setStageInstance(stage);
        stage.setTitle("XD - Xit the Dungeon");
//        Parent root;
//        try {
//            root = FXMLLoader.load(getClass().getResource("mainMenu.fxml"));
//        } catch (IOException e) {
//            System.out.println("ggwp");
//            e.printStackTrace();
//            return;
//        }
        Scene scene = appBehaviour.load();
        stage.setScene(scene);
        stage.show();
//        try {
//            TimeUnit.SECONDS.sleep(5);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

//        StartGame(stage);
    }

    public void StartGame(Stage stage) {
        Scene scene = generateLevel();
        stage.setScene(scene);
        stage.show();
        startGameLoop(this);
    }

    Scene generateLevel() {
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
        ArrayList<String> obj = new ArrayList<>();
        obj.add("B");
        obj.add("D");
        map.setObj(obj);

        world = new GameWorld(map);


//        root.getChildren().add(new AnchorPane())
//        main.avatar.Avatar avatar = new main.avatar.Avatar();
//        root.getChildren().add(avatar.getView());
//
//        Wall wall = new Wall(230, 320);
//        root.getChildren().add(wall.getView());


        root.getChildren().add(world.getView());
        return scene;
    }

    public void startGameLoop(Game g) {
        GameLoop loop = new GameLoop(this, fps -> System.out.println("FPS: " + fps));
        loop.start();
    }
}
