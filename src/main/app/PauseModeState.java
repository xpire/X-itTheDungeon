package main.app;

import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.app.controller.PauseModeController;

public class PauseModeState implements AppState {

    @Override
    public void load(Game game, Stage stage) throws Exception {
        game.stop();
        FXMLLoader pauseModeLoader = new FXMLLoader(getClass().getResource("view/PauseScreen.fxml"));
        Group root = pauseModeLoader.load();

        PauseModeController controller = pauseModeLoader.getController();
        controller.initGame(game, stage);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
