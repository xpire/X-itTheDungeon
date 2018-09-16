package main.app;

import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.stage.Stage;
import main.app.controller.PlayModeController;


public class PlayModeState implements AppState{

    @Override
    public void load(Game game, Stage stage) throws Exception {

        FXMLLoader gamePlayLoader = new FXMLLoader(getClass().getResource("view/gamePlay.fxml"));
        Group root = gamePlayLoader.load();

        PlayModeController controller = gamePlayLoader.getController();
        controller.initGame(game, stage);

        game.runGame(stage, root);
    }
}
