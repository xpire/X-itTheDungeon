package main.achivement;


import main.events.EventBus;

import java.util.ArrayList;

/**
 * Class which manages the set of achievements for a game
 */
public class AchievementSystem {

    private EventBus eventBus;
    private ArrayList<Achievement> achievements = new ArrayList<>();

    public AchievementSystem(EventBus bus) {
        this.eventBus = bus;
    }

    public void addAchievement(Achievement achievement) {
        achievements.add(achievement);
        achievement.activate(eventBus);
    }

    public boolean checkAchievedAll() {
        return achievements.stream().allMatch(Achievement::isCompleted);
    }
}