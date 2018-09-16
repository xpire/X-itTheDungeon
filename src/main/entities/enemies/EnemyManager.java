package main.entities.enemies;

import main.maploading.Level;
import main.math.Vec2i;

import java.util.ArrayList;

public class EnemyManager {

    private Level level;
    private ArrayList<Integer> pastMoves;

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
     * @return If a hunter exist in the map
     */
    public boolean hunterExist() {
        for (Enemy e : getEnemies()) {
            if (e.isHunter())
                return true;
        }
        return false;
    }


    /**
     * @return The past moves of the avatar
     */
    public ArrayList<Integer> getPastMoves() { return pastMoves; }


    /**
     * @return a list of entity
     */
    private ArrayList<Enemy> getEnemies() { return level.getEnemies(); } //TODO check if used
}
