package main.app.model;

import javafx.stage.Stage;
import main.app.controller.AppController;
import main.app.controller.RegisterPageController;

public class RegisterScreen extends  AppScreen {
    {
        title = "Register";
        fxmlName = "main/app/view/registerPage.fxml";
    }

    public RegisterScreen(Stage stage) { super(stage); }

    @Override
    protected AppController getController() { return new RegisterPageController( this); }
}
