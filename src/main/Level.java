package main;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import main.util.Array2DIterator;
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
import main.math.Vec2d;
import main.math.Vec2i;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
public class Level {

    private final Supplier<Terrain> DEFAULT_TERRAIN = () -> new Ground(this);

    private ViewComponent view;
    private double size;
    private int nRows = 0;
    private int nCols = 0;

    private String name;
    private boolean isCreateMode = false;

    private ArrayList<String> objectives;

    private Group terrainLayer  = new Group();
    private Group propLayer     = new Group();
    private Group pickupLayer   = new Group();
    private Group enemyLayer    = new Group();
    private Group avatarLayer   = new Group();

    private Terrain[][] terrains;
    private HashMap<Vec2i, Prop> props;
    private HashMap<Vec2i, Enemy> enemies;
    private HashMap<Vec2i, Pickup> pickups;
    private Avatar avatar;

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

    public Level(int nRows, int nCols, double size, String name, boolean isCreateMode) {
        this.nRows = nRows;
        this.nCols = nCols;
        this.size  = size;
        this.name = name;
        this.isCreateMode = isCreateMode;

        this.objectives = new ArrayList<>();

        this.terrains = new Terrain[nRows][nCols];
        this.props = new HashMap<>();
        this.enemies = new HashMap<>();
        this.pickups = new HashMap<>();

        GridPane gridView = new GridPane();
        gridView.setMinSize(getWidth(), getHeight());

        view = new ViewComponent(gridView);

        for (int y = 0; y < nRows; y++) {
            for (int x = 0; x < nCols; x++) {
                Ground ground = new Ground(this);
                addTerrain(new Vec2i(x, y), ground);
            }
        }

        gridView.gridLinesVisibleProperty().set(true);

        view.addNode(terrainLayer);
        view.addNode(propLayer);
        view.addNode(pickupLayer);
        view.addNode(enemyLayer);
        view.addNode(avatarLayer);
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
        return terrains[pos.getY()][pos.getX()];
    }

    /**
     * Gets a Prop entity at the given position
     * @param pos : the position on the Level where you want to look for a Prop
     * @return The Prop entity at the given position if there is one
     */
    public Prop getProp(Vec2i pos) {
        return props.get(pos);
    }

    /**
     * Gets the Pickup entity at the given position
     * @param pos : the position on the Level where you want to look for a Pickup
     * @return The Pickup entity at the given position if there is one
     */
    public Pickup getPickup(Vec2i pos) {
        return pickups.get(pos);
    }

    /**
     * Gets the Enemy entity at the given position
     * @param pos : the position on the Level where you want to look for an Enemy
     * @return The Enemy entity at the given position if there is one
     */
    public Enemy getEnemy(Vec2i pos) {
        return enemies.get(pos);
    }

    /**
     * Gets the Avatar entity on the Level
     * @return The Avatar entity
     */
    public Avatar getAvatar() {
        return avatar;
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
        removeTerrain(pos);
        terrains[pos.getY()][pos.getX()] = terrain;
        moveEntityTo(pos, terrain);
    }

    /**
     * Sets a Prop entity on the level at a given position
     * Overwrites the previous Prop entity at the location
     * @param pos : position to set the Prop entity
     * @param prop : Prop entity to be set
     */
    public void setProp(Vec2i pos, Prop prop) {
        removeProp(pos);
        props.put(new Vec2i(pos), prop);
        moveEntityTo(pos, prop);

        notifyOnEnterByProp(pos, prop);
    }

    /**
     * Sets a Pickup entity on the level at a given position
     * Overwrites the previous Pickup entity at the location
     * @param pos : position to set the Pickup entity
     * @param pickup : Terrain entity to be set
     */
    public void setPickup(Vec2i pos, Pickup pickup) {
        removePickup(pos);
        pickups.put(new Vec2i(pos), pickup);
        moveEntityTo(pos, pickup);
    }

