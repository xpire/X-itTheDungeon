package main.init;

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
        addAchievement(new Achievement("First Blood", "Kill 1 Enemy", 1), IntStat.Key.NUM_ENEMIES_KILLED);
        addAchievement(new Achievement("Penta Kill", "Kill 5 Enemies", 5), IntStat.Key.NUM_ENEMIES_KILLED);
        addAchievement(new Achievement("Yom Yom Yom", "Kill 10 Enemies", 10), IntStat.Key.NUM_ENEMIES_KILLED);

        addAchievement(new Achievement("Oopsie Daisie", "Fall into a Pit", 1), IntStat.Key.NUM_PIT_DEATHS);
    }

    private void addAchievement(Achievement achievement, IntStat.Key statType) {
        system.addAchievement(achievement);
        stats.addListener(statType, achievement::onUpdate);
    }
}
