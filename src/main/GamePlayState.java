package main;

import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.shape.Box;
import javafx.stage.Stage;
import main.maploading.Level;

import java.util.ArrayList;

public class GamePlayState implements ApplicationState{

    final static int WIDTH = 800;
    final static int HEIGHT = 540;

    @Override
    public void load(Game game, Stage stage) throws Exception {
//        Scene scene = generateLevel(game);
//        stage.setScene(scene);
//        stage.show();
//        game.startGameLoop(game);
        FXMLLoader gamePlayLoader = new FXMLLoader(getClass().getResource("gamePlay.fxml"));
        Group root = gamePlayLoader.load();
        GamePlayController gamePlayController = gamePlayLoader.getController();
        gamePlayController.initGame(game, stage);

        game.StartGame(stage, root);
//        return game.generateLevel();
    }



}