    /**
     * Sets a Enemy entity on the level at a given position
     * Overwrites the previous Enemy entity at the location
     * @param pos : position to set the Enemy entity
     * @param enemy : Enemy entity to be set
     */
    public void setEnemy(Vec2i pos, Enemy enemy) {
        removeEnemy(pos);
        enemies.put(new Vec2i(pos), enemy);
        moveEntityTo(pos, enemy);

        notifyOnEnterByEnemy(pos, enemy);
    }

    /**
     * Sets a Avatar entity on the level at a given position
     * Overwrites the previous Avatar entity at the location
     * @param pos : position to set the Avatar entity
     * @param avatar : Avatar entity to be set
     */
    public void setAvatar(Vec2i pos, Avatar avatar) {
        removeAvatar();
        this.avatar = avatar;
        moveEntityTo(pos, avatar);

        notifyOnEnterByAvatar(pos, avatar);
    }

    /**
     * Moves an entity from their current position to a new on on the Level
     * @param pos : new position
     * @param entity : entity to be moved
     */
    private void moveEntityTo(Vec2i pos, Entity entity) {
        entity.moveTo(pos);
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
        setTerrain(pos, terrain);
        addEntityView(terrainLayer, terrain);
    }

    /**
     * Sets a Prop entity onto the Level and also updates it on the Game view
     * @param pos : position on the Level
     * @param prop : Prop entity
     */
    public void addProp(Vec2i pos, Prop prop) {
        setProp(pos, prop);
        addEntityView(propLayer, prop);
    }

    /**
     * Sets a Pickup entity onto the Level and also updates it on the Game view
     * @param pos : position on the Level
     * @param pickup : Pickup entity
     */
    public void addPickup(Vec2i pos, Pickup pickup) {
        setPickup(pos, pickup);
        addEntityView(pickupLayer, pickup);
    }

    /**
     * Sets an Enemy entity onto the Level and also updates it on the Game view
     * @param pos : position on the Level
     * @param enemy : Enemy entity
     */
    public void addEnemy(Vec2i pos, Enemy enemy) {
        setEnemy(pos, enemy);
        addEntityView(enemyLayer, enemy);
    }

    /**
     * Sets an Avatar entity onto the Level and also updates it on the Game view
     * @param pos : position on the Level
     * @param avatar : Avatar entity
     */
    public void addAvatar(Vec2i pos, Avatar avatar) {
        setAvatar(pos, avatar);
        addEntityView(avatarLayer, avatar);
    }

    /**
     * Adds an entity to the Game View
     * @param layer : entity layer to be displayed
     * @param entity : entity to be added
     */
    private void addEntityView(Group layer, Entity entity) {
        layer.getChildren().add(entity.getView());
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
        if (hasAvatar(pos)) removeAvatar();

    }

    /**
     * Removes a Terrain entity at a certain position
     * @param pos : position on the Level
     * @return The Terrain entity just removed
     */
    public Terrain removeTerrain(Vec2i pos) {
        Terrain terrain = terrains[pos.getY()][pos.getX()];
        terrains[pos.getY()][pos.getX()] = null;

        if (terrain != null)
            removeEntityView(terrainLayer, terrain);

        return terrain;
    }

    /**
     * Overloaded method: allows a flag to set a Ground tile at the removed Terrain entity
     * @param pos : position on the Level
     * @param replaceWithDefault : flag to replace the removed Terrain with a Ground tile
     * @return The Terrain entity just removed
     */
    public Terrain removeTerrain(Vec2i pos, boolean replaceWithDefault) {
        Terrain terrain = removeTerrain(pos);

        if (replaceWithDefault)
            addTerrain(pos, DEFAULT_TERRAIN.get());

        return terrain;
    }


    /**
     * Removes a Prop entity at a certain position
     * @param pos : position on the Level
     * @return The Prop entity just removed
     */
    public Prop removeProp(Vec2i pos) {
        Prop prop = props.remove(pos);
        if (prop != null) {
            removeEntityView(propLayer, prop);
            notifyOnLeaveByProp(pos, prop);
        }

        return prop;
    }

