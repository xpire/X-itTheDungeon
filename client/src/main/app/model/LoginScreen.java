package main.app.model;

import javafx.stage.Stage;
import main.app.controller.AppController;
import main.app.controller.LoginPageController;

/**
 * Screen for the login page
 */
public class LoginScreen extends AppScreen {

    {
        title = "Sign in";
        fxmlName = "main/app/view/loginPage.fxml";
    }

    /**
     * Generic constructor
     * @param stage : corresponding screen
     */
    public LoginScreen(Stage stage) { super(stage); }

    @Override
    protected AppController getController() { return new LoginPageController(this); }
}
