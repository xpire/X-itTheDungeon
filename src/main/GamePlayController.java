package main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.awt.*;

public class GamePlayController {

    private Game game;
    private Stage stage;

    @FXML
    private Button Exit;

    public void initGame(Game g, Stage s) {
        if (this.game != null) {
            throw new IllegalStateException("Game can only be initialised once");
        }
        this.game = g;
        this.stage = s;
        Exit.setFocusTraversable(false);
    }

    @FXML
    private void GoToMainMenu(ActionEvent event) throws Exception {
        System.out.println("Proceed to Main Menu");
        game.setAppState(new MainMenuState());
        game.getAppState().load(game, stage);
    }
}