    /**
     * Removes a Pickup entity at a certain position
     * @param pos : position on the Level
     * @return The Pickup entity just removed
     */
    public Pickup removePickup(Vec2i pos) {
        Pickup pickup = pickups.remove(pos);
        if (pickup != null)
            removeEntityView(pickupLayer, pickup);
        return pickup;
    }

    /**
     * Removes a Enemy entity at a certain position
     * @param pos : position on the Level
     * @return The Enemy entity just removed
     */
    public Enemy removeEnemy(Vec2i pos) {
        Enemy enemy = enemies.remove(pos);
        if (enemy != null) {
            removeEntityView(enemyLayer, enemy);
            notifyOnLeaveByEnemy(pos, enemy);
        }
        return enemy;
    }

    /**
     * Removes the Avatar entity from the Level
     * I.e when the Avatar dies
     */
    public void removeAvatar() {
        if (avatar != null) {
            removeEntityView(avatarLayer, avatar);
            notifyOnLeaveByAvatar(avatar.getGridPos(), avatar);
        }
        avatar = null;
    }

    /**
     * Removes the graphical component of an entity from the Game View
     * @param layer : layer the entity is on
     * @param entity : the entity to be removed
     */
    private void removeEntityView(Group layer, Entity entity) {
        layer.getChildren().remove(entity.getView());
        entity.onRemovedFromLevel();
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
        if (!hasProp(prop.getGridPos())) return;

        props.remove(prop.getGridPos());
        notifyOnLeaveByProp(prop.getGridPos(), prop);
        setProp(pos, prop);
    }

    /**
     * Moves an Enemy entity on the Level
     * @param pos : position to move onto
     * @param enemy : Enemy entity to move
     */
    public void moveEnemy(Vec2i pos, Enemy enemy) {
        if (!hasEnemy(enemy.getGridPos())) return;

        enemies.remove(enemy.getGridPos());
        notifyOnLeaveByEnemy(enemy.getGridPos(), enemy);
        setEnemy(pos, enemy);
    }

    /**
     * Moves the Avatar on the Level
     * @param pos : position to move onto
     */
    public void moveAvatar(Vec2i pos) {
        if (avatar == null) return;

        Vec2i prev = avatar.getGridPos();
        moveEntityTo(pos, avatar);
        notifyOnLeaveByAvatar(prev, avatar); // only call after left
        notifyOnEnterByAvatar(pos, avatar);
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
        Terrain currTerrain = getTerrain(pos);
        if (currTerrain != null) currTerrain.onEnterByProp(prop);

        if (hasTerrain(pos))    getTerrain(pos).onEnterByProp(prop);
        if (hasPickup(pos))     getPickup(pos).onEnterByProp(prop);
        if (hasEnemy(pos))      getEnemy(pos).onEnterByProp(prop);
        if (hasAvatar(pos))     getAvatar().onEnterByProp(prop);
    }

    /**
     * Notifies the Terrain entity when an Enemy moves on top of it
     * @param pos : position of the Terrain entity
     * @param enemy : Enemy which was moved
     */
    private void notifyOnEnterByEnemy(Vec2i pos, Enemy enemy) {
        if (hasTerrain(pos))    getTerrain(pos).onEnterByEnemy(enemy);
        if (hasProp(pos))       getProp(pos).onEnterByEnemy(enemy);
        if (hasPickup(pos))     getPickup(pos).onEnterByEnemy(enemy);
        if (hasAvatar(pos))     getAvatar().onEnterByEnemy(enemy);
    }

    /**
     * Notifies the Terrain entity and Pickup entity when the Avatar moves on top of it
     * @param pos : position of the Terrain & Pickup entity
     * @param avatar : Avatar entity
     */
    private void notifyOnEnterByAvatar(Vec2i pos, Avatar avatar) {
        if (hasTerrain(pos))    getTerrain(pos).onEnterByAvatar(avatar);
        if (hasProp(pos))       getProp(pos).onEnterByAvatar(avatar);
        if (hasPickup(pos))     getPickup(pos).onEnterByAvatar(avatar);
        if (hasEnemy(pos))      getEnemy(pos).onEnterByAvatar(avatar);
    }

