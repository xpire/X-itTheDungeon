package main.entities.enemies;

import main.maploading.Level;
import main.math.Vec2i;

import java.util.ArrayList;

/**
 * Provides info that the enemies require to make their moves
 */
public class EnemyManager {

    private Level level;
    private ArrayList<Integer> pastMoves;

    /**
     * Constructor for enemy manager
     * @param level Level it will exist in
     */
    public EnemyManager(Level level) {

        this.level      = level;
        this.pastMoves  = level.getAvatar().getPastMoves();

        getEnemies().forEach(e -> e.setManager(this));
    }


    /**
     * Updates the AI decision on where to move
     */
    public void Update() {

        getEnemies().forEach(e -> {

            // call move and extract first index
            e.decideBehaviour();

            Vec2i target = e.getMove();
            if (!e.getGridPos().equals(target))
                level.moveEnemy(target, e);

        });
    }


    /**
     * Checks if there is a Hunter on the map
     * @return True if Hunter exists, false otherwise
     */
    public boolean hunterExist() {
        for (Enemy e : getEnemies()) {
            if (e.isHunter())
                return true;
        }
        return false;
    }


    /**
     * Gets the past moves of the avatar
     * @return ArrayList containing the Avatar's past moves
     */
    public ArrayList<Integer> getPastMoves() { return pastMoves; }


    /**
     * Getter for enemies
     * @return ArrayList of enemies
     */
    private ArrayList<Enemy> getEnemies() { return level.getEnemies(); } //TODO check if used
}
