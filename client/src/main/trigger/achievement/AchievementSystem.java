package main.trigger.achievement;

import java.util.ArrayList;
import java.util.Iterator;

public class AchievementSystem {

    private ArrayList<Achievement> achievements = new ArrayList<>();

    public void addAchievement(Achievement achievement) {
        this.achievements.add(achievement);
    }

    public ArrayList<Achievement> getAchievements() {
        return new ArrayList<>(this.achievements);
    }
}