    /**
     * Notifies the Terrain entity when a Prop is moved from on top of it
     * @param pos : position of the Terrain
     * @param prop : Prop which was moved
     */
    private void notifyOnLeaveByProp(Vec2i pos, Prop prop) {
        if (hasTerrain(pos))    getTerrain(pos).onLeaveByProp(prop);
        if (hasProp(pos))       getProp(pos).onLeaveByProp(prop);
        if (hasEnemy(pos))      getEnemy(pos).onLeaveByProp(prop);
        if (hasAvatar(pos))     getAvatar().onLeaveByProp(prop);
    }

    /**
     * Notifies the Terrain when an Enemy is moved from on top of it
     * @param pos : position of the Terrain
     * @param enemy : Enemy which moved
     */
    private void notifyOnLeaveByEnemy(Vec2i pos, Enemy enemy) {
        if (hasTerrain(pos))    getTerrain(pos).onLeaveByEnemy(enemy);
        if (hasProp(pos))       getProp(pos).onLeaveByEnemy(enemy);
        if (hasPickup(pos))     getPickup(pos).onLeaveByEnemy(enemy);
        if (hasAvatar(pos))     getAvatar().onLeaveByEnemy(enemy);
    }

    /**
     * Notifies the Terrain when an Avatar is moved from on top of it
     * @param pos : position of the Terrain
     * @param avatar : Avatar entity
     */
    private void notifyOnLeaveByAvatar(Vec2i pos, Avatar avatar) {
        if (hasTerrain(pos))    getTerrain(pos).onLeaveByAvatar(avatar);
        if (hasProp(pos))       getProp(pos).onLeaveByAvatar(avatar);
        if (hasPickup(pos))     getPickup(pos).onLeaveByAvatar(avatar);
        if (hasEnemy(pos))      getEnemy(pos).onLeaveByAvatar(avatar);
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
        return getTerrain(pos) != null;
    }

    /**
     * Checks if there is a Prop entity at a given position
     * @param pos : position to check
     * @return True if the Prop exists, False otherwise
     */
    public boolean hasProp(Vec2i pos) {
        return props.containsKey(pos);
    }

    /**
     * Checks if there is a Pickup entity at a given position
     * @param pos : position to check
     * @return True if the Pickup exists, False otherwise
     */
    public boolean hasPickup(Vec2i pos) {
        return pickups.containsKey(pos);
    }

    /**
     * Checks if there is an Enemy entity at a given position
     * @param pos : position to check
     * @return True if the Enemy exists, False otherwise
     */
    public boolean hasEnemy(Vec2i pos) {
        return enemies.containsKey(pos);
    }

    /**
     * Checks if there is an Avatar entity at a given position
     * @param pos : position to check
     * @return True if the Avatar exists, False otherwise
     */
    public boolean hasAvatar(Vec2i pos) {
        return avatar != null && avatar.getGridPos().equals(pos);
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
        if (hasProp(pos)) {
            if (!terrain.canStackForProp(getProp(pos))) return false;
        }

        if (hasPickup(pos)) {
            if (!terrain.canStackForPickup(getPickup(pos))) return false;
        }

        if (hasEnemy(pos)) {
            if(!terrain.canStackForEnemy(getEnemy(pos))) {
                return false;
            }
        }

        if (hasAvatar(pos)) {
            if (!terrain.canStackForAvatar(getAvatar())) return false;
        }

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
        if (hasTerrain(pos)) {
            if (!getTerrain(pos).canStackForProp(prop)) return false;
        }

        if (hasPickup(pos)) {
            if (!prop.canStackForPickup(getPickup(pos))) return false;
        }

        if (hasEnemy(pos)) {
            if (!prop.canStackForEnemy(getEnemy(pos))) return false;
        }

        if (hasAvatar(pos)) {
            if (!prop.canStackForAvatar(getAvatar())) return false;
        }

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
        if (hasTerrain(pos)) {
            if (!getTerrain(pos).canStackForPickup(pickup)) return false;
        }

        if (hasProp(pos)) {
            if (!getProp(pos).canStackForPickup(pickup)) return false;
        }

        if (hasEnemy(pos)) {
            if (!pickup.canStackForEnemy(getEnemy(pos))) return false;
        }

        if (hasAvatar(pos)) {
            if (!pickup.canStackForAvatar(getAvatar())) return false;
        }

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
            if (!enemy.canStackForAvatar(getAvatar())) return false;
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
        if (hasTerrain(pos)) {
            if (!getTerrain(pos).canStackForAvatar(avatar)) return false;
        }

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

        if (hasTerrain(pos) && !getTerrain(pos).isPassableForAvatar(other))   return false;
        if (hasProp(pos)    && !getProp(pos).isPassableForAvatar(other))      return false;
        if (hasPickup(pos)  && !getPickup(pos).isPassableForAvatar(other))    return false;
        if (hasEnemy(pos)   && !getEnemy(pos).isPassableForAvatar(other))     return false;
        if (hasAvatar(pos)  && !getAvatar().isPassableForAvatar(other))       return false;
        return true;
    }

