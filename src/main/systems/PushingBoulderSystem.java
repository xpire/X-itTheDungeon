package main.systems;

import main.GameWorld;
import main.maploading.Tile;
import main.maploading.TileMap;
import main.math.Vec2i;

public class PushingBoulderSystem {
    private GameWorld world;

    public PushingBoulderSystem(GameWorld world) {
        this.world = world;
    }

    /**
     *
     * @param from push from position
     * @param target push to position
     * @pre one tile difference between from and to
     * @return
     */
    public boolean canPush(Vec2i from, Vec2i target) {

        TileMap map = world.getMap();
        Vec2i dir = target.sub(from);

        if (!dir.isDirection()) return false;

        Vec2i to = target.add(dir);

        if (!map.isValidGridPos(to)) return false;

        Tile tile = map.getTile(to);

        return tile != null && tile.isPassableForPushable();
    }

    public Vec2i push(Vec2i from, Vec2i target) {

        Vec2i dir = target.sub(from);

        if(!canPush(from, target)) return new Vec2i(0, 0);

        return dir;
    }
}
