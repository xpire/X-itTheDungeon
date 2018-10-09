package main.content;


import java.util.ArrayList;

// Initialiser Class for the Whole Game
public class GameConfig {

    private ArrayList<LevelConfig> levelConfigs;
    private int maxLevelConquered = 0;

    public GameConfig() {
        levelConfigs = new ArrayList<>();
        levelConfigs.add(new LevelConfig("Level 1", "level01"));
        levelConfigs.add(new LevelConfig("Level 2", "level02"));
        levelConfigs.add(new LevelConfig("Level 3", "level03"));

        levelConfigs.get(0).unlock();
    }

    public boolean isCompleted(int level) {
        return levelConfigs.get(level).isCompleted();
    }

    public boolean isLocked(int level) {
        return levelConfigs.get(level).isLocked();
    }
}
