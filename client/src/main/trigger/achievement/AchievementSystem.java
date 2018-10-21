package main.trigger.achievement;

import main.events.EventBus;

import java.util.ArrayList;

/**
 * Class which manages the achievements
 */
public class AchievementSystem {

    private ArrayList<Achievement> achievements = new ArrayList<>();
    private EventBus bus = new EventBus();

    /**
     * adds an achievement
     * @param achievement achievement to be added
     */
    public void addAchievement(Achievement achievement) {
        this.achievements.add(achievement);
    }

    /**
     * Gets the list of achievements
     * @return the list of achievements
     */
    public ArrayList<Achievement> getAchievements() {
        return new ArrayList<>(this.achievements);
    }

    public EventBus getBus() {
        return bus;
    }
}
