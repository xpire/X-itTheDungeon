package main.trigger.achievement;

import main.events.EventBus;

import java.util.ArrayList;
import java.util.Iterator;

public class AchievementSystem {

    private ArrayList<Achievement> achievements = new ArrayList<>();
    private EventBus bus = new EventBus();

    public void addAchievement(Achievement achievement) {
        this.achievements.add(achievement);
    }

    public ArrayList<Achievement> getAchievements() {
        return new ArrayList<>(this.achievements);
    }

    public EventBus getBus() {
        return bus;
    }
}
