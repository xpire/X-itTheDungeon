package main.entities.enemies;

import main.Level;
import main.entities.Avatar;
import main.math.Vec2i;

import java.util.ArrayList;

/**
 * Provides info that the enemies require to make their moves
 */
public class EnemyCommander {

    private Level level;
    private Avatar avatar;

    /**
     * Constructor for enemy manager
     * @param level Level it will exist in
     */
    public EnemyCommander(Level level) {
        this.level = level;
        this.avatar = level.getAvatar();
        getEnemies().forEach(e -> e.setManager(this));
    }


    /**
     * Updates the AI decision on where to move
     */
    public void update() {

        System.out.println("START ENEMIES!");
        getEnemies().forEach(e -> {
            System.out.println("Enemy");
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

    /**
     * Getter for the Avatars position
     * @return the avatars position
     */
    public Vec2i getAvatarPos() {
        return avatar.getGridPos();
    }

    /**
     * Checks if the avatar is enraged by an invincibility potion
     * @return true if avatar is enraged
     */
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
