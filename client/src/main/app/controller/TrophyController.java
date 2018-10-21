package main.app.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import main.app.Main;
import main.app.model.*;
import main.sound.SoundManager;
import main.trigger.achievement.Achievement;
import main.trigger.achievement.AchievementSystem;

import java.util.ArrayList;
import java.util.Iterator;

public class TrophyController extends AppController<TrophyScreen> {

    @FXML
    private ListView<Achievement> lvAchievements;

    public TrophyController(TrophyScreen screen) {
        super(screen);
    }

    private SoundManager soundManager = SoundManager.getInstance(5);

    @FXML
    public void initialize() {
        lvAchievements.setItems(FXCollections.observableArrayList(Main.achievementSystem.getAchievements()));
    }


    @FXML
    public void onBackBtnPressed() {
        switchScreen(new MainScreen(screen.getStage()));
        soundManager.playSoundEffect("Item");
    }
}
