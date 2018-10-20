package main.app.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import main.app.Main;
import main.app.engine.AlertHelper;
import main.app.model.CreateModeSelectScreen;
import main.app.model.MainScreen;
import main.app.model.LoginScreen;
import main.app.model.PlayModeSelectScreen;
import main.app.model.TrophyScreen;
import main.sound.SoundManager;
import javafx.scene.control.Alert;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController extends AppController implements Initializable  {

//    @FXML
//    private Button btnStart;
    SoundManager soundManager = SoundManager.getInstance(5);

    @FXML
    Button btnLogin;

    @FXML
    Button btnLogout;

    public MainController(MainScreen screen) {
         super(screen);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (Main.currClient.isLoggedin()) {
            btnLogin.setVisible(false);
            btnLogout.setVisible(true);
        }
        else {
            btnLogin.setVisible(true);
            btnLogout.setVisible(false);
        }
    }

    @FXML
    public void onPlayBtnPressed() {
        switchScreen(new PlayModeSelectScreen(screen.getStage()));
        soundManager.playSoundEffect("Item");
    }

    @FXML
    public void onMakeBtnPressed() {
        switchScreen(new CreateModeSelectScreen(screen.getStage()));
        soundManager.playSoundEffect("Item");
    }

    @FXML
    public void onTrophyBtnPressed() {
        switchScreen(new TrophyScreen(screen.getStage()));
    }

    @FXML
    public void onLoginBtnPressed() { switchScreen(new LoginScreen(screen.getStage())); }

    @FXML
    public void onLogoutBtnPressed() {
        AlertHelper.showAlert(Alert.AlertType.INFORMATION,"Hello!!!", Main.currClient.attemptLogout());
        btnLogin.setVisible(true);
        btnLogout.setVisible(false);
    }
}