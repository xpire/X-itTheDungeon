package main.init;

import main.Toast;
import main.app.Main;
import main.content.IntStat;
import main.trigger.achievement.Achievement;
import main.trigger.achievement.AchievementSystem;

public class AchievementInit {

    private AchievementSystem system;
    private IntStat stats;

    public AchievementInit(AchievementSystem system, IntStat stats) {
        this.system = system;
        this.stats  = stats;
    }

    public void init() {
        addAchievement("First Blood", "Kill 1 Enemy", 1, IntStat.Key.NUM_ENEMIES_KILLED);
        addAchievement("Penta Kill", "Kill 5 Enemies", 5, IntStat.Key.NUM_ENEMIES_KILLED);
        addAchievement("Penta Kill 2", "Kill 5 Enemies 2", 5, IntStat.Key.NUM_ENEMIES_KILLED);
        addAchievement("Yom Yom Yom", "Kill 10 Enemies", 10, IntStat.Key.NUM_ENEMIES_KILLED);
        addAchievement("Oopsie Daisie", "Fall into a Pit", 1, IntStat.Key.NUM_PIT_DEATHS);
    }

    private void addAchievement(String name, String desc, int target, IntStat.Key statType) {
        Achievement a = new Achievement(name, desc, target, stats.get(statType));
        a.isAchieved().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                Toast.makeToast(Main.primaryStage, "Achievement Unlocked: " + a.getName(), 1000, 500, 1000);
            }
        });

        system.addAchievement(a);
        stats.addListener(statType, a::onUpdate);
    }
}
