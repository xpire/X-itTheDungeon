package main.app;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ResumeModeState implements AppState {
    @Override
    public void load(Game game, Stage stage) throws Exception {
        Group root = game.getPreviousRoot();
        stage.setScene(new Scene(root));
//        Scene scene = stage.getScene();
//        scene.setRoot(root);
//        stage.setScene(scene);
        stage.show();

        game.resume();

        game.setAppState(new PlayModeState());
    }
}
