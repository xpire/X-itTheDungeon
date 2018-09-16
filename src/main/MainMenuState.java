package main;

import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainMenuState implements ApplicationState {
    @Override
    public void load(Game game, Stage stage) throws Exception {
        //THIS LINE CAUSES IO EXCEPTION IF INVALID INPUT SO PLEASE MAKE NOTE OF THIS
        game.stopGameLoop(game);
        FXMLLoader mainMenuLoader = new FXMLLoader(getClass().getResource("mainMenu.fxml"));
        Group root = mainMenuLoader.load();
        MainMenuController mainMenuController = mainMenuLoader.getController();
        mainMenuController.initGame(game, stage);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
