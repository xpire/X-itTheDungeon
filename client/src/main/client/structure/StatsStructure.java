package  main.client.structure;

import java.util.List;

public class StatsStructure {
    private String username;
    private int enemyKilled;
    private int treasureCollected;
    private int maxLevel;
    private int bombsKilled;
    private List<Integer> levelStar;

    public StatsStructure(
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
}
