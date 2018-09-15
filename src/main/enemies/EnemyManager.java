package main.enemies;

import main.Game;
import main.avatar.Avatar;
import main.entities.Entity;
import main.math.Vec2i;
import main.GameWorld;

import java.util.ArrayList;
public class EnemyManager {
    private ArrayList<Entity> entities;
    private ArrayList<Integer> pastMoves;
    GameWorld world;

    public EnemyManager(ArrayList<Entity> entities, GameWorld world) {
        this.entities = entities;
        for (Entity x : this.entities) {
            ((Enemy) x).setManager(this);
        }
        this.world = world;
        this.pastMoves = ((Avatar) world.getAvatar()).getPastmoves();
    }

    /**
     * Updates the AI decision on where to move
     */
    public void Update() {
        for (Entity x : entities) {
            // Call move and extract first index
            Vec2i move = ((Enemy) x).getMove();
            world.moveEntity(x, move);
        }
    }

    /**
     * @return If a hunter exist in the map
     */
    public boolean hunterExist() {
        for (Entity x : entities) {
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
    public ArrayList<Entity> getEntities() { return entities; }

    /**
     * @return The world
     */
    public GameWorld getWorld() { return world; }
}
