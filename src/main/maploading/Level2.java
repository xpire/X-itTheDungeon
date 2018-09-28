package main;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Node;
import main.achivement.Achievement;
import main.achivement.AchievementSystem;
import main.component.ViewComponent;
import main.entities.Avatar;
import main.entities.Entity;
import main.entities.enemies.Enemy;
import main.entities.pickup.Pickup;
import main.entities.prop.Prop;
import main.entities.terrain.Ground;
import main.entities.terrain.Terrain;
import main.events.EventBus;
import main.maploading.*;
import main.math.Vec2d;
import main.math.Vec2i;
import main.util.Array2DIterator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Class which represents and handles the game map, both in its grid
 * based view and its game based view. This is used for play mode and create mode
 *
 * Entities within the Level are split into 5 layers:
 *  - Terrains
 *  - Props
 *  - Pickups
 *  - Enemies
 *  - Avatar
 *
 */

// TODO: decide whether to fully apply the visitor pattern or not, e.g. onEntityLeave...
    //
    // notifyOn... things break if we separate things into layers


public class Level2 {

    private final Supplier<Terrain> DEFAULT_TERRAIN = () -> new Ground(this);

    private ViewComponent view;
    private double size;
    private int nRows = 0;
    private int nCols = 0;

    private String name;
    private boolean isCreateMode = false;

    private ArrayList<String> objectives;

    private ArrayList<EntityLayer> layers;
    private TerrainLayer    terrainLayer;
    private PropLayer       propLayer;
    private PickupLayer     pickupLayer;
    private EnemyLayer      enemyLayer;
    private AvatarLayer     avatarLayer;

    private EventBus eventBus = new EventBus();
    private AchievementSystem achievementSystem = new AchievementSystem(eventBus);

    /**
     * Constructor for the Level class
     * @param nRows : number of rows
     * @param nCols : number of columns
     * @param size : size of each tile (in the view)
     * @param name : name of the Level
     * @param isCreateMode : flag to differentiate events that should be triggered
     */

    public Level2(int nRows, int nCols, double size, String name, boolean isCreateMode) {
        this.nRows = nRows;
        this.nCols = nCols;
        this.size  = size;
        this.name = name;
        this.isCreateMode = isCreateMode;

        this.objectives = new ArrayList<>();



        for (int y = 0; y < nRows; y++) {
            for (int x = 0; x < nCols; x++) {
                Ground ground = new Ground(null);
                addTerrain(new Vec2i(x, y), ground);
            }
        }

        view = new ViewComponent();
        layers.add(terrainLayer);
        layers.add(propLayer);
        layers.add(pickupLayer);
        layers.add(enemyLayer);
        layers.add(avatarLayer);

        layers.forEach(layer -> view.addNode(layer.getView()));
    }


    /**
     * Level constructor which sets PlayMode as the default flag
     * @param nRows : number of rows
     * @param nCols : number of cols
     * @param size : size of each tile (in the view)
     * @param name : name of the Level
     */
    public Level2(int nRows, int nCols, double size, String name) {
        this(nRows, nCols, size, name, false);
    }


    /*
        NAME TOOLS
     */

    /**
     * Getter for Level's name
     * @return the Level's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the Level name
     * @param name : name of the Level
     */
    public void setName(String name) {
        this.name = name;
    }





    /**
     * Removes all entities at a position
     * @param pos : position on the Level to remove everything
     * @param replaceWithDefault : flag to replace the cleared tile with a Ground tile
     */
    public void removeAllAt(Vec2i pos, boolean replaceWithDefault) {
        layers.forEach(layer -> layer.removeEntity(pos, replaceWithDefault));
    }

    /**
     * Notifies the Terrain entity when a Prop moves on top of it
     * @param pos : position of Terrain entity
     * @param prop : Prop entity which was moved
     */
    private void notifyOnEnterByProp(Vec2i pos, Prop prop) {
        forEachEntityAt(pos, e -> e.onEnterByProp(prop));
    }

    /**
     * Notifies the Terrain entity when an Enemy moves on top of it
     * @param pos : position of the Terrain entity
     * @param enemy : Enemy which was moved
     */
    private void notifyOnEnterByEnemy(Vec2i pos, Enemy enemy) {
        forEachEntityAt(pos, e -> e.onEnterByEnemy(enemy));
    }

    /**
     * Notifies the Terrain entity and Pickup entity when the Avatar moves on top of it
     * @param pos : position of the Terrain & Pickup entity
     * @param avatar : Avatar entity
     */
    private void notifyOnEnterByAvatar(Vec2i pos, Avatar avatar) {
        forEachEntityAt(pos, e -> e.onEnterByAvatar(avatar));
    }

    /**
     * Notifies the Terrain entity when a Prop is moved from on top of it
     * @param pos : position of the Terrain
     * @param prop : Prop which was moved
     */
    private void notifyOnLeaveByProp(Vec2i pos, Prop prop) {
        forEachEntityAt(pos, e -> e.onLeaveByProp(prop));
    }

    /**
     * Notifies the Terrain when an Enemy is moved from on top of it
     * @param pos : position of the Terrain
     * @param enemy : Enemy which moved
     */
    private void notifyOnLeaveByEnemy(Vec2i pos, Enemy enemy) {
        forEachEntityAt(pos, e -> e.onLeaveByEnemy(enemy));
    }

    /**
     * Notifies the Terrain when an Avatar is moved from on top of it
     * @param pos : position of the Terrain
     * @param avatar : Avatar entity
     */
    private void notifyOnLeaveByAvatar(Vec2i pos, Avatar avatar) {
        forEachEntityAt(pos, e -> e.onLeaveByAvatar(avatar));
    }

    // TODO: param skip-layer
    private void forEachEntityAt(Vec2i pos, Consumer<Entity> action) {
        layers.forEach(layer -> {
            if (layer.hasEntity(pos))
                action.accept(layer.getEntity(pos));
        });
    }


    /*
        CAN STACK FOR
     */

    /**
     * Checks if you can place a Terrain entity at a certain position, without
     * violating stacking rules
     * @param pos : position to check
     * @param terrain : Terrain entity to be placed
     * @return True if you can place the Terrain down, false otherwise
     */
    public boolean canPlaceTerrain(Vec2i pos, Terrain terrain) {
        if (terrainLayer.hasEntity(pos)) return false;
        return canReplaceTerrain(pos, terrain);
    }

    /**
     * Checks if you can place a Prop entity at a certain position, without
     * violating stacking rules
     * @param pos : position to check
     * @param prop : Prop entity to be placed
     * @return True if you can place the Prop down, false otherwise
     */
    public boolean canPlaceProp(Vec2i pos, Prop prop) {
        if (propLayer.hasEntity(pos)) return false;
        return canReplaceProp(pos, prop);
    }

    /**
     * Checks if you can place a Pickup entity at a certain position, without
     * violating stacking rules
     * @param pos : position to check
     * @param pickup : Pickup entity to be placed
     * @return True if you can place the Pickup down, false otherwise
     */
    public boolean canPlacePickup(Vec2i pos, Pickup pickup) {
        if (pickupLayer.hasEntity(pos)) return false;
        return canReplacePickup(pos, pickup);
    }

    /**
     * Checks if you can place an Enemy entity at a certain position, without
     * violating stacking rules
     * @param pos : position to check
     * @param enemy : Enemy entity to be placed
     * @return True if you can place the Enemy down, false otherwise
     */
    public boolean canPlaceEnemy(Vec2i pos, Enemy enemy) {
        if (enemyLayer.hasEntity(pos)) return false;
        return canReplaceEnemy(pos, enemy);
    }

    /**
     * Checks if you can place an Avatar entity at a certain position, without
     * violating stacking rules
     * @param pos : position to check
     * @param avatar : Avatar entity to be placed
     * @return True if you can place the Avatar down, false otherwise
     */
    public boolean canPlaceAvatar(Vec2i pos, Avatar avatar) {
        if (avatarLayer.hasEntity(pos)) return false;
        return canReplaceAvatar(pos, avatar);
    }

