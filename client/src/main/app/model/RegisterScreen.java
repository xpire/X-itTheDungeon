package main.app.model;

import javafx.stage.Stage;
import main.app.controller.AppController;
import main.app.controller.RegisterPageController;

/**
 * Screen for the Registration page
 */
public class RegisterScreen extends AppScreen {
    {
        title = "Register";
        fxmlName = "main/app/view/registerPage.fxml";
    }

    /**
     * Generic constructor
     * @param stage : corresponding stage
     */
    public RegisterScreen(Stage stage) { super(stage); }

    @Override
    protected AppController getController() { return new RegisterPageController( this); }
}
