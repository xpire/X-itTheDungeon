package main.enemies;

import main.Game;
import main.entities.Entity;
import main.math.Vec2i;
import main.GameWorld;

import java.util.ArrayList;
public class EnemyManager {
    private ArrayList<Entity> entities;
    private int pastMoves;
    GameWorld world;

    public EnemyManager(ArrayList<Entity> entities, int pastMoves, GameWorld world) {
        this.entities = entities;
        this.pastMoves = pastMoves;
        for (Entity x : this.entities) {
            ((Enemy) x).setManager(this);
        }
        this.world = world;
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
    public int getPastMoves() { return pastMoves; }

    /**
     * @return a list of entity
     */
    public ArrayList<Entity> getEntities() { return entities; }
}
