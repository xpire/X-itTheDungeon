package main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class MainMenuController {

    private Game game;
    private Stage stage;

    @FXML
    private Label mainMenuLabel1;

    @FXML
    private Button levelButton;

    public void initGame(Game g, Stage s) {
        if (this.game != null) {
            throw new IllegalStateException("Game can only be initialised once");
        }
        this.game = g;
        this.stage = s;
    }

    @FXML
    private void GoToGamePlay(ActionEvent event) throws Exception {
        System.out.println("Proceed to Gameplay");
        game.setAppState(new GamePlayState());
        game.getAppState().load(game, stage);
    }
}
