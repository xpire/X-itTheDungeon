package main.trigger.achievement;

import java.util.ArrayList;

public class AchievementSystem {

    private ArrayList<Achievement> achievements = new ArrayList<>();

    public void addAchievement(Achievement achievement) {
        this.achievements.add(achievement);
    }
}
