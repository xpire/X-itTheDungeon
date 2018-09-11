package main.systems;

import main.GameWorld;
import main.entities.Entity;
import main.maploading.TileMap;
import main.math.Vec2i;

import java.util.Iterator;

public class GridMovementSystem {

    private GameWorld world;
    private TileMap map;


    public GridMovementSystem(GameWorld world, TileMap map) {
        this.world = world;
        this.map = map;
    }

    public boolean onTileMovement(Entity e, Vec2i target) {

        Vec2i curr = e.getGridPos();

        if (!target.sub(curr).isDirection()) {
            return false;
        }

        Iterator<Entity> entities = map.getEntities(target);
        while (entities.hasNext()) {
            if (!onTileInvaded(e, entities.next())) {
                return false;
            }
        }

        return true;
    }


    public boolean onTileInvaded(Entity invader, Entity target) {
        return target.onInvadeBy(invader);
    }
}
