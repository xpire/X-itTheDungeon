package main.systems;

import main.GameWorld;
import main.entities.Entity;
import main.maploading.Level;
import main.math.Vec2i;

import java.util.Iterator;

public class GridMovementSystem {

    private GameWorld world;
    private Level map;


    public GridMovementSystem(GameWorld world, Level map) {
        this.world = world;
        this.map = map;
    }

    public boolean onTileMovement(Entity e, Vec2i target) {

        Vec2i curr = e.getGridPos();

        if (!target.sub(curr).isDirection()) {
            return false;
        }


        if (!map.isPassableFor(e, target)) {

            Iterator<Entity> it = map.getEntities(target);

            while (it.hasNext()) {
                if (!onTileInvaded(e, it.next())) {
                    return false;
                }
            }
        }

        e.moveBy(target.sub(curr));
        return true;
    }


    public boolean onTileInvaded(Entity invader, Entity target) {
        return target.onEntityPush(invader);
    }
}
