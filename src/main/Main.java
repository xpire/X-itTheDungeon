package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("XD - Xit the Dungeon");
        Group root = new Group();
        FXMLLoader mainMenuLoader = new FXMLLoader(getClass().getResource("mainMenu.fxml"));
        root = mainMenuLoader.load();
        MainMenuController mainMenuController = mainMenuLoader.getController();
        System.out.println(mainMenuController);
        FXMLLoader gamePlayLoader = new FXMLLoader(getClass().getResource("gamePlay.fxml"));
        root = gamePlayLoader.load();
        GamePlayController gamePlayController = gamePlayLoader.getController();
        System.out.println(gamePlayController);
        Game game = new Game();
        mainMenuController.initGame(game, primaryStage);
        gamePlayController.initGame(game, primaryStage);
        game.getAppState().load(game, primaryStage);
//        Scene scene = new Scene(root, 800, 540);
//        primaryStage.setScene(scene);
//        primaryStage.show();
    }
}
