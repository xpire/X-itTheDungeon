package server.model;

import java.util.List;

/**
 *  Stores the achievement information of each user
 */
public class UserStatus {
    private String username;
    private int enemyKilled;
    private int treasureCollected;
    private int maxLevel;
    private int bombsKilled;
    private List<Integer> levelStar;

    public UserStatus(
            String username,
            int enemyKilled,
            int treasureCollected,
            int maxLevel,
            int bombsKilled,
            List<Integer> levelStar
    ) {
        this.username = username;
        this.enemyKilled = enemyKilled;
        this.treasureCollected = treasureCollected;
        this.maxLevel = maxLevel;
        this.bombsKilled = bombsKilled;
        this.levelStar = levelStar;
    }

    public String getUsername() {
        return username;
    }

    public int getEnemyKilled() {
        return enemyKilled;
    }

    public int getTreasureCollected() {
        return treasureCollected;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public int getBombsKilled() {
        return bombsKilled;
    }

    public List<Integer> getLevelStar() {
        return levelStar;
    }
}