    /**
     * Checks if you can place a Terrain entity at a certain position, without
     * violating stacking rules if a current Terrain entity is removed
     * @param pos : position to check
     * @param terrain : Terrain to be placed
     * @return True if you can place it, false otherwise
     */
    public boolean canReplaceTerrain(Vec2i pos, Terrain terrain) {

        return
                ifPresent(propLayer, pos, terrain::isStackableForProp, true)
        ifPresent(pickupLayer, pos, terrain::isStackableForPickup, true)
        ifPresent(enemyLayer, pos, terrain::isStackableForEnemy, true)
        ifPresent(avatarLayer, pos, terrain::isStackableForAvatar, true)

        return true;
    }

//    private boolean canStackOn(Entity entity) {
//        entity.isStackableForTerrain(terrain);
//    }

    private <T extends Entity, R> R ifPresent(EntityLayer<T> layer, Vec2i pos, Function<T, R> func, R defaultValue) {
        if (layer.hasEntity(pos))
            return func.apply(layer.getEntity(pos));
        return defaultValue;
    }


    /**
     * Checks if you can place a Prop entity at a certain position, without
     * violating stacking rules if a current Prop entity is removed
     * @param pos : position to check
     * @param prop : Prop to be placed
     * @return True if you can place it, false otherwise
     */
    public boolean canReplaceProp(Vec2i pos, Prop prop) {

        // TODO try predicate chaining
        ifPresent(terrainLayer, pos, e -> e.isStackableForProp(prop), true);
        ifPresent(pickupLayer, pos, e -> e.canStackOn(prop), true);
        ifPresent(enemyLayer, pos, e -> e.canStackOn(prop), true);
        ifPresent(avatarLayer, pos, e -> e.canStackOn(prop), true);

        for (EntityLayer layer : layers) {
            if (layer.equals(propLayer)) break;

            if (layer.hasEntity(pos) && !layer.getEntity(pos).isStackableForProp(prop))
                return false;

        }
    }


//    public <R> void forEachEntityAtUnder(Vec2i pos, EntityLayer layer, Function<EntityLayer, R> action) {
//
//        int index = layers.indexOf(layer);
//
//        for (int i = 0; i < index; i++) {
//            EntityLayer l = layers.get(i);
//            action.apply(layer);
//        }
//    }

    /**
     * Checks if you can place a Pickup entity at a certain position, without
     * violating stacking rules if a current Pickup entity is removed
     * @param pos : position to check
     * @param pickup : Pickup to be placed
     * @return True if you can place it, false otherwise
     */
    public boolean canReplacePickup(Vec2i pos, Pickup pickup) {
        if (!terrainLayer.canStackFor(pos, pickup)) return false;
        if (!propLayer.canStackFor(pos, pickup)) return false;
        if (!enemyLayer.canStackOn(pos, pickup)) return false;
        if (!avatarLayer.canStackOn(pos, pickup)) return false;
        return true;
    }

    /**
     * Checks if you can place a Enemy entity at a certain position, without
     * violating stacking rules if a current Enemy entity is removed
     * @param pos : position to check
     * @param enemy : Enemy to be placed
     * @return True if you can place it, false otherwise
     */
    public boolean canReplaceEnemy(Vec2i pos, Enemy enemy) {
        if (hasTerrain(pos)) {
            if (!getTerrain(pos).canStackForEnemy(enemy)) return false;
        }

        if (hasProp(pos)) {
            if (!getProp(pos).canStackForEnemy(enemy)) return false;
        }

        if (hasPickup(pos)) {
            if (!getPickup(pos).canStackForEnemy(enemy)) return false;
        }

        if (hasAvatar(pos)) {
            if (!enemy.isStackableForAvatar(getAvatar())) return false;
        }

        return true;
    }

    /**
     * Checks if you can place a Avatar entity at a certain position, without
     * violating stacking rules if a current Avatar entity is removed
     * @param pos : position to check
     * @param avatar : Avatar to be placed
     * @return True if you can place it, false otherwise
     */
    public boolean canReplaceAvatar(Vec2i pos, Avatar avatar) {
        if (hasTerrain(pos) && !getTerrain(pos).canStackForAvatar(avatar)) return false;

        if (hasProp(pos)) {
            if (!getProp(pos).canStackForAvatar(avatar)) return false;
        }

        if (hasPickup(pos)) {
            if (!getPickup(pos).canStackForAvatar(avatar)) return false;
        }

        if (hasEnemy(pos)) {
            if (!getEnemy(pos).canStackForAvatar(avatar)) return false;
        }

        return true;
    }


