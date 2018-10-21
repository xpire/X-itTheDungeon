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
        AchievementSystem achievementSystem = Main.locator.getAchievementSystem();
        lvAchievements.setItems(FXCollections.observableArrayList(achievementSystem.getAchievements()));

//        lvAchievements.setCellFactory(lv -> new ListCell<Achievement>() {
//            @Override
//            public void updateItem(Achievement a, boolean empty) {
//                super.updateItem(a, empty);
//                if (empty) {
//                    setText(null);
//                    setGraphic(null);
//                } else {
//                    setText(String.format("%s - %s", a.getName(), a.getDescription()));
//                }
//            }
//        });
    }


    @FXML
    public void onBackBtnPressed() {
        switchScreen(new MainScreen(screen.getStage()));
        soundManager.playSoundEffect("Item");
    }
}
