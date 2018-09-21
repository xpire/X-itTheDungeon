package main.app.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import main.app.Game;
import main.app.MainMenuState;
import main.app.PlayModeState;
import main.app.ResumeModeState;

public class PauseModeController {

    private Game game;
    private Stage stage;

    public void initGame(Game g, Stage s) {
        if (this.game != null)
            throw new IllegalStateException("Game can only be initialised once");

        this.game = g;
        this.stage = s;
        game.setPauseModeController(this);
    }

    @FXML
    private void goToGamePlay(ActionEvent event) throws Exception {
        System.out.println("Resume game play");
        game.setAppState(new ResumeModeState());
        game.getAppState().load(game, stage);
    }

    @FXML
    private void goToMainMenu(ActionEvent event) throws Exception {
        System.out.println("Proceed to Main Menu");
        game.setAppState(new MainMenuState());
        game.getAppState().load(game, stage);
    }
}