    public boolean isPassableFor(Vec2i pos, Entity other) {
        if(!isValidGridPos(pos)) return false;
        return layers.stream().allMatch(layer -> layer.isPassableFor(pos, other));
    }

    public boolean onPushByAvatar(Vec2i pos, Avatar avatar) {
        if (!isValidGridPos(pos)) return false;

        return terrainLayer.applyIfPresent(pos, t -> t.onPush(avatar), true)
                && propLayer.applyIfPresent(pos, t -> t.onPush(avatar), true);
    }


    /*
        Objective tools
     */

    /**
     * Getter for the objectives of the Level
     * @return ArrayList of Level objectives
     */
    public ArrayList<String> getObjectives() {
        return objectives;
    }

    /**
     * Setter for the objectives of the Level
     * @param objectives ArrayList of objectives
     */
    public void setObjectives(ArrayList<String> objectives) {
        this.objectives = objectives;
    }

    /*
        Dimensions and View
     */

    /**
     * Getter for the # of rows in the Level
     * @return The # of rows in the Level
     */
    public int getNRows() {
        return nRows;
    }

    /**
     * Getter for the # of cols in the Level
     * @return The # of cols in the Level
     */
    public int getNCols() {
        return nCols;
    }

    /**
     * Getter for the Height of each tile
     * @return The height of each tile
     */
    public double getHeight() {
        return size * nRows;
    }

    /**
     * Getter for the width of each tile
     * @return the width of each tile
     */
    public double getWidth() {
        return size * nCols;
    }

    /**
     * Getter for the View of the Level
     * @return the View of the Level
     */
    public Node getView() {
        return view.getView();
    }


    /**
     * Checks if a certain coordinate is a valid point on the Level
     * @param pos : coordinate to check
     * @return True if valid, false otherwise
     */
    public boolean isValidGridPos(Vec2i pos) {
        return pos.withinX(0, getNCols() - 1) && pos.withinY(0, getNRows() - 1);
    }

    /**
     * Computes the position of a Tile on the screen
     * @param pos : position of the tile
     * @return The coordinate of the Tile on the screen
     */
    public Vec2d gridPosToWorldPosCentre(Vec2i pos) {
        return new Vec2d((pos.getX() + 0.5) * size, (pos.getY() + 0.5) * size);
    }

    /**
     * Resizes the # of rows and cols of the Level
     * @param newNRow : new # of rows for the Level
     * @param newNCol : new # of cols for the Level
     */
    public void resize(int newNRow, int newNCol) {
        Vec2i newDim = new Vec2i(newNRow, newNCol);
        if (!newDim.within(new Vec2i(4, 4), new Vec2i(64, 64))) {
            System.out.println("Error: map size must be between 4x4 and 64x64");
            return;
        }

        Terrain[][] resizedTerrain = new Terrain[newNRow][newNCol];
        HashMap<Vec2i, Prop> resizedProps = new HashMap<>();
        HashMap<Vec2i, Enemy> resizedEnemies = new HashMap<>();
        HashMap<Vec2i, Pickup> resizedPickups = new HashMap<>();

        int copyNRow = (nRows < newNRow) ? nRows : newNRow;
        int copyNCol = (nCols < newNCol) ? nCols : newNCol;

        Vec2i min = new Vec2i(0, 0);
        Vec2i max = new Vec2i(newNCol - 1, newNRow - 1);

        for (int i = 0; i < nRows; i++) {
            for (int j = 0; j < nCols; j++) {
                Vec2i pos = new Vec2i(j, i);

                if (!pos.within(min, max)) removeAllAt(pos,  true);
            }
        }

        for (int i = 0; i < copyNRow; i++) {
            for (int j = 0; j < copyNCol; j++) {
                Vec2i pos = new Vec2i(j, i);

                resizedTerrain[i][j] = terrains[i][j];
                if (enemies.containsKey(pos)) resizedEnemies.put(pos, enemies.get(pos));
                if (props.containsKey(pos)) resizedProps.put(pos, props.get(pos));
                if (pickups.containsKey(pos)) resizedPickups.put(pos, pickups.get(pos));
            }
        }

        this.nRows = newNRow;
        this.nCols = newNCol;

        this.terrains = resizedTerrain;
        this.props = resizedProps;
        this.enemies = resizedEnemies;
        this.pickups = resizedPickups;

        for (int i = 0; i < newNRow; i++) {
            for (int j = 0; j < newNCol; j++) {
                Vec2i pos = new Vec2i(j, i);
                Terrain ground = new Ground(this);
                if (!hasTerrain(pos)) addTerrain(pos, ground);
            }
        }
    }


