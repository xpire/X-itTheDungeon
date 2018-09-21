package main.app;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.stage.Stage;
import main.app.controller.MainMenuController;
import main.app.controller.PauseModeController;
import main.app.controller.PlayModeController;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("XD - Xit the Dungeon");

        Group root = new Group();

        FXMLLoader mainMenuLoader = new FXMLLoader(getClass().getResource("view/mainMenu.fxml"));
        root = mainMenuLoader.load();

        MainMenuController ctrlMain = mainMenuLoader.getController();
        System.out.println(ctrlMain);

        FXMLLoader playModerLoader = new FXMLLoader(getClass().getResource("view/gamePlay.fxml"));
        root = playModerLoader.load();

        PlayModeController ctrlPlay = playModerLoader.getController();
        System.out.println(ctrlPlay);

        FXMLLoader pauseModeLoader = new FXMLLoader(getClass().getResource("view/PauseScreen.fxml"));
        root = pauseModeLoader.load();

        PauseModeController ctrlPause = pauseModeLoader.getController();
        System.out.println(ctrlPause);

        Game game = new Game();
        ctrlMain.initGame(game, primaryStage);
        ctrlPlay.initGame(game, primaryStage);
        ctrlPause.initGame(game, primaryStage);

        game.getAppState().load(game, primaryStage);
    }
}
