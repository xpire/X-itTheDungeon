package main.app.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import main.app.Main;
import main.app.model.AppScreen;
import main.app.model.MainScreen;
import main.sound.SoundManager;

/**
 * Controller for the Register Page screen
 */
public class RegisterPageController extends AppController {

    /**
     * Generic constructor
     * @param screen : the corresponding screen
     */
    public RegisterPageController(AppScreen screen) { super(screen); }

    private SoundManager soundManager = SoundManager.getInstance(5);

    @FXML
    private Label response;

    @FXML
    private TextField InputName;

    @FXML
    private PasswordField InputPassword;

    @FXML
    public void onBackBtnPressed() {
        switchScreen(new MainScreen(screen.getStage()));
        soundManager.playSoundEffect("Item");
    }

    @FXML
    public void onRegisterBtnPressed() {
        soundManager.playSoundEffect("Item");
        response.setText(Main.currClient
                .attemptRegister(
                        InputName.getText(),
                        InputPassword.getText()
                )
        );
    }

}
