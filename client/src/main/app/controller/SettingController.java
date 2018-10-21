package main.app.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Slider;
import main.app.Main;
import main.app.model.MainScreen;
import main.app.model.SettingScreen;
import main.sound.SoundManager;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingController extends AppController<SettingScreen> {

    private SoundManager soundManager = SoundManager.getInstance(5);

    public SettingController(SettingScreen screen) {
        super(screen);
    }

    @FXML
    private Slider sliderFX; //=new Slider(0,100,100);

    @FXML
    private Slider sliderBGM;// = new Slider(0,100,100);



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
