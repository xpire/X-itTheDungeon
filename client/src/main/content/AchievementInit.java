package main.content;

import main.Toast;
import main.app.Main;
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
        addAchievement("The Executioner", "Kill 10 Enemies", 10, IntStat.Key.NUM_ENEMIES_KILLED);
        addAchievement("Caching!", "Collect 1 Treasure", 1, IntStat.Key.NUM_TREASURES_COLLECTED);
        addAchievement("Treasure Hunter", "Collect 7 Treasures", 7, IntStat.Key.NUM_TREASURES_COLLECTED);
        addAchievement("Millionaire $$$", "Collect 20 Treasures", 20, IntStat.Key.NUM_TREASURES_COLLECTED);
        addAchievement("Explorer of the Abyss", "Fall into a Pit", 1, IntStat.Key.NUM_PIT_DEATHS);
        addAchievement("Hawkeye", "Kill 3 Enemies with a Bow", 3, IntStat.Key.NUM_ENEMIES_KILLED_WITH_ARROW);
        addAchievement("Superman", "Kill an Enemy while Invincible", 1, IntStat.Key.NUM_ENEMIES_KILLED_WHEN_INVINCIBLE);
        addAchievement("Bomberman", "Kill 3 Enemies with a Bomb", 3, IntStat.Key.NUM_ENEMIES_KILLED_WITH_BOMB);
        addAchievement("Dumberman", "Kill yourself with a Bomb", 1, IntStat.Key.NUM_BOMB_DEATHS);
        addAchievement("Boulder Destroyer", "Destroy 5 Boulders", 5, IntStat.Key.NUM_BOULDERS_BOMBED);
        addAchievement("Lock Picker", "Open 3 Doors", 3, IntStat.Key.NUM_DOORS_UNLOCKED);
        addAchievement("XD Master", "Beat All Levels", 4, IntStat.Key.MAX_LEVEL_CONQUERED);
    }

    private void addAchievement(String name, String desc, int target, IntStat.Key statType) {
        Achievement a = new Achievement(name, desc, target, stats.get(statType));
        a.isAchieved().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                new Toast.Builder(Main.primaryStage)
                        .msg("Achievement Unlocked: " + a.getName())
                        .toastDelay(1000)
                        .fadeInDelay(500)
                        .fadeOutDelay(1000)
                        .makeToast();
            }
        });

        system.addAchievement(a);
        stats.addListener(statType, a::onUpdate);
    }
}
