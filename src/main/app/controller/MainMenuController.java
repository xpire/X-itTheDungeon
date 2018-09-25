package main.app.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import main.app.Game;
import main.app.PlayModeState;

public class MainMenuController {

    private Game game;
    private Stage stage;

    public void initGame(Game g, Stage s) {
        if (this.game != null)
            throw new IllegalStateException("Game can only be initialised once");

        this.game = g;
        this.stage = s;
    }


    @FXML
    private void goToGamePlay(ActionEvent event) throws Exception {
        System.out.println("Proceed to game play");
        game.setAppState(new PlayModeState());
        game.getAppState().load(game, stage);
    }
}
