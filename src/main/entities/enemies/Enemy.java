package main.entities.enemies;

import main.Level;
import main.algorithms.GridAStar;
import main.behaviour.AIBehaviour;
import main.entities.Avatar;
import main.entities.Entity;
import main.events.EnemyEvent;
import main.math.Vec2i;

import java.util.List;

/**
 * Class which abstracts Enemy entities on the Level.
 * Determines their next move and also the enemies current behaviour
 */
public abstract class Enemy extends Entity {

    protected boolean isHunter = false;

    protected EnemyManager manager = null;

    /**
     * Constructors for Enemies
     * @param level reference to the level the Enemy will exist in
     */
    public Enemy(Level level) {
        super(level);
    }
    public Enemy(Level level, Vec2i pos) {
        super(level, pos);
    }

    @Override
    public void onCreated() {
        level.postEvent(new EnemyEvent(EnemyEvent.ENEMY_CREATED));
    }

    @Override
    public void onDestroyed() {
        level.postEvent(new EnemyEvent(EnemyEvent.ENEMY_KILLED));
        level.removeEnemy(pos);
    }

    @Override
    public void onExploded() {
        onDestroyed();
    }

    /**
     * Determines the move of an Enemy
     * @return Move of an AI
     */
    public Vec2i decideMove() {
        AIBehaviour behaviour = decideBehaviour();

        List<Vec2i> targetTiles = behaviour.decideTargetTiles();
        List<Vec2i> path        = shortestPath(targetTiles);

        if (path.size() > 1)
            return path.get(1);
        else
            return path.get(0);
    }

    protected abstract AIBehaviour decideBehaviour();

    /**
     * Finding the shortest path to a specific square using A*, the heuristic chosen
     * to be the Manhattan distance and the path length of the current node to the
     * length node.
     * @param targets target square that the AI wants to go to
     * @return an ArrayList of the path to the square
     */
    private List<Vec2i> shortestPath(List<Vec2i> targets) {
        GridAStar star = new GridAStar(level, pos, targets);
        return star.search();
    }


    @Override
    public boolean isPassableFor(Entity entity) {
        return false;
    }

    @Override
    public boolean isPassableForAvatar(Avatar avatar) {
        return true;
    }

    @Override
    public boolean canStackFor(Entity entity) {
        return false;
    }

    @Override
    public void onEnterByAvatar(Avatar avatar) {
        if (avatar.isRaged())
            onDestroyed();
        else
            avatar.onDestroyed();
    }

    /**
     * Set the manager of this entity
     * @param manager Manager of all AIs
     */
    public void setManager(EnemyManager manager) { this.manager = manager; }


    /**
     * Checks if the current entity is a hunter
     * @return True if hunter, false otherwise
     */
    public boolean isHunter() { return isHunter; }

}
