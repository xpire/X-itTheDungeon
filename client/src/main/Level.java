package main;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import main.app.Main;
import main.trigger.objective.Objective;
import main.trigger.objective.ObjectiveSystem;
import main.component.ViewComponent;
import main.entities.Avatar;
import main.entities.Entity;
import main.entities.enemies.Enemy;
import main.entities.pickup.Pickup;
import main.entities.prop.Prop;
import main.entities.terrain.Ground;
import main.entities.terrain.Terrain;
import main.events.EventBus;
import main.maploading.EntityLayer;
import main.maploading.HashMapLayer;
import main.maploading.SingletonLayer;
import main.maploading.TerrainLayer;
import main.math.Vec2d;
import main.math.Vec2i;
import main.trigger.objective.ObjectiveView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Consumer;

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
public class Level {

    private ViewComponent view;
    private double size;
    private int nRows;
    private int nCols;

    private String name;
    private boolean isCreateMode;

    private ArrayList<EntityLayer> layers;
    private TerrainLayer terrains;
    private HashMapLayer<Prop> props;
    private HashMapLayer<Pickup> pickups;
    private HashMapLayer<Enemy> enemies;
    private SingletonLayer<Avatar> avatarLayer;

    private EventBus eventBus = new EventBus();
    private ObjectiveSystem objectiveSystem = new ObjectiveSystem(eventBus);

    private ImageView background;

    /**
     * Constructor for the Level class
     * @param nRows : number of rows
     * @param nCols : number of columns
     * @param size : size of each tile (in the view)
     * @param name : name of the Level
     * @param isCreateMode : flag to differentiate events that should be triggered
     */

    //TODO does not resize at the start
    public Level(int nRows, int nCols, double size, String name, boolean isCreateMode) {
        this.nRows = nRows;
        this.nCols = nCols;
        this.size  = size;
        this.name = name;
        this.isCreateMode = isCreateMode;

        view = new ViewComponent();

        background = new ImageView(new Image("asset/sprite/terrain/ground/8.png"));
        view.addNode(background);

        terrains    = new TerrainLayer(nRows, nCols, () -> new Ground(this));
        props       = new HashMapLayer<>();
        pickups     = new HashMapLayer<>();
        enemies     = new HashMapLayer<>();
        avatarLayer = new SingletonLayer<>();

        props.setOnEntityEnter(this::notifyOnEnterByProp);
        props.setOnEntityLeave(this::notifyOnLeaveByProp);
        enemies.setOnEntityEnter(this::notifyOnEnterByEnemy);
        enemies.setOnEntityLeave(this::notifyOnLeaveByEnemy);
        avatarLayer.setOnEntityEnter(this::notifyOnEnterByAvatar);
        avatarLayer.setOnEntityLeave(this::notifyOnLeaveByAvatar);

        layers = new ArrayList<>();
        layers.add(terrains);
        layers.add(props);
        layers.add(pickups);
        layers.add(enemies);
        layers.add(avatarLayer);

        layers.forEach(layer -> view.addNode(layer.getView()));

        for (int y = 0; y < nRows; y++) {
            for (int x = 0; x < nCols; x++) {
                Ground ground = new Ground(this);
                addTerrain(new Vec2i(x, y), ground);
            }
        }

        rescale(size);
    }


    /**
     * Level constructor which sets PlayMode as the default flag
     * @param nRows : number of rows
     * @param nCols : number of cols
     * @param size : size of each tile (in the view)
     * @param name : name of the Level
     */
    public Level(int nRows, int nCols, double size, String name) {
        this(nRows, nCols, size, name, false);
    }


