package main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class Controller {

    @FXML
    private Label mainMenuLabel1;

    @FXML
    private Button levelButton;

    @FXML
    private void handleButton1Action(ActionEvent event) {
        System.out.println("Proceed to Gameplay");
        Button b = (Button) event.getSource();
        Game.getInstance().StartGame(Game.getInstance().getStageInstance());

    }
}
