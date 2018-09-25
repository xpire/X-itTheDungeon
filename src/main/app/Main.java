package main.app;


import javafx.application.Application;
import javafx.stage.Stage;
import main.app.model.MainScreen;

public class Main extends Application {

    @Override
    public void start(Stage s) throws Exception{

        s.setWidth(960);
        s.setHeight(640);

        MainScreen sc = new MainScreen(s);
        sc.start();
    }


    public static void main(String[] args) {
        launch(args);
    }



    //    @Override
//    public void start(Stage primaryStage) throws Exception {
//
//        primaryStage.setTitle("XD - Xit the Dungeon");
//
//        Group root = new Group();
//
//        FXMLLoader mainMenuLoader = new FXMLLoader(getClass().getResource("view/mainMenu.fxml"));
//        root = mainMenuLoader.load();
//
//        MainMenuController ctrlMain = mainMenuLoader.getController();
//        System.out.println(ctrlMain);
//
//        FXMLLoader playModerLoader = new FXMLLoader(getClass().getResource("view/playModeSelect.fxml"));
//        root = playModerLoader.load();
//
//        PlayModeController ctrlPlay = playModerLoader.getController();
//        System.out.println(ctrlPlay);
//
//        Game game = new Game();
//        ctrlMain.initGame(game, primaryStage);
//        ctrlPlay.initGame(game, primaryStage);
//
//        game.getAppState().load(game, primaryStage);
//    }
}
