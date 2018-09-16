package main.entities.enemies;

import main.entities.Entity;
import main.maploading.Level;
import main.math.Vec2i;
import java.util.ArrayList;

public class EnemyManager {
    private ArrayList<Integer> pastMoves;
    private Level level;

    public EnemyManager(Level level) {
        ArrayList<Enemy> entities = level.getEnemies();
        for (Entity x : entities) {
            ((Enemy) x).setManager(this);
        }
        this.level = level;
        this.pastMoves = level.getAvatar().getPastmoves();
    }

    /**
     * Updates the AI decision on where to move
     */
    public void Update() {
        for (Enemy x : level.getEnemies()) {
            // Call move and extract first index
            x.decideBehaviour(level);
            Vec2i move = x.getMove();
            if (!x.getGridPos().equals(move)) { level.moveEnemy(move, x); }
        }
    }

    /**
     * @return If a hunter exist in the map
     */
    public boolean hunterExist() {
        for (Entity x : level.getEnemies()) {
            if (((Enemy) x).IsHunter()) {
                return true;
            }
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
    public ArrayList<Enemy> getEntities() { return level.getEnemies(); }
}
