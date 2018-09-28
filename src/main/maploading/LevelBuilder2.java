package main.maploading;

import main.Level;
import main.entities.Avatar;
import main.entities.Entity;
import main.entities.enemies.*;
import main.entities.pickup.*;
import main.entities.prop.Boulder;
import main.entities.prop.Prop;
import main.entities.terrain.*;
import main.math.Vec2i;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A Class using Factory Pattern to add entities to a Level
 *
 * Converts entityCode characters into Entity objects
 */
public class LevelBuilder2 {

    private Level level;
    private ArrayList<LayerBuilder> builders = new ArrayList<>();

    // TODO: should read-in the meta data first and then apply the bindings
    // but the layer builders need access to the meta data
    public HashMap<Key, Door> keyToDoor = new HashMap<>();



    public LevelBuilder2(int nRows, int nCols, double size) {
        level = new Level(nRows, nCols, size, "Level");
    }


    public void addKeyDoorMapping(Vec2i keyPos, Vec2i doorPos) {
        Key key   = new Key(level, keyPos);
        Door door = new Door(level, doorPos);

        key.setMatchingDoor(door);

        if (!level.canReplacePickup(keyPos, key))
            level.removeAllAt(keyPos, true);
        level.addPickup(keyPos, key);


        if (!level.canReplaceTerrain(doorPos, door))
            level.removeAllAt(doorPos, true);
        level.addTerrain(doorPos, door);


    }


    /**
     * Adds individual entities to the a level
     *
     * @param entityCode Symbol of the entity to be added
     * @param pos position to add that entity
     * @throws InvalidMapException If symbol is unrecognised
     */
    //TODO: may be ask deep whether parameterising type is actually a better solution, because it's more flexible
    public void makeAndAttach(Vec2i pos, char entityCode) throws InvalidMapException {

        if (entityCode == 'K' || entityCode == '|') return;

        LayerBuilder builder = builders.stream()
                .filter(b -> b.canMakeEntity(entityCode))
                .findFirst()
                .orElseThrow(() -> new InvalidMapException("Unrecognised entity code: " + entityCode));

        builder.makeAndAttach(pos, entityCode);
    }


    /**
     * Separately adds Keys and Doors to Levels. This is so
     * keys can have their matching door set, and it enforces key-doors to be
     * placed in pairs
     * @param level : reference to the level where entities will be added
     * @param keyPos : position of the key
     * @param doorPos : position of the door
     */
    // TODO the replacement logic shouldn't be handled here
    public void addKeyDoor(Level level, Vec2i keyPos, Vec2i doorPos) {
        Key key   = new Key(level, keyPos);
        Door door = new Door(level, doorPos);

        key.setMatchingDoor(door);

        if (!level.canReplacePickup(keyPos, key))
            level.removeAllAt(keyPos, true);
        level.addPickup(keyPos, key);


        if (!level.canReplaceTerrain(doorPos, door))
            level.removeAllAt(doorPos, true);
        level.addTerrain(doorPos, door);
    }


    public void init() {
        EntityFactory<Terrain> terrainFactory = new EntityFactory<>();
        terrainFactory.addSupplier('|', () -> new Door(level));
        terrainFactory.addSupplier('X', () -> new Exit(level));
        terrainFactory.addSupplier('.', () -> new Ground(level));
        terrainFactory.addSupplier('#', () -> new Pit(level));
        terrainFactory.addSupplier('/', () -> new Switch(level));
        terrainFactory.addSupplier('*', () -> new Wall(level));

        EntityFactory<Prop> propFactory = new EntityFactory<>();
        propFactory.addSupplier('O', () -> new Boulder(level));

        EntityFactory<Pickup> pickupFactory = new EntityFactory<>();
        pickupFactory.addSupplier('-', () -> new Arrow(level));
        pickupFactory.addSupplier('!', () -> new Bomb(level));
        pickupFactory.addSupplier('^', () -> new HoverPotion(level));
        pickupFactory.addSupplier('>', () -> new InvincibilityPotion(level));
        pickupFactory.addSupplier('+', () -> new Sword(level));
        pickupFactory.addSupplier('$', () -> new Treasure(level));

        EntityFactory<Enemy> enemyFactory = new EntityFactory<>();
        enemyFactory.addSupplier('1', () -> new Hunter(level));
        enemyFactory.addSupplier('2', () -> new Strategist(level));
        enemyFactory.addSupplier('3', () -> new Hound(level));
        enemyFactory.addSupplier('4', () -> new Coward(level));

        EntityFactory<Avatar> avatarFactory = new EntityFactory<>();
        avatarFactory.addSupplier('P', () -> new Avatar(level));
    }

    public <T extends Entity> void addLayerBuilder(EntityFactory<T> factory, LayerBuilder<T> builder) {
        builders.put(factory, builder);
    }

//    private boolean attachTerrain(Vec2i pos, Terrain terrain) {
//        if (level.canPlaceTerrain(pos, terrain)) {
//            level.addTerrain(pos, terrain);
//            return true;
//        }
//        return false;
//    }
//
//    private boolean attachProp(Vec2i pos, Prop prop) {
//        if (level.canPlaceProp(pos, prop)) {
//            level.addProp(pos, prop);
//            return true;
//        }
//        return false;
//    }
//
//    private boolean attachTerrain(Vec2i pos, Terrain terrain) {
//        if (level.canPlaceTerrain(pos, terrain)) {
//            level.addTerrain(pos, terrain);
//            return true;
//        }
//        return false;
//    }
}


















        /*
        EntityFactory<Terrain> terrainFactory = new EntityFactory<>();

        if (terrainFactory.canMakeEntity(entityCode)) {
            Terrain terrain = terrainFactory.getEntity(entityCode, level, pos);

            if (!level.canReplaceTerrain(pos, terrain))
                level.removeAllAt(pos, true);

            level.addTerrain(pos, terrain);
        }

        EntityFactory<Prop> propFactory = new EntityFactory<>();

        if (propFactory.canMakeEntity(entityCode)) {
            Prop prop = propFactory.getEntity(entityCode, level, pos);

            if (!level.canReplaceProp(pos, prop))
                level.removeAllAt(pos, true);

            level.addProp(pos, prop);
        }

        EntityFactory<Pickup> pickupFactory = new EntityFactory<>();

        if (pickupFactory.canMakeEntity(entityCode)) {
            Pickup pickup = pickupFactory.getEntity(entityCode, level, pos);

            if (!level.canReplacePickup(pos, pickup))
                level.removeAllAt(pos, true);

            level.addPickup(pos, pickup);
        }

        EntityFactory<Enemy> enemyFactory = new EntityFactory<>();

        if (enemyFactory.canMakeEntity(entityCode)) {
            Enemy enemy = enemyFactory.getEntity(entityCode, level, pos);

            if (!level.canReplaceEnemy(pos, enemy))
                level.removeAllAt(pos, true);

            level.addEnemy(pos, enemy);
        }

        EntityFactory<Avatar> avatarFactory = new EntityFactory<>();

        if (avatarFactory.canMakeEntity(entityCode)) {
            Avatar avatar = avatarFactory.getEntity(entityCode, level, pos);

            if (!level.canReplaceAvatar(pos, avatar))
                level.removeAllAt(pos, true);

            level.addAvatar(pos, avatar);
        }
        //    EnemyFactory enemyFactory       = new EnemyFactory();
//    PickupFactory pickupFactory     = new PickupFactory();
//    PropFactory propFactory         = new PropFactory();
//    TerrainFactory terrainFactory   = new TerrainFactory();
//    AvatarFactory avatarFactory     = new AvatarFactory();
        */