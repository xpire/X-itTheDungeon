package main;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainMenuBehaviour implements ApplicationBehaviour {
    @Override
    public Scene load() throws Exception {
        //THIS LINE CAUSES IO EXCEPTION IF INVALID INPUT SO PLEASE MAKE NOTE OF THIS
        Parent root = FXMLLoader.load(getClass().getResource("mainMenu.fxml"));
        return new Scene(root);
    }
}
