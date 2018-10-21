package main.app.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import main.app.Main;
import main.app.model.AppScreen;
import main.app.model.MainScreen;
import main.app.model.RegisterScreen;
import main.sound.SoundManager;

/**
 * This class controls the login page of the controller
 */
public class LoginPageController extends AppController {
    public LoginPageController(AppScreen screen) { super(screen); }


    private SoundManager soundManager = SoundManager.getInstance(5);
    @FXML
    private Label response;

    @FXML
    private Button btnLogin;

    @FXML
    private TextField InputName;
    @FXML
    private PasswordField InputPassword;

    @FXML
    public void initialize() { }

    // TODO Change the handle here for exception
    // TODO Change the handle input and alerting
    @FXML
    public void onLoginBtnPressed() {
        soundManager.playSoundEffect("Item");
        response.setText(Main.currClient
                .attemptLogin(
                InputName.getText(),
                InputPassword.getText()
        ));
    }

    @FXML
    public void onBackBtnPressed() {
        switchScreen(new MainScreen(screen.getStage()));
        soundManager.playSoundEffect("Item");
    }

    @FXML
    public void onRegisterBtnPressed() {
        switchScreen(new RegisterScreen(screen.getStage()));
        soundManager.playSoundEffect("Item");
    }
}
