package  main.client.structure;

import java.util.List;

/**
 * Class representing the statistics
 */
public class StatsStructure {
    private String username;
    private int enemyKilled;
    private int treasureCollected;
    private int maxLevel;
    private int bombsKilled;
    private List<Integer> levelStar;

    /**
     * Generic constructor
     * @param username : username of Gamer
     * @param enemyKilled : enemies killed stat
     * @param treasureCollected : treaure collected stat
     * @param maxLevel : progression of the user
     * @param bombsKilled : killed by bomb stat
     * @param levelStar : number of stars given for the level
     */
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
