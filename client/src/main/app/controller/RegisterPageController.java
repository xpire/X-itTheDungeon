package main.app.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import main.app.Main;
import main.app.model.AppScreen;
import main.app.model.MainScreen;
import main.app.model.RegisterScreen;
import main.sound.SoundManager;

public class RegisterPageController extends AppController {
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
