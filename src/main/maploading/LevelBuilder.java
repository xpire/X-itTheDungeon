package main.maploading;

import main.Level;
import main.entities.prop.IceBlock;
import main.trigger.objective.archive.AllSwitchesOnTrigger;
import main.trigger.objective.archive.CollectAllTreasuresTrigger;
import main.trigger.objective.archive.ExitDungeonTrigger;
import main.trigger.objective.archive.KillAllEnemiesTrigger;
import main.entities.Avatar;
import main.entities.enemies.*;
import main.entities.pickup.*;
import main.entities.prop.Boulder;
import main.entities.prop.Prop;
import main.entities.terrain.*;
import main.math.Vec2i;

import java.util.ArrayList;

/**
 * A Class using Factory Pattern to add entities to a Level
 *
 * Converts entityCode characters into Entity objects
 */
public class LevelBuilder {

    private Level level;

    private LayerBuilder<Terrain>   terrainLayerBuilder;
    private LayerBuilder<Prop>      propLayerBuilder;
    private LayerBuilder<Pickup>    pickupLayerBuilder;
    private LayerBuilder<Enemy>     enemyLayerBuilder;
    private LayerBuilder<Avatar>    avatarLayerBuilder;

    private ArrayList<LayerBuilder> builders;


    public LevelBuilder(int nRows, int nCols, double size, String name, boolean isCreateMode) {
        this(new Level(nRows, nCols, size, name, isCreateMode));
    }

    public LevelBuilder(Level level) {
        this.level = level;
        initLayerBuilders();
    }


    /**
     * Separately adds Keys and Doors to Levels. This is so
     * keys can have their matching door set, and it enforces key-doors to be
     * placed in pairs
     * @param keyPos : position of the key
     * @param doorPos : position of the door
     */
    public void addKeyDoor(Vec2i keyPos, Vec2i doorPos) throws InvalidMapException {
        if (!level.isValidGridPos(keyPos))
            throw new InvalidMapException("Invalid Key position: " + keyPos);

        if (!level.isValidGridPos(doorPos))
            throw new InvalidMapException("Invalid Door position: " + doorPos);

        Key key   = new Key(level, keyPos);
        Door door = new Door(level, doorPos);

        key.setMatchingDoor(door);

        pickupLayerBuilder.attachForcefully(keyPos, key, true);
        terrainLayerBuilder.attachForcefully(doorPos, door, true);
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
        if (!level.isValidGridPos(pos))
            throw new InvalidMapException("Invalid position: " + pos);

        if (entityCode == 'K' || entityCode == '|') return;

        LayerBuilder builder = builders.stream()
                .filter(b -> b.canMakeEntity(entityCode))
                .findFirst()
                .orElseThrow(() -> new InvalidMapException("Unrecognised entity code: " + entityCode));

        builder.makeAndAttachForcefully(pos, entityCode, true);
    }



    public void setObjectives(ArrayList<String> objectives) throws InvalidMapException {

        for (String objective : objectives) {
            switch (objective) {
                case "A":
                    level.addObjectives(new ExitDungeonTrigger());
                    break;
                case "B":
                    level.addObjectives(new AllSwitchesOnTrigger());
                    break;
                case "C":
                    level.addObjectives(new CollectAllTreasuresTrigger());
                    break;
                case "D":
                    level.addObjectives(new KillAllEnemiesTrigger());
                    break;
                default:
                    throw new InvalidMapException("Invalid Objective Code: " + objective);
            }
        }
    }


    public int getNRows() {
        return level.getNRows();
    }


    public int getNCols() {
        return level.getNCols();
    }


    public Level getLevel() {
        return level;
    }


    public void initLayerBuilders() {
        initTerrainLayerBuilder();
        initPropLayerBuilder();
        initPickupLayerBuilder();
        initEnemyLayerBuilder();
        initAvatarLayerBuilder();

        builders = new ArrayList<>();
        builders.add(terrainLayerBuilder);
        builders.add(propLayerBuilder);
        builders.add(pickupLayerBuilder);
        builders.add(enemyLayerBuilder);
        builders.add(avatarLayerBuilder);
    }