    public void rescale(double tileSize) {
        size = tileSize;
        layers.forEach(EntityLayer::rescale);
        background.setFitWidth(getWidth());
        background.setFitHeight(getHeight());
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


    /*
        ENTITY GETTERS
     */

    /**
     * Gets a Terrain entity at the given position
     * @param pos : the position on the Level where you want to look for a Terrain
     * @return The Terrain entity at the given position if there is one
     */
    public Terrain getTerrain(Vec2i pos) {
        return terrains.getEntity(pos);
    }

    /**
     * Gets a Prop entity at the given position
     * @param pos : the position on the Level where you want to look for a Prop
     * @return The Prop entity at the given position if there is one
     */
    public Prop getProp(Vec2i pos) {
        return props.getEntity(pos);
    }

    /**
     * Gets the Pickup entity at the given position
     * @param pos : the position on the Level where you want to look for a Pickup
     * @return The Pickup entity at the given position if there is one
     */
    public Pickup getPickup(Vec2i pos) {
        return pickups.getEntity(pos);
    }

    /**
     * Gets the Enemy entity at the given position
     * @param pos : the position on the Level where you want to look for an Enemy
     * @return The Enemy entity at the given position if there is one
     */
    public Enemy getEnemy(Vec2i pos) {
        return enemies.getEntity(pos);
    }

    /**
     * Gets the Avatar entity on the Level
     * @return The Avatar entity
     */
    public Avatar getAvatar() {
        return avatarLayer.get();
    }



    /*
        ENTITY SETTERS
     */

    /**
     * Sets a Terrain entity on the level at a given position
     * Overwrites the previous Terrain entity at the location
     * @param pos : position to set the Terrain entity
     * @param terrain : Terrain entity to be set
     */
    public void setTerrain(Vec2i pos, Terrain terrain) {
        terrains.setEntity(pos, terrain);
    }

    /**
     * Sets a Prop entity on the level at a given position
     * Overwrites the previous Prop entity at the location
     * @param pos : position to set the Prop entity
     * @param prop : Prop entity to be set
     */
    public void setProp(Vec2i pos, Prop prop) {
        props.setEntity(pos, prop);
    }

    /**
     * Sets a Pickup entity on the level at a given position
     * Overwrites the previous Pickup entity at the location
     * @param pos : position to set the Pickup entity
     * @param pickup : Terrain entity to be set
     */
    public void setPickup(Vec2i pos, Pickup pickup) {
        pickups.setEntity(pos, pickup);
    }

    /**
     * Sets a Enemy entity on the level at a given position
     * Overwrites the previous Enemy entity at the location
     * @param pos : position to set the Enemy entity
     * @param enemy : Enemy entity to be set
     */
    public void setEnemy(Vec2i pos, Enemy enemy) {
        enemies.setEntity(pos, enemy);
    }

    /**
     * Sets a Avatar entity on the level at a given position
     * Overwrites the previous Avatar entity at the location
     * @param pos : position to set the Avatar entity
     * @param avatar : Avatar entity to be set
     */
    public void setAvatar(Vec2i pos, Avatar avatar) {
        avatarLayer.setEntity(pos, avatar);
    }



    /*
       ADDING NEW ENTITIES
    */

    /**
     * Sets a Terrain entity onto the Level and also updates it on the Game view
     * @param pos : position on the Level
     * @param terrain : Terrain entity
     */
    public void addTerrain(Vec2i pos, Terrain terrain) {
        terrains.addEntity(pos, terrain);
    }

    /**
     * Sets a Prop entity onto the Level and also updates it on the Game view
     * @param pos : position on the Level
     * @param prop : Prop entity
     */
    public void addProp(Vec2i pos, Prop prop) {
        props.addEntity(pos, prop);
    }

    /**
     * Sets a Pickup entity onto the Level and also updates it on the Game view
     * @param pos : position on the Level
     * @param pickup : Pickup entity
     */
    public void addPickup(Vec2i pos, Pickup pickup) {
        pickups.addEntity(pos, pickup);
    }

    /**
     * Sets an Enemy entity onto the Level and also updates it on the Game view
     * @param pos : position on the Level
     * @param enemy : Enemy entity
     */
    public void addEnemy(Vec2i pos, Enemy enemy) {
        enemies.addEntity(pos, enemy);
    }

    /**
     * Sets an Avatar entity onto the Level and also updates it on the Game view
     * @param pos : position on the Level
     * @param avatar : Avatar entity
     */
    public void addAvatar(Vec2i pos, Avatar avatar) {
        avatarLayer.addEntity(pos, avatar);
    }



    /*
        ENTITY REMOVERS
     */

    /**
     * Removes all entities at a position
     * @param pos : position on the Level to remove everything
     * @param replaceWithDefault : flag to replace the cleared tile with a Ground tile
     */
    public void removeAllAt(Vec2i pos, boolean replaceWithDefault) {
        removeTerrain(pos, replaceWithDefault);
        removeProp(pos);
        removePickup(pos);
        removeEnemy(pos);
        removeAvatar(pos);
    }


    /**
     * Removes a Terrain entity at a certain position
     * @param pos : position on the Level
     * @return The Terrain entity just removed
     */
    public Terrain removeTerrain(Vec2i pos) {
        return terrains.removeEntity(pos);
    }

    /**
     * Overloaded method: allows a flag to set a Ground tile at the removed Terrain entity
     * @param pos : position on the Level
     * @param replaceWithDefault : flag to replace the removed Terrain with a Ground tile
     * @return The Terrain entity just removed
     */
    public Terrain removeTerrain(Vec2i pos, boolean replaceWithDefault) {
        return terrains.removeEntity(pos, replaceWithDefault);
    }


    /**
     * Removes a Prop entity at a certain position
     * @param pos : position on the Level
     * @return The Prop entity just removed
     */
    public Prop removeProp(Vec2i pos) {
        Prop prop = props.removeEntity(pos);
        if (prop != null)
            notifyOnLeaveByProp(pos, prop);
        return prop;
    }

    /**
     * Removes a Pickup entity at a certain position
     * @param pos : position on the Level
     * @return The Pickup entity just removed
     */
    public Pickup removePickup(Vec2i pos) {
        return pickups.removeEntity(pos);
    }

    /**
     * Removes a Enemy entity at a certain position
     * @param pos : position on the Level
     * @return The Enemy entity just removed
     */
    public Enemy removeEnemy(Vec2i pos) {
        return enemies.removeEntity(pos);
    }

    /**
     * Removes the Avatar entity from the Level
     * I.e when the Avatar dies
     */
    public void removeAvatar() {
        avatarLayer.remove();
    }

    public void removeAvatar(Vec2i pos) {
        avatarLayer.removeEntity(pos);
    }


    /*
        MOVE ENTITY
     */

    /**
     * Moves a Prop entity on the Level
     * @param pos : position to move onto
     * @param prop : Prop entity
     */
    public void moveProp(Vec2i pos, Prop prop) {
        props.moveEntity(pos, prop);
    }

    /**
     * Moves an Enemy entity on the Level
     * @param pos : position to move onto
     * @param enemy : Enemy entity to move
     */
    public void moveEnemy(Vec2i pos, Enemy enemy) {
        enemies.moveEntity(pos, enemy);
    }

    /**
     * Moves the Avatar on the Level
     * @param pos : position to move onto
     */
    public void moveAvatar(Vec2i pos) {
        avatarLayer.moveEntity(pos, avatarLayer.get());
    }


    /*
        NOTIFY TILE OBSERVERS
     */

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


    private void forEachEntityAt(Vec2i pos, Consumer<Entity> action) {
        layers.forEach(layer -> {

            if (layer.hasEntity(pos))
                action.accept(layer.getEntity(pos));
        });
    }

    /*
        HAS ENTITY
     */

    /**
     * Checks if there is a Terrain entity at a given position
     * @param pos : position to check
     * @return True if the Terrain exists, False otherwise
     */
    public boolean hasTerrain(Vec2i pos) {
        return terrains.hasEntity(pos);
    }

    /**
     * Checks if there is a Prop entity at a given position
     * @param pos : position to check
     * @return True if the Prop exists, False otherwise
     */
    public boolean hasProp(Vec2i pos) {
        return props.hasEntity(pos);
    }

    /**
     * Checks if there is a Pickup entity at a given position
     * @param pos : position to check
     * @return True if the Pickup exists, False otherwise
     */
    public boolean hasPickup(Vec2i pos) {
        return pickups.hasEntity(pos);
    }

    /**
     * Checks if there is an Enemy entity at a given position
     * @param pos : position to check
     * @return True if the Enemy exists, False otherwise
     */
    public boolean hasEnemy(Vec2i pos) {
        return enemies.hasEntity(pos);
    }

    /**
     * Checks if there is an Avatar entity at a given position
     * @param pos : position to check
     * @return True if the Avatar exists, False otherwise
     */
    public boolean hasAvatar(Vec2i pos) {
        return avatarLayer.hasEntity(pos);
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
        if (hasTerrain(pos)) return false;
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
        if (hasProp(pos)) return false;
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
        if (hasPickup(pos)) return false;
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
        if (hasEnemy(pos)) return false;
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
        if (hasAvatar(pos)) return false;
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
        if (hasProp(pos)    && !terrain.canStackForProp(getProp(pos)))      return false;
        if (hasPickup(pos)  && !terrain.canStackForPickup(getPickup(pos)))  return false;
        if (hasEnemy(pos)   && !terrain.canStackForEnemy(getEnemy(pos)))    return false;
        if (hasAvatar(pos)  && !terrain.canStackForAvatar(getAvatar()))     return false;
        return true;
    }

    /**
     * Checks if you can place a Prop entity at a certain position, without
     * violating stacking rules if a current Prop entity is removed
     * @param pos : position to check
     * @param prop : Prop to be placed
     * @return True if you can place it, false otherwise
     */
    public boolean canReplaceProp(Vec2i pos, Prop prop) {
        if (hasTerrain(pos) && !getTerrain(pos).canStackForProp(prop))  return false;
        if (hasPickup(pos)  && !prop.canStackForPickup(getPickup(pos))) return false;
        if (hasEnemy(pos)   && !prop.canStackForEnemy(getEnemy(pos)))   return false;
        if (hasAvatar(pos)  && !prop.canStackForAvatar(getAvatar()))    return false;
        return true;
    }

    /**
     * Checks if you can place a Pickup entity at a certain position, without
     * violating stacking rules if a current Pickup entity is removed
     * @param pos : position to check
     * @param pickup : Pickup to be placed
     * @return True if you can place it, false otherwise
     */
    public boolean canReplacePickup(Vec2i pos, Pickup pickup) {
        if (hasTerrain(pos) && !getTerrain(pos).canStackForPickup(pickup))  return false;
        if (hasProp(pos)    && !getProp(pos).canStackForPickup(pickup))     return false;
        if (hasEnemy(pos)   && !pickup.canStackForEnemy(getEnemy(pos)))     return false;
        if (hasAvatar(pos)  && !pickup.canStackForAvatar(getAvatar()))      return false;
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
        if (hasTerrain(pos) && !getTerrain(pos).canStackForEnemy(enemy))    return false;
        if (hasProp(pos)    && !getProp(pos).canStackForEnemy(enemy))       return false;
        if (hasPickup(pos)  && !getPickup(pos).canStackForEnemy(enemy))     return false;
        if (hasAvatar(pos)  && !enemy.canStackForAvatar(getAvatar()))       return false;
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
        if (hasTerrain(pos) && !getTerrain(pos).canStackForAvatar(avatar))    return false;
        if (hasProp(pos)    && !getProp(pos).canStackForAvatar(avatar))       return false;
        if (hasPickup(pos)  && !getPickup(pos).canStackForAvatar(avatar))     return false;
        if (hasEnemy(pos)   && !getEnemy(pos).canStackForAvatar(avatar))      return false;
        return true;
    }


    /*
        ENTITY PASSABLE FOR
     */

    /**
     * Checks if an Avatar entity can pass through a certain tile
     * @param pos : position to check
     * @param other : Other entity which wants to move onto the tile
     * @return True if the entity can move, false otherwise
     */
    public boolean isPassableForAvatar(Vec2i pos, Avatar other) {
        if(!isValidGridPos(pos)) return false;
        return layers.stream()
                .allMatch(l -> !l.hasEntity(pos) || l.getEntity(pos).isPassableForAvatar(other));
    }

    /**
     * Checks if an Enemy entity can pass through a certain tile
     * @param pos : position to check
     * @param other : Other entity which wants to move onto the tile
     * @return True if the entity can move, false otherwise
     */
    public boolean isPassableForEnemy(Vec2i pos, Enemy other) {
        if(!isValidGridPos(pos)) return false;
        return layers.stream()
                .allMatch(l -> !l.hasEntity(pos) || l.getEntity(pos).isPassableForEnemy(other));
    }

    /**
     * Checks if an Prop entity can pass through a certain tile
     * @param pos : position to check
     * @param other : Other entity which wants to move onto the tile
     * @return True if the entity can move, false otherwise
     */
    public boolean isPassableForProp(Vec2i pos, Prop other) {
        if(!isValidGridPos(pos)) return false;
        return layers.stream()
                .allMatch(l -> !l.hasEntity(pos) || l.getEntity(pos).isPassableForProp(other));
    }


    public boolean onPushByAvatar(Vec2i pos, Avatar avatar) {
        if (!isValidGridPos(pos)) return false;
        if (hasTerrain(pos) && !getTerrain(pos).onPush(avatar)) return false;
        if (hasProp(pos) && !getProp(pos).onPush(avatar)) return false;
        return true;
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
    public Group getView() {
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
     * @param newNRows : new # of rows for the Level
     * @param newNCols : new # of cols for the Level
     */
    public void resize(int newNRows, int newNCols) {
        layers.forEach(layer -> layer.resize(newNRows, newNCols));
        nRows = newNRows;
        nCols = newNCols;
    }

    /**
     * Gets an iterator for the Terrain entities of the Level
     * @return an iterator of the Terrain entities
     */
    public Iterator<Terrain> getTerrainIterator() {
        return terrains.iterator();
    }

    /**
     * Gets an iterator for the Prop entities of the Level
     * @return an iterator of the Prop entities
     */
    public Iterator<Prop> getPropIterator() {
        return props.iterator();
    }

    /**
     * Gets an iterator for the Pickup entities of the Level
     * @return an iterator of the Pickup entities
     */
    public Iterator<Pickup> getPickupIterator() {
        return pickups.iterator();
    }

    /**
     * Gets an iterator for the Enemy entities of the Level
     * @return an iterator of the Pickup entities
     */
    public Iterator<Enemy> getEnemyIterator() {
        return enemies.iterator();
    }

    public ArrayList<Enemy> getEnemies() {
        return enemies.getEntities();
    }

    /**
     * Gets an iterator for the entities at a certain position
     * @param pos : the position to find entities
     * @return an iterator of the entities at the position
     */
    public Iterator<Entity> getEntitiesAt(Vec2i pos) {
        ArrayList<Entity> entities = new ArrayList<>();
        if (hasTerrain(pos))    entities.add(getTerrain(pos));
        if (hasProp(pos))       entities.add(getProp(pos));
        if (hasPickup(pos))     entities.add(getPickup(pos));
        if (hasEnemy(pos))      entities.add(getEnemy(pos));
        if (hasAvatar(pos))     entities.add(getAvatar());

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
        Main.playModeBus.postEvent(event);
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

    public void addObjective(Objective objective) {
        objectiveSystem.addObjective(objective);
    }

    public Iterator<ObjectiveView> getObjectiveViews() {
        return objectiveSystem.getObjectiveViews();
    }

    /**
     * Checks if all objective have been completed
     * @return true if all completed, false otherwise
     */
    public boolean checkAchievedAllObjectives(){
        return objectiveSystem.checkTriggeredAll();
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
     * @param isCreateMode whether or not the level is in Creative mode
     */
    public void setCreateMode(boolean isCreateMode) {
        this.isCreateMode = isCreateMode;
    }

    public String listObjectives() {
        return objectiveSystem.getObjectives();
    }

    public void clearObjectives() {
        objectiveSystem.clearObjectives();
    }

    public double getSize() {
        return size;
    }
}