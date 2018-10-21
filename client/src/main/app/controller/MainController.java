package main.app.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import main.app.Main;
import main.app.engine.AlertHelper;
import main.app.model.*;
import main.client.util.LocalManager;
import main.sound.SoundManager;
import javafx.scene.control.Alert;

/**
 * Controller class for the main menu screen of the application
 */
public class MainController extends AppController  {

    private SoundManager soundManager = SoundManager.getInstance(5);

    @FXML
    Button btnLogin;

    @FXML
    Button btnLogout;

    /**
     * Generic constructor
     * @param screen : the corresponding screen
     */
    public MainController(MainScreen screen) {
         super(screen);
    }

    @FXML
    public void initialize() {
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
        soundManager.playSoundEffect("Item");
    }

    @FXML
    public void onLoginBtnPressed() {
        switchScreen(new LoginScreen(screen.getStage()));
        soundManager.playSoundEffect("Item");
    }


    @FXML
    public void onLogoutBtnPressed() {
        LocalManager.saveConfig();
        LocalManager.uploadConfig();
        AlertHelper.showAlert(Alert.AlertType.INFORMATION, "Hello!!!", Main.currClient.attemptLogout());
        btnLogin.setVisible(true);
        btnLogout.setVisible(false);
        soundManager.playSoundEffect("Item");
        LocalManager.loadConfig();
    }

    @FXML
    public void onHelpBtnPressed() {
        switchScreen(new HelpManualScreen(screen.getStage(), screen));
        soundManager.playSoundEffect("Item");
    }

    @FXML
    public void onSettingBtnPressed() {
        switchScreen(new SettingScreen(screen.getStage(), screen));
        soundManager.playSoundEffect("Item");
    }

    @FXML
    public void onExitBtnPressed() {
        soundManager.playSoundEffect("Item");
        screen.getStage().close();
    }
}