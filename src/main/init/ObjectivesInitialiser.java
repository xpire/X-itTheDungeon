package main.init;

import main.content.IntStat;
import main.events.ExitEvent;
import main.trigger.achievement.Achievement;
import main.trigger.achievement.AchievementSystem;
import main.trigger.objective.FixedCountTrigger;

public class ObjectivesInitialiser {

    private AchievementSystem system;
    private IntStat stats;

    public ObjectivesInitialiser(AchievementSystem system, IntStat stats) {
        this.system = system;
        this.stats  = stats;
    }

    public void init() {
        FixedCountTrigger exitTrigger = new FixedCountTrigger(ExitEvent.EXIT_SUCCESS, null, 1);

                /*

                bus.addEventHandler(SwitchEvent.SWITCH_CREATED,     SWITCH_CREATED);
        bus.addEventHandler(SwitchEvent.SWITCH_DESTROYED,   SWITCH_DESTROYED);
        bus.addEventHandler(SwitchEvent.SWITCH_ACTIVATED,   SWITCH_ACTIVATED);
        bus.addEventHandler(SwitchEvent.SWITCH_DEACTIVATED, SWITCH_DEACTIVATED);
                 */
    }

    private void addAchievement(Achievement achievement, IntStat.Key statType) {
        system.addAchievement(achievement);
        stats.addListener(statType, achievement::onUpdate);
    }
}
