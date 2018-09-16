package main.maploading;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import main.Array2DIterator;
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

    public Level(int nRows, int nCols, double size, String name) {
        this(nRows, nCols, size, name, false);
    }


    /*
        Name tools
     */

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    /*
        ENTITY GETTERS
     */
    public Terrain getTerrain(Vec2i pos) {
        return terrains[pos.getY()][pos.getX()];
    }

    public Prop getProp(Vec2i pos) {
        return props.get(pos);
    }

    public Pickup getPickup(Vec2i pos) {
        return pickups.get(pos);
    }

    public Enemy getEnemy(Vec2i pos) {
        return enemies.get(pos);
    }

    public Avatar getAvatar() {
        return avatar;
    }



    /*
        ENTITY SETTERS
     */
    public void setTerrain(Vec2i pos, Terrain terrain) {
        removeTerrain(pos);
        terrains[pos.getY()][pos.getX()] = terrain;
        moveEntityTo(pos, terrain);
    }


    public void setProp(Vec2i pos, Prop prop) {
        removeProp(pos);
        props.put(new Vec2i(pos), prop);
        moveEntityTo(pos, prop);

        notifyOnEnterByProp(pos, prop);
    }

    public void setPickup(Vec2i pos, Pickup pickup) {
        removePickup(pos);
        pickups.put(new Vec2i(pos), pickup);
        moveEntityTo(pos, pickup);
    }

    public void setEnemy(Vec2i pos, Enemy enemy) {
        removeEnemy(pos);
        enemies.put(new Vec2i(pos), enemy);
        moveEntityTo(pos, enemy);

        notifyOnEnterByEnemy(pos, enemy);
    }

    public void setAvatar(Vec2i pos, Avatar avatar) {
        removeAvatar();
        this.avatar = avatar;
        moveEntityTo(pos, avatar);

        notifyOnEnterByAvatar(pos, avatar);
    }

    private void moveEntityTo(Vec2i pos, Entity entity) {
        entity.moveTo(pos);
    }


    /*
       ADDING NEW ENTITIES
    */
    public void addTerrain(Vec2i pos, Terrain terrain) {
        setTerrain(pos, terrain);
        addEntityView(terrainLayer, terrain);
    }

    public void addProp(Vec2i pos, Prop prop) {
        setProp(pos, prop);
        addEntityView(propLayer, prop);
    }

    public void addPickup(Vec2i pos, Pickup pickup) {
        setPickup(pos, pickup);
        addEntityView(pickupLayer, pickup);
    }

    public void addEnemy(Vec2i pos, Enemy enemy) {
        setEnemy(pos, enemy);
        addEntityView(enemyLayer, enemy);
    }

    public void addAvatar(Vec2i pos, Avatar avatar) {
        setAvatar(pos, avatar);
        addEntityView(avatarLayer, avatar);
    }

    private void addEntityView(Group layer, Entity entity) {
        layer.getChildren().add(entity.getView());
    }


    /*
        ENTITY REMOVERS
     */
    public void removeAllAt(Vec2i pos, boolean replaceWithDefault) {
        removeTerrain(pos, replaceWithDefault);
        removeProp(pos);
        removePickup(pos);
        removeEnemy(pos);
        if (hasAvatar(pos)) removeAvatar();

    }

    public Terrain removeTerrain(Vec2i pos) {
        Terrain terrain = terrains[pos.getY()][pos.getX()];
        terrains[pos.getY()][pos.getX()] = null;

        if (terrain != null)
            removeEntityView(terrainLayer, terrain);

        return terrain;
    }

    public Terrain removeTerrain(Vec2i pos, boolean replaceWithDefault) {
        Terrain terrain = removeTerrain(pos);

        if (replaceWithDefault)
            addTerrain(pos, DEFAULT_TERRAIN.get());

        return terrain;
    }


    public Prop removeProp(Vec2i pos) {
        Prop prop = props.remove(pos);
        if (prop != null) {
            removeEntityView(propLayer, prop);
            notifyOnLeaveByProp(pos, prop);
        }

        return prop;
    }

    public Pickup removePickup(Vec2i pos) {
        Pickup pickup = pickups.remove(pos);
        if (pickup != null)
            removeEntityView(pickupLayer, pickup);
        return pickup;
    }

    public Enemy removeEnemy(Vec2i pos) {
        Enemy enemy = enemies.remove(pos);
        if (enemy != null) {
            removeEntityView(enemyLayer, enemy);
            notifyOnLeaveByEnemy(pos, enemy);
        }
        return enemy;
    }

    public void removeAvatar() {
        if (avatar != null) {
            removeEntityView(avatarLayer, avatar);
            notifyOnLeaveByAvatar(avatar.getGridPos(), avatar);
        }
        avatar = null;
    }

    private void removeEntityView(Group layer, Entity entity) {
        layer.getChildren().remove(entity.getView());
        entity.onRemovedFromLevel();
    }


    /*
        MOVE ENTITY
     */
    public void moveProp(Vec2i pos, Prop prop) {
        if (!hasProp(prop.getGridPos())) return;

        props.remove(prop.getGridPos());
        notifyOnLeaveByProp(prop.getGridPos(), prop);
        setProp(pos, prop);
    }

    public void moveEnemy(Vec2i pos, Enemy enemy) {
        if (!hasEnemy(enemy.getGridPos())) return;

        enemies.remove(enemy.getGridPos());
        notifyOnLeaveByEnemy(enemy.getGridPos(), enemy);
        setEnemy(pos, enemy);
    }

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
    private void notifyOnEnterByProp(Vec2i pos, Prop prop) {
        Terrain currTerrain = getTerrain(pos);
        if (currTerrain != null) currTerrain.onEnterByProp(prop);

        if (hasTerrain(pos))    getTerrain(pos).onEnterByProp(prop);
        if (hasPickup(pos))     getPickup(pos).onEnterByProp(prop);
        if (hasEnemy(pos))      getEnemy(pos).onEnterByProp(prop);
        if (hasAvatar(pos))     getAvatar().onEnterByProp(prop);
    }

    private void notifyOnEnterByEnemy(Vec2i pos, Enemy enemy) {
        if (hasTerrain(pos))    getTerrain(pos).onEnterByEnemy(enemy);
        if (hasProp(pos))       getProp(pos).onEnterByEnemy(enemy);
        if (hasPickup(pos))     getPickup(pos).onEnterByEnemy(enemy);
        if (hasAvatar(pos))     getAvatar().onEnterByEnemy(enemy);
    }

    private void notifyOnEnterByAvatar(Vec2i pos, Avatar avatar) {
        if (hasTerrain(pos))    getTerrain(pos).onEnterByAvatar(avatar);
        if (hasProp(pos))       getProp(pos).onEnterByAvatar(avatar);
        if (hasPickup(pos))     getPickup(pos).onEnterByAvatar(avatar);
        if (hasEnemy(pos))      getEnemy(pos).onEnterByAvatar(avatar);
    }

    private void notifyOnLeaveByProp(Vec2i pos, Prop prop) {
        if (hasTerrain(pos))    getTerrain(pos).onLeaveByProp(prop);
        if (hasProp(pos))       getProp(pos).onLeaveByProp(prop);
        if (hasEnemy(pos))      getEnemy(pos).onLeaveByProp(prop);
        if (hasAvatar(pos))     getAvatar().onLeaveByProp(prop);
    }

    private void notifyOnLeaveByEnemy(Vec2i pos, Enemy enemy) {
        if (hasTerrain(pos))    getTerrain(pos).onLeaveByEnemy(enemy);
        if (hasProp(pos))       getProp(pos).onLeaveByEnemy(enemy);
        if (hasPickup(pos))     getPickup(pos).onLeaveByEnemy(enemy);
        if (hasAvatar(pos))     getAvatar().onLeaveByEnemy(enemy);
    }

    private void notifyOnLeaveByAvatar(Vec2i pos, Avatar avatar) {
        if (hasTerrain(pos))    getTerrain(pos).onLeaveByAvatar(avatar);
        if (hasProp(pos))       getProp(pos).onLeaveByAvatar(avatar);
        if (hasPickup(pos))     getPickup(pos).onLeaveByAvatar(avatar);
        if (hasEnemy(pos))      getEnemy(pos).onLeaveByAvatar(avatar);
    }

    /*
        HAS ENTITY
     */
    public boolean hasTerrain(Vec2i pos) {
        return getTerrain(pos) != null;
    }

    public boolean hasProp(Vec2i pos) {
        return props.containsKey(pos);
    }

    public boolean hasPickup(Vec2i pos) {
        return pickups.containsKey(pos);
    }

    public boolean hasEnemy(Vec2i pos) {
        return enemies.containsKey(pos);
    }

    public boolean hasAvatar(Vec2i pos) {
        return avatar != null && avatar.getGridPos().equals(pos);
    }

    /*
        CAN STACK FOR
     */

    public boolean canPlaceTerrain(Vec2i pos, Terrain terrain) {
        if (hasTerrain(pos)) return false;

        return canReplaceTerrain(pos, terrain);
    }

    public boolean canPlaceProp(Vec2i pos, Prop prop) {
        if (hasProp(pos)) return false;

        return canReplaceProp(pos, prop);
    }

    public boolean canPlacePickup(Vec2i pos, Pickup pickup) {
        if (hasPickup(pos)) return false;

        return canReplacePickup(pos, pickup);
    }

    public boolean canPlaceEnemy(Vec2i pos, Enemy enemy) {
        if (hasEnemy(pos)) return false;

        return canReplaceEnemy(pos, enemy);
    }

    public boolean canPlaceAvatar(Vec2i pos, Avatar avatar) {
        if (hasAvatar(pos)) return false;

        return canReplaceAvatar(pos, avatar);
    }

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
    public boolean isPassableForAvatar(Vec2i pos, Avatar other) {
        if(!isValidGridPos(pos)) return false;

        Terrain terrain = getTerrain(pos);
        if (terrain != null && !terrain.isPassableForAvatar(other)) return false;

        Prop prop = getProp(pos);
        if (prop != null && !prop.isPassableForAvatar(other)) return false;

        Pickup pickup = getPickup(pos);
        if (pickup != null && !pickup.isPassableForAvatar(other)) return false;

        return true;
    }


    public boolean isPassableForEnemy(Vec2i pos, Enemy other) {
        if(!isValidGridPos(pos)) return false;

        Terrain terrain = getTerrain(pos);
        if (terrain != null && !terrain.isPassableForEnemy(other)) return false;

        Prop prop = getProp(pos);
        if (prop != null && !prop.isPassableForEnemy(other)) return false;

        Pickup pickup = getPickup(pos);
        if (pickup != null && !pickup.isPassableForEnemy(other)) return false;

        Enemy enemy = getEnemy(pos);
        if (enemy != null && !enemy.isPassableForEnemy(other)) return false;

        return true;
    }


    public boolean isPassableForProp(Vec2i pos, Prop other) {
        if(!isValidGridPos(pos)) return false;

        Terrain terrain = getTerrain(pos);
        if (terrain != null && !terrain.isPassableForProp(other)) return false;

        Prop prop = getProp(pos);
        if (prop != null && !prop.isPassableForProp(other)) return false;

        Pickup pickup = getPickup(pos);
        if (pickup != null && !pickup.isPassableForProp(other)) return false;

        return true;
    }

    /*
        Objective tools
     */

    public ArrayList<String> getObjectives() {
        return objectives;
    }

    public void setObjectives(ArrayList<String> objectives) {
        this.objectives = objectives;
    }

    /*
        Dimensions and View
     */
    public int getNRows() {
        return nRows;
    }

    public int getNCols() {
        return nCols;
    }

    public double getHeight() {
        return size * nRows;
    }

    public double getWidth() {
        return size * nCols;
    }

    public Node getView() {
        return view.getView();
    }


    public boolean isValidGridPos(Vec2i pos) {
        return pos.withinX(0, getNCols() - 1) && pos.withinY(0, getNRows() - 1);
    }

    public Vec2d gridPosToWorldPosCentre(Vec2i pos) {
        return new Vec2d((pos.getX() + 0.5) * size, (pos.getY() + 0.5) * size);
    }

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
        Vec2i max = new Vec2i(newNCol, newNRow);

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


    public Iterator<Terrain> getTerrainIterator() {
        return new Array2DIterator<>(terrains);
    }

    public Iterator<Prop> getPropIterator() {
        return new ArrayList<>(props.values()).iterator();
    }

    public Iterator<Pickup> getPickupIterator() {
        return new ArrayList<>(pickups.values()).iterator();
    }

    public Iterator<Enemy> getEnemyIterator() {
        return new ArrayList<>(enemies.values()).iterator();
    }

    public ArrayList<Enemy> getEnemies() {
        return new ArrayList<>(enemies.values());
    }




    public Iterator<Entity> getEntitiesAt(Vec2i pos) {
        ArrayList<Entity> entities = new ArrayList<>();
        if (hasTerrain(pos))    entities.add(getTerrain(pos));
        if (hasProp(pos))       entities.add(getProp(pos));
        if (hasPickup(pos))     entities.add(getPickup(pos));
        if (hasEnemy(pos))      entities.add(getEnemy(pos));
        if (hasAvatar(pos))     entities.add(avatar);

        return entities.iterator();
    }

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
    public void postEvent(Event event) {
        eventBus.postEvent(event);
    }

    public <T extends Event> void addEventHandler(EventType<T> type, EventHandler<? super T> handler) {
        eventBus.addEventHandler(type, handler);
    }

    public <T extends Event> void removeEventHandler(EventType<T> type, EventHandler<? super T> handler) {
        eventBus.removeEventHandler(type, handler);
    }

    /*
        GAME ACHIEVEMENT
     */

    public EventBus getEventBus() {
        return eventBus;
    }

    public void addObjectives(Achievement objective){
        achievementSystem.addAchievement(objective);
    }

    public boolean checkAchievedAllObjectives(){
        return achievementSystem.checkAchievedAll();
    }


    /*
        TOGGLE PLAY-MODE / CREATE-MODE
     */

    public boolean isCreateMode() {
        return isCreateMode;
    }

    public void setCreateMode(boolean isCreateMode) {
        this.isCreateMode = isCreateMode;
    }
}


