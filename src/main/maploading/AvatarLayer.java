package main.maploading;

import javafx.scene.Group;
import javafx.scene.Node;
import main.component.ViewComponent;
import main.entities.Avatar;
import main.entities.Entity;
import main.entities.enemies.Enemy;
import main.entities.pickup.Pickup;
import main.entities.prop.Prop;
import main.entities.terrain.Ground;
import main.entities.terrain.Terrain;
import main.math.Vec2i;
import main.util.Array2DIterator;

import java.util.Iterator;
import java.util.function.Supplier;

public class AvatarLayer implements EntityLayer<Avatar> {

    private final Supplier<Terrain> DEFAULT_TERRAIN = () -> new Ground(this);

    private ViewComponent view;
    private double size;
    private int nRows;
    private int nCols;
    private Group layer  = new Group();

    private Terrain[][] terrains;

    /**
     * Constructor for the Level class
     * @param nRows : number of rows
     * @param nCols : number of columns
     * @param size : size of each tile (in the view)
     */
    public AvatarLayer(int nRows, int nCols, double size) {
        this.nRows = nRows;
        this.nCols = nCols;
        this.size  = size;

        this.terrains = new Terrain[nRows][nCols];

//        GridPane gridView = new GridPane();
//        gridView.setMinSize(nCols * size, nRows * size);
//        gridView.gridLinesVisibleProperty().set(true);

        view = new ViewComponent();
    }


    /**
     * Gets a Terrain entity at the given position
     * @param pos : the position on the Level where you want to look for a Terrain
     * @return The Terrain entity at the given position if there is one
     */
    @Override
    public Terrain getEntity(Vec2i pos) {
        return terrains[pos.getY()][pos.getX()];
    }


    /**
     * Sets a Terrain entity on the level at a given position
     * Overwrites the previous Terrain entity at the location
     * @param pos : position to set the Terrain entity
     * @param terrain : Terrain entity to be set
     */
    @Override
    public void setEntity(Vec2i pos, Terrain terrain) {
        removeEntity(pos);
        terrains[pos.getY()][pos.getX()] = terrain;
//        moveEntityTo(pos, terrain);
    }


    /**
     * Sets a Terrain entity onto the Level and also updates it on the Game view
     * @param pos : position on the Level
     * @param terrain : Terrain entity
     */
    @Override
    public void addEntity(Vec2i pos, Terrain terrain) {
        setEntity(pos, terrain);
        addEntityView(terrain);
    }

    private void addEntityView(Entity entity) {
        view.addNode(entity.getView());
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
            removeEntityView(terrain);

        return terrain;
    }


    /**
     * Overloaded method: allows a flag to set a Ground tile at the removed Terrain entity
     * @param pos : position on the Level
     * @param replaceWithDefault : flag to replace the removed Terrain with a Ground tile
     * @return The Terrain entity just removed
     */
    public Terrain removeEntity(Vec2i pos, boolean replaceWithDefault) {
        Terrain terrain = removeTerrain(pos);

        if (replaceWithDefault)
            addEntity(pos, DEFAULT_TERRAIN.get());

        return terrain;
    }