    /**
     * Checks if an Enemy entity can pass through a certain tile
     * @param pos : position to check
     * @param other : Other entity which wants to move onto the tile
     * @return True if the entity can move, false otherwise
     */
    public boolean isPassableForEnemy(Vec2i pos, Enemy other) {
        if(!isValidGridPos(pos)) return false;

        if (hasTerrain(pos) && !getTerrain(pos).isPassableForEnemy(other))   return false;
        if (hasProp(pos)    && !getProp(pos).isPassableForEnemy(other))      return false;
        if (hasPickup(pos)  && !getPickup(pos).isPassableForEnemy(other))    return false;
        if (hasEnemy(pos)   && !getEnemy(pos).isPassableForEnemy(other))     return false;
        if (hasAvatar(pos)  && !getAvatar().isPassableForEnemy(other))       return false;
        return true;
    }

    /**
     * Checks if an Prop entity can pass through a certain tile
     * @param pos : position to check
     * @param other : Other entity which wants to move onto the tile
     * @return True if the entity can move, false otherwise
     */
    public boolean isPassableForProp(Vec2i pos, Prop other) {
        if(!isValidGridPos(pos)) return false;

        if (hasTerrain(pos) && !getTerrain(pos).isPassableForProp(other))   return false;
        if (hasProp(pos)    && !getProp(pos).isPassableForProp(other))      return false;
        if (hasPickup(pos)  && !getPickup(pos).isPassableForProp(other))    return false;
        if (hasEnemy(pos)   && !getEnemy(pos).isPassableForProp(other))     return false;
        if (hasAvatar(pos)  && !getAvatar().isPassableForProp(other))       return false;
        return true;
    }


    public boolean onPushByAvatar(Vec2i pos, Avatar avatar) {
        if (!isValidGridPos(pos)) return false;
        if (hasTerrain(pos) && !getTerrain(pos).onPush(avatar)) return false;
        if (hasProp(pos) && !getProp(pos).onPush(avatar)) return false;
        return true;
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
        return new Array2DIterator<>(terrains);
    }

    /**
     * Gets an iterator for the Prop entities of the Level
     * @return an iterator of the Prop entities
     */
    public Iterator<Prop> getPropIterator() {
        return new ArrayList<>(props.values()).iterator();
    }

    /**
     * Gets an iterator for the Pickup entities of the Level
     * @return an iterator of the Pickup entities
     */
    public Iterator<Pickup> getPickupIterator() {
        return new ArrayList<>(pickups.values()).iterator();
    }

    /**
     * Gets an iterator for the Enemy entities of the Level
     * @return an iterator of the Pickup entities
     */
    public Iterator<Enemy> getEnemyIterator() {
        return new ArrayList<>(enemies.values()).iterator();
    }

    /**
     * Gets an ArrayList of the Enemy entities of the Level
     * @return an ArrayList of the Enemy entities
     */
    public ArrayList<Enemy> getEnemies() {
        return new ArrayList<>(enemies.values());
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
        if (hasAvatar(pos))     entities.add(avatar);

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