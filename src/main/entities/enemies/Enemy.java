package main.entities.enemies;

import main.algorithms.AStarSearch;
import main.behaviour.AIBehaviour;
import main.entities.Avatar;
import main.entities.Entity;
import main.events.EnemyEvent;
import main.Level;
import main.math.Vec2i;

import java.util.ArrayList;

/**
 * Class which abstracts Enemy entities on the Level.
 * Determines their next move and also the enemies current behaviour
 */
public abstract class Enemy extends Entity implements StateDecision {

    protected boolean isHunter = false;

    protected EnemyManager manager = null;
    private AIBehaviour currBehaviour = null;


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


    /**
     * Finding the shortest path to a specific square using A*, the heuristic chosen
     * to be the Manhattan distance and the path length of the current node to the
     * length node.
     * @param targets target square that the AI wants to go to
     * @return an ArrayList of the path to the square
     */
    protected ArrayList<Vec2i> shortestPath(ArrayList<Vec2i> targets) {
        AStarSearch search = new AStarSearch(level, targets, pos);
        return search.search();
    }

    /**
     * Determines the move of an Enemy
     * @return Move of an AI
     */
    public Vec2i getMove() {
        ArrayList<Vec2i> wantedTiles = currBehaviour.decideMove(
                level,
                pos,
                level.getAvatar().getGridPos(),
                manager.getPastMoves(),
                level.getEnemies()
        );

        ArrayList<Vec2i> targets = shortestPath(wantedTiles);

        Vec2i wanted;
        if (targets.size() > 1)
            wanted = targets.get(1);
        else
            wanted = targets.get(0);

        return wanted;
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
     * Getter for current behaviour
     * @return the Enemies current behaviour
     */
    public AIBehaviour getCurrBehaviour() { return currBehaviour; }
    public void setCurrBehaviour(AIBehaviour behaviour) { currBehaviour = behaviour;}


    /**
     * Getter for the enemy manager
     * @return The current manager of the Enemies
     */
    public EnemyManager getManager() { return manager; }

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
