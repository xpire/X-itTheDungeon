package main.entities.enemies;

import main.Algorithms.AStarSearch;
import main.behaviour.AIBehaviour;
import main.entities.Avatar;
import main.entities.Entity;
import main.entities.pickup.Pickup;
import main.entities.prop.Prop;
import main.entities.terrain.Terrain;
import main.maploading.Level;
import main.math.Vec2i;
import java.util.ArrayList;

public abstract class Enemy extends Entity implements StateDecision {

    protected boolean isHunter = false;

    EnemyManager manager;
    private AIBehaviour currBehavior;

    public void setCurrBehavior(AIBehaviour b) { this.currBehavior = b;}
    public AIBehaviour getCurrBehavior() { return this.currBehavior; }

    public Enemy(Level level) {
        super(level);
    }

    public Enemy(Level level, Vec2i pos) {
        super(level, pos);
    }


    @Override
    public boolean isPassableFor(Entity entity) { return false; }

    @Override
    public void onDestroyed() {
        level.removeEnemy(getGridPos());
    }

    /**
     * @return If this is a hunter
     */
    public boolean IsHunter() { return this.isHunter; }

    /**
     * Set the manager of this entity
     * @param manager Manager of all AIs
     */
    public void setManager(EnemyManager manager) { this.manager = manager; }

    /**
     * @return The current manager of the Enemies
     */
    public EnemyManager getManager() { return manager; }

    /**
     * Finding the shortest path to a specific square using A*, the heuristic chosen
     * to be the Mahanttan distance and the path length of the current node to the
     * length node.
     * @param targets target square that the AI wants to go to
     * @return an ArrayList of the path to the square
     */
    protected ArrayList<Vec2i> shortestPath(ArrayList<Vec2i> targets) {
        AStarSearch search = new AStarSearch(level, targets, pos);
        return search.Search();
    }

    /**
     * @return Move of an AI
     */
    public Vec2i getMove() {
        ArrayList<Vec2i> wantedTiles = currBehavior.decideMove(
                level,
                pos,
                level.getAvatar().getGridPos(),
                manager.getPastMoves(),
                level.getEnemies()
        );

        //System.out.println(wantedTiles);

        ArrayList<Vec2i> targets = shortestPath(wantedTiles);
        Vec2i wanted;
        if (targets.size() > 1) {
            wanted = targets.get(1);
        } else {
            wanted = targets.get(0);
        }
        //System.out.println(wanted);
//      System.out.println(targets);
        return wanted;
    }

    @Override
    public boolean isPassableForEnemy(Enemy enemy) {
        return false;
    }

    @Override
    public boolean canStackFor(Entity entity) {
        return false;
    }

    @Override
    public boolean canStackForTerrain(Terrain terrain) {
        return canStackFor(terrain);
    }

    @Override
    public boolean canStackForProp(Prop prop) {
        return canStackFor(prop);
    }

    @Override
    public boolean canStackForPickup(Pickup pickup) {
        return canStackFor(pickup);
    }

    @Override
    public boolean canStackForEnemy(Enemy enemy) {
        return canStackFor(enemy);
    }

    @Override
    public boolean canStackForAvatar(Avatar avatar) {
        return canStackFor(avatar);
    }
}
