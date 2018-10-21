package main.app.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
<<<<<<< HEAD
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;
import javafx.util.Callback;
=======
import javafx.scene.control.ListView;
>>>>>>> app.controller package javadoc done
import main.app.Main;
import main.app.model.*;
import main.sound.SoundManager;
import main.trigger.achievement.Achievement;
import main.trigger.achievement.AchievementSystem;

/**
 * Controller for the Achievements screen
 */
public class TrophyController extends AppController<TrophyScreen> {

    @FXML
    private ListView<Achievement> lvAchievements;

    /**
     * Generic constructor
     * @param screen : the corresponding screen
     */
    public TrophyController(TrophyScreen screen) {
        super(screen);
    }

    private SoundManager soundManager = SoundManager.getInstance(5);

    @FXML
    public void initialize() {
<<<<<<< HEAD
        lvAchievements.setItems(FXCollections.observableArrayList(Main.achievementSystem.getAchievements()));
        lvAchievements.setCellFactory(view ->
            new ListCell<Achievement>() {
                @Override
                public void updateItem(Achievement item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null) {
                        setText(item.toString());
                        if (item.isAchieved().get())
                            setTextFill(Color.GREEN);
                    }
                }
        });
=======
        AchievementSystem achievementSystem = Main.locator.getAchievementSystem();
        lvAchievements.setItems(FXCollections.observableArrayList(achievementSystem.getAchievements()));
>>>>>>> app.controller package javadoc done
    }

    @FXML
    public void onBackBtnPressed() {
        switchScreen(new MainScreen(screen.getStage()));
        soundManager.playSoundEffect("Item");
    }
}