    private void initTerrainLayerBuilder() {
        EntityFactory<Terrain> terrainFactory = new EntityFactory<>();
        terrainFactory.addSupplier('|', () -> new Door(level));
        terrainFactory.addSupplier('X', () -> new Exit(level));
        terrainFactory.addSupplier('.', () -> new Ground(level));
        terrainFactory.addSupplier('#', () -> new Pit(level));
        terrainFactory.addSupplier('/', () -> new Switch(level));
        terrainFactory.addSupplier('*', () -> new Wall(level));
        terrainFactory.addSupplier('H', () -> new HeatPlate(level));

        terrainLayerBuilder = new LayerBuilder<Terrain>(level, terrainFactory) {
            @Override
            protected boolean canPlaceEntity(Vec2i pos, Terrain entity) {
                return level.canPlaceTerrain(pos, entity);
            }

            @Override
            protected boolean canReplaceEntity(Vec2i pos, Terrain entity) {
                return level.canReplaceTerrain(pos, entity);
            }

            @Override
            protected void addEntity(Vec2i pos, Terrain entity) {
                level.addTerrain(pos, entity);
            }
        };
    }



    private void initPropLayerBuilder() {
        EntityFactory<Prop> propFactory = new EntityFactory<>();
        propFactory.addSupplier('O', () -> new Boulder(level));
        propFactory.addSupplier('I', () -> new IceBlock(level));

        propLayerBuilder = new LayerBuilder<Prop>(level, propFactory) {
            @Override
            protected boolean canPlaceEntity(Vec2i pos, Prop entity) {
                return level.canPlaceProp(pos, entity);
            }

            @Override
            protected boolean canReplaceEntity(Vec2i pos, Prop entity) {
                return level.canReplaceProp(pos, entity);
            }

            @Override
            protected void addEntity(Vec2i pos, Prop entity) {
                level.addProp(pos, entity);
            }
        };
    }

    private void initPickupLayerBuilder() {
        EntityFactory<Pickup> pickupFactory = new EntityFactory<>();
        pickupFactory.addSupplier('-', () -> new Arrow(level));
        pickupFactory.addSupplier('!', () -> new Bomb(level));
        pickupFactory.addSupplier('^', () -> new HoverPotion(level));
        pickupFactory.addSupplier('>', () -> new InvincibilityPotion(level));
        pickupFactory.addSupplier('+', () -> new Sword(level));
        pickupFactory.addSupplier('$', () -> new Treasure(level));
        pickupFactory.addSupplier('@', () -> new BombPotion(level));

        pickupLayerBuilder = new LayerBuilder<Pickup>(level, pickupFactory) {
            @Override
            protected boolean canPlaceEntity(Vec2i pos, Pickup entity) {
                return level.canPlacePickup(pos, entity);
            }

            @Override
            protected boolean canReplaceEntity(Vec2i pos, Pickup entity) {
                return level.canReplacePickup(pos, entity);
            }

            @Override
            protected void addEntity(Vec2i pos, Pickup entity) {
                level.addPickup(pos, entity);
            }
        };
    }

    private void initEnemyLayerBuilder() {
        EntityFactory<Enemy> enemyFactory = new EntityFactory<>();
        enemyFactory.addSupplier('1', () -> new Hunter(level));
        enemyFactory.addSupplier('2', () -> new Strategist(level));
        enemyFactory.addSupplier('3', () -> new Hound(level));
        enemyFactory.addSupplier('4', () -> new Coward(level));

        enemyLayerBuilder = new LayerBuilder<Enemy>(level, enemyFactory) {
            @Override
            protected boolean canPlaceEntity(Vec2i pos, Enemy entity) {
                return level.canPlaceEnemy(pos, entity);
            }

            @Override
            protected boolean canReplaceEntity(Vec2i pos, Enemy entity) {
                return level.canReplaceEnemy(pos, entity);
            }

            @Override
            protected void addEntity(Vec2i pos, Enemy entity) {
                level.addEnemy(pos, entity);
            }
        };
    }

    private void initAvatarLayerBuilder() {
        EntityFactory<Avatar> avatarFactory = new EntityFactory<>();
        avatarFactory.addSupplier('P', () -> new Avatar(level));

        avatarLayerBuilder = new LayerBuilder<Avatar>(level, avatarFactory) {
            @Override
            protected boolean canPlaceEntity(Vec2i pos, Avatar entity) {
                return level.canPlaceAvatar(pos, entity);
            }

            @Override
            protected boolean canReplaceEntity(Vec2i pos, Avatar entity) {
                return level.canReplaceAvatar(pos, entity);
            }

            @Override
            protected void addEntity(Vec2i pos, Avatar entity) {
                level.addAvatar(pos, entity);
            }
        };
    }
}