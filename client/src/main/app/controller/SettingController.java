package main.app.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import main.app.model.SettingScreen;
import main.sound.SoundManager;

/**
 * Controller for the Settings screen
 */
public class SettingController extends AppController<SettingScreen> {

    private SoundManager soundManager = SoundManager.getInstance(5);

    /**
     * Generic constructor
     * @param screen : the corresponding screen
     */
    public SettingController(SettingScreen screen) {
        super(screen);
    }

    @FXML
    private Slider sliderFX;

    @FXML
    private Slider sliderBGM;

    @FXML
    public void initialize() {
        sliderFX.setMax(100);
        sliderFX.setMin(0);
        sliderFX.setValue(soundManager.getFXVolume()*100);

        sliderBGM.setMax(100);
        sliderBGM.setMin(0);
        sliderBGM.setValue(soundManager.getBGMVolume()*100);

        sliderBGM.valueProperty().addListener((observable, oldValue, newValue) -> {
            soundManager.setBGMVolume(newValue.doubleValue()/100);
            System.out.printf("bgm: %f\n", newValue.doubleValue());
        });

        sliderFX.valueProperty().addListener((observable, oldValue, newValue) -> {
            soundManager.setFXVolume(newValue.doubleValue()/100);
            System.out.printf("fx: %f\n", newValue.doubleValue());
        });


    }

    @FXML
    public void onBackBtnPressed() {
        switchScreen(screen.getParent());
        soundManager.playSoundEffect("Item");
    }

}
