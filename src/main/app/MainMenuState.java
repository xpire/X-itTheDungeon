package main.app;

import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.app.controller.MainMenuController;


public class MainMenuState implements AppState {

    @Override
    public void load(Game game, Stage stage) throws Exception {

        game.stop();

        FXMLLoader mainMenuLoader = new FXMLLoader(getClass().getResource("view/mainMenu.fxml"));
        Group root = mainMenuLoader.load();

        MainMenuController ctrlMain = mainMenuLoader.getController();
        ctrlMain.initGame(game, stage);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
