package main.maploading;

import main.Level;
import main.entities.Avatar;
import main.entities.enemies.Enemy;
import main.entities.pickup.Key;
import main.entities.pickup.Pickup;
import main.entities.prop.Prop;
import main.entities.terrain.Door;
import main.entities.terrain.Terrain;
import main.math.Vec2i;

/**
 * A Class using Factory Pattern to add entities to a Level
 */
public class LevelBuilder {

    /**
     * Adds individual entities to the a level
     *
     * @param entity Symbol of the entity to be added
     * @param pos position to add that entity
     * @param level reference to the Level the entity will be added to
     * @throws Exception If symbol is unrecognised
     */
    public void buildEntity(char entity, Vec2i pos, Level level) throws Exception {

        EnemyFactory enemyFactory = new EnemyFactory();
        PickupFactory pickupFactory = new PickupFactory();
        PropFactory propFactory = new PropFactory();
        TerrainFactory terrainFactory = new TerrainFactory();
        AvatarFactory avatarFactory = new AvatarFactory();

        switch (entity) {
            case 'X':
            case '.':
            case '#':
            case '/':
            case '*':
                Terrain terrain = terrainFactory.getTerrain(entity, level, pos);

                if (level.canReplaceTerrain(pos, terrain)) {
                    level.addTerrain(pos, terrain);
                } else {
                    level.removeAllAt(pos, true);
                    level.addTerrain(pos, terrain);
                }

                return;
            case 'O':
                Prop prop = propFactory.getProp(entity, level, pos);

                if (level.canReplaceProp(pos, prop)) {
                    level.addProp(pos, prop);
                } else {
                    level.removeAllAt(pos, true);
                    level.addProp(pos, prop);
                }

                return;
            case '-':
            case '!':
            case '^':
            case '>':
            case '+':
            case '$':
                Pickup pickup = pickupFactory.getPickup(entity, level, pos);

                if (level.canReplacePickup(pos, pickup)) {
                    level.addPickup(pos, pickup);
                } else {
                    level.removeAllAt(pos, true);
                    level.addPickup(pos, pickup);
                }

                return;
            case '1':
            case '2':
            case '3':
            case '4':
                Enemy enemy = enemyFactory.getEnemy(entity, level, pos);

                if (level.canReplaceEnemy(pos, enemy)) {
                    level.addEnemy(pos, enemy);
                } else {
                    level.removeAllAt(pos, true);
                    level.addEnemy(pos, enemy);
                }

                return;
            case 'P':
                Avatar avatar = avatarFactory.getAvatar(entity, level, pos);

                if (level.canReplaceAvatar(pos, avatar)) {
                    level.addAvatar(pos, avatar);
                } else {
                    level.removeAllAt(pos, true);
                    level.addAvatar(pos, avatar);
                }

                return;
            case 'K':
            case '|':

                return;
            default:
                System.out.println("Error: Unrecognised entity");
                throw new Exception();
        }
    }

    /**
     * Separately adds Keys and Doors to Levels. This is so
     * keys can have their matching door set, and it enforces key-doors to be
     * placed in pairs
     * @param level : reference to the level where entities will be added
     * @param keyPos : position of the key
     * @param doorPos : position of the door
     */
    public void buildKeyDoor(Level level, Vec2i keyPos, Vec2i doorPos) {

        Key key = new Key(level, keyPos);
        Door door = new Door(level, doorPos);

        key.setMatchingDoor(door);

        if (level.canReplacePickup(keyPos, key)) {
            level.addPickup(keyPos, key);
        } else {
            level.removeAllAt(keyPos, true);
            level.addPickup(keyPos, key);
        }

        if (level.canReplaceTerrain(doorPos, door)) {
            level.addTerrain(doorPos, door);
        } else {
            level.removeAllAt(doorPos, true);
            level.addTerrain(doorPos, door);
        }

    }
}