    /**
     * Removes the graphical component of an entity from the Game View
     * @param entity : the entity to be removed
     */
    private void removeEntityView(Entity entity) {
        view.removeNode(entity.getView());
//        entity.onRemovedFromLevel(); TODO should be handled separately
    }



//    /**
//     * Notifies the Terrain entity when a Prop moves on top of it
//     * @param pos : position of Terrain entity
//     * @param prop : Prop entity which was moved
//     */
//    private void notifyOnEnterByProp(Vec2i pos, Prop prop) {
//        if (hasEntity(pos)) getEntity(pos).onEnterByProp(prop);
//    }
//
//    /**
//     * Notifies the Terrain entity when an Enemy moves on top of it
//     * @param pos : position of the Terrain entity
//     * @param enemy : Enemy which was moved
//     */
//    private void notifyOnEnterByEnemy(Vec2i pos, Enemy enemy) {
//        if (hasEntity(pos)) getEntity(pos).onEnterByEnemy(prop);
//    }

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
     * Checks if you can place a Terrain entity at a certain position, without
     * violating stacking rules if a current Terrain entity is removed
     * @param pos : position to check
     * @param terrain : Terrain to be placed
     * @return True if you can place it, false otherwise
     */
    public boolean canReplaceTerrain(Vec2i pos, Terrain terrain) {
        if (hasProp(pos)) {
            if (!terrain.isStackableForProp(getProp(pos))) return false;
        }

        if (hasPickup(pos)) {
            if (!terrain.isStackableForPickup(getPickup(pos))) return false;
        }

        if (hasEnemy(pos)) {
            if(!terrain.isStackableForEnemy(getEnemy(pos))) {
                return false;
            }
        }

        if (hasAvatar(pos)) {
            if (!terrain.isStackableForAvatar(getAvatar())) return false;
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
            if (!prop.isStackableForPickup(getPickup(pos))) return false;
        }

        if (hasEnemy(pos)) {
            if (!prop.isStackableForEnemy(getEnemy(pos))) return false;
        }

        if (hasAvatar(pos)) {
            if (!prop.isStackableForAvatar(getAvatar())) return false;
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
            if (!pickup.isStackableForEnemy(getEnemy(pos))) return false;
        }

        if (hasAvatar(pos)) {
            if (!pickup.isStackableForAvatar(getAvatar())) return false;
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




    /**
     * Getter for the View of the Level
     * @return the View of the Level
     */
    public Node getView() {
        return view.getView();
    }


    /**
     * Resizes the # of rows and cols of the Level
     * @param newNRow : new # of rows for the Level
     * @param newNCol : new # of cols for the Level
     */
//    public void resize(int newNRow, int newNCol) {
//        Vec2i newDim = new Vec2i(newNRow, newNCol);
//        if (!newDim.within(new Vec2i(4, 4), new Vec2i(64, 64))) {
//            System.out.println("Error: map size must be between 4x4 and 64x64");
//            return;
//        }
//
//        Terrain[][] resizedTerrain = new Terrain[newNRow][newNCol];
//        HashMap<Vec2i, Prop> resizedProps = new HashMap<>();
//        HashMap<Vec2i, Enemy> resizedEnemies = new HashMap<>();
//        HashMap<Vec2i, Pickup> resizedPickups = new HashMap<>();
//
//        int copyNRow = (nRows < newNRow) ? nRows : newNRow;
//        int copyNCol = (nCols < newNCol) ? nCols : newNCol;
//
//        Vec2i min = new Vec2i(0, 0);
//        Vec2i max = new Vec2i(newNCol - 1, newNRow - 1);
//
//        for (int i = 0; i < nRows; i++) {
//            for (int j = 0; j < nCols; j++) {
//                Vec2i pos = new Vec2i(j, i);
//
//                if (!pos.within(min, max)) removeAllAt(pos,  true);
//            }
//        }
//
//        for (int i = 0; i < copyNRow; i++) {
//            for (int j = 0; j < copyNCol; j++) {
//                Vec2i pos = new Vec2i(j, i);
//
//                resizedTerrain[i][j] = terrains[i][j];
//                if (enemies.containsKey(pos)) resizedEnemies.put(pos, enemies.get(pos));
//                if (props.containsKey(pos)) resizedProps.put(pos, props.get(pos));
//                if (pickups.containsKey(pos)) resizedPickups.put(pos, pickups.get(pos));
//            }
//        }
//
//        this.nRows = newNRow;
//        this.nCols = newNCol;
//
//        this.terrains = resizedTerrain;
//        this.props = resizedProps;
//        this.enemies = resizedEnemies;
//        this.pickups = resizedPickups;
//
//        for (int i = 0; i < newNRow; i++) {
//            for (int j = 0; j < newNCol; j++) {
//                Vec2i pos = new Vec2i(j, i);
//                Terrain ground = new Ground(this);
//                if (!hasTerrain(pos)) addTerrain(pos, ground);
//            }
//        }
//    }


    /**
     * Gets an iterator for the Terrain entities of the Level
     * @return an iterator of the Terrain entities
     */
    public Iterator<Terrain> getTerrainIterator() {
        return new Array2DIterator<>(terrains);
    }


    /**
     * Gets an iterator for the entities at a certain position
     * @param pos : the position to find entities
     * @return an iterator of the entities at the position
     */
//    public Iterator<Entity> getEntitiesAt(Vec2i pos) {
//        ArrayList<Entity> entities = new ArrayList<>();
//        if (hasTerrain(pos))    entities.add(getTerrain(pos));
//        if (hasProp(pos))       entities.add(getProp(pos));
//        if (hasPickup(pos))     entities.add(getPickup(pos));
//        if (hasEnemy(pos))      entities.add(getEnemy(pos));
//        if (hasAvatar(pos))     entities.add(avatar);
//
//        return entities.iterator();
//    }
}
