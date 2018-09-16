package main.app.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import main.app.Game;
import main.app.MainMenuState;

public class PlayModeController {

    private Game game;
    private Stage stage;

    @FXML
    private Button btnExit;

    public void initGame(Game g, Stage s) {
        if (game != null)
            throw new IllegalStateException("Game can only be initialised once");

        this.game  = g;
        this.stage = s;
        btnExit.setFocusTraversable(false);
    }

    @FXML
    private void goToMainMenu(ActionEvent event) throws Exception {
        System.out.println("Proceed to Main Menu");

        game.setAppState(new MainMenuState());
        game.getAppState().load(game, stage);
    }
}