    /**
     * Gets an iterator for the Terrain entities of the Level
     * @return an iterator of the Terrain entities
     */
    public Iterator<Terrain> getTerrainIterator() {
        return terrainLayer.getIterator();
    }

    /**
     * Gets an iterator for the Prop entities of the Level
     * @return an iterator of the Prop entities
     */
    public Iterator<Prop> getPropIterator() {
        return propLayer.getIterator();
    }

    /**
     * Gets an iterator for the Pickup entities of the Level
     * @return an iterator of the Pickup entities
     */
    public Iterator<Pickup> getPickupIterator() {
        return pickupLayer.getIterator();
    }

    /**
     * Gets an iterator for the Enemy entities of the Level
     * @return an iterator of the Pickup entities
     */
    public Iterator<Enemy> getEnemyIterator() {
        return enemyLayer.getIterator();
    }

    /**
     * Gets an iterator for the entities at a certain position
     * @param pos : the position to find entities
     * @return an iterator of the entities at the position
     */
    public Iterator<Entity> getEntitiesAt(Vec2i pos) {
        ArrayList<Entity> entities = new ArrayList<>();
        if (hasTerrain(pos)) entities.add(getTerrain(pos));
        if (hasProp(pos))    entities.add(getProp(pos));
        if (hasPickup(pos))  entities.add(getPickup(pos));
        if (hasEnemy(pos))   entities.add(getEnemy(pos));
        if (hasAvatar(pos))  entities.add(avatar);

        return entities.iterator();
    }

    /**
     * Displays the Level to the terminal in symbol format
     */
    public void displayLevel() {

        System.out.println(nRows + "\t" + nCols);

        for (int i = 0; i < nRows; i++) {
            for (int j = 0; j < nCols; j++) {
                Iterator<Entity> it = getEntitiesAt(new Vec2i(j, i));
                while (it.hasNext()) {
                    Entity e = it.next();

                    System.out.print(e.getSymbol());
                }
                System.out.print("\t");
            }
            System.out.println();
        }

        ArrayList<String> objectives = getObjectives();
        System.out.print("Objectives are: ");
        for (String s : objectives) {
            System.out.print(" " + s);
        }
        System.out.println();
    }



    /*
        GAME EVENT
     */

    /**
     * Dispatches a given event to all listeners of that event
     * @param event Event to dispatch
     */
    public void postEvent(Event event) {
        eventBus.postEvent(event);
    }

    /**
     * Adds an event handler
     * @param type type of event
     * @param handler handler object for the event
     * @param <T> event type
     */
    public <T extends Event> void addEventHandler(EventType<T> type, EventHandler<? super T> handler) {
        eventBus.addEventHandler(type, handler);
    }


    /**
     * Removes an event handler
     * @param type type of event
     * @param handler handler object for the event
     * @param <T> event type
     */
    public <T extends Event> void removeEventHandler(EventType<T> type, EventHandler<? super T> handler) {
        eventBus.removeEventHandler(type, handler);
    }

    /*
        GAME ACHIEVEMENT
     */

    /**
     * Getter for the Event Bus
     * @return The event bus
     */
    public EventBus getEventBus() {
        return eventBus;
    }

    /**
     * Adds objectives to the Level
     * @param objective the objective to add
     */
    public void addObjectives(Achievement objective){
        achievementSystem.addAchievement(objective);
    }

    /**
     * Checks if all objectives have been completed
     * @return true if all completed, false otherwise
     */
    public boolean checkAchievedAllObjectives(){
        return achievementSystem.checkAchievedAll();
    }


    /*
        TOGGLE PLAY-MODE / CREATE-MODE
     */

    /**
     * Checks if the level is running on Create Mode
     * @return True if on create mode, false otherwise
     */
    public boolean isCreateMode() {
        return isCreateMode;
    }

    /**
     * Sets the level into Create Mode
     * @param isCreateMode
     */
    public void setCreateMode(boolean isCreateMode) {
        this.isCreateMode = isCreateMode;
    }
}