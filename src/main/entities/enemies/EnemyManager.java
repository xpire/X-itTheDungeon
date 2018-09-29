package main.entities.enemies;

import main.Level;
import main.entities.Avatar;
import main.math.Vec2i;

import java.util.ArrayList;

/**
 * Provides info that the enemies require to make their moves
 */
public class EnemyManager {

    private Level level;
    private Avatar avatar;

    /**
     * Constructor for enemy manager
     * @param level Level it will exist in
     */
    public EnemyManager(Level level) {
        this.level = level;
        this.avatar = level.getAvatar();
        getEnemies().forEach(e -> e.setManager(this));
    }


    /**
     * Updates the AI decision on where to move
     */
    public void update() {

        getEnemies().forEach(e -> {
//            System.out.println("Enemy: " + e.toString());
            Vec2i target = e.decideMove();
            if (!e.getGridPos().equals(target))
                level.moveEnemy(target, e);

        });
    }

    /**
     * Checks if there is a Hunter on the map
     * @return True if Hunter exists, false otherwise
     */
    public boolean checkHunterExists() {
        return getEnemies().stream().anyMatch(Enemy::isHunter);
    }


    public Vec2i getAvatarPos() {
        return avatar.getGridPos();
    }

    public boolean isAvatarRaged() {
        return avatar.isRaged();
    }

    /**
     * Gets the past moves of the avatar
     * @return ArrayList containing the Avatar's past moves
     */
    public ArrayList<Integer> getPastMoves() { return avatar.getPastMoves(); }

    /**
     * Getter for enemies
     * @return ArrayList of enemies
     */
    private ArrayList<Enemy> getEnemies() { return level.getEnemies(); }
}
