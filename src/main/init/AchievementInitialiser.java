package main.init;

import main.stat.StatisticType;
import main.stat.Statistics;
import main.trigger.achievement.Achievement;
import main.trigger.achievement.AchievementSystem;

public class AchievementInitialiser {

    private AchievementSystem system;
    private Statistics stats;

    public AchievementInitialiser(AchievementSystem system, Statistics stats) {
        this.system = system;
        this.stats  = stats;
    }

    public void init() {
        addAchievement(new Achievement("First Blood", "Kill 1 Enemy", 1), StatisticType.NUM_ENEMIES_KILLED);
        addAchievement(new Achievement("Penta Kill", "Kill 5 Enemies", 5), StatisticType.NUM_ENEMIES_KILLED);
        addAchievement(new Achievement("Yom Yom Yom", "Kill 10 Enemies", 10), StatisticType.NUM_ENEMIES_KILLED);

        addAchievement(new Achievement("Oopsie Daisie", "Fall into a Pit", 1), StatisticType.NUM_PIT_DEATHS);
    }

    private void addAchievement(Achievement achievement, StatisticType statType) {
        system.addAchievement(achievement);
        stats.addListener(statType, achievement::onUpdate);
    }
}
