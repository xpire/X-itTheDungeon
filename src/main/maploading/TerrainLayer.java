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

public class TerrainLayer extends EntityLayer<Terrain> {

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
    public TerrainLayer(int nRows, int nCols, double size) {
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
     * Overloaded method: allows a flag to set a Ground tile at the removed Terrain entity
     * @param pos : position on the Level
     * @param replaceWithDefault : flag to replace the removed Terrain with a Ground tile
     * @return The Terrain entity just removed
     */
    public Terrain removeEntity(Vec2i pos, boolean replaceWithDefault) {
        Terrain terrain = terrains[pos.getY()][pos.getX()];
        terrains[pos.getY()][pos.getX()] = null;

        if (terrain != null)
            removeEntityView(terrain);

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


    /**
     * Checks if there is a Terrain entity at a given position
     * @param pos : position to check
     * @return True if the Terrain exists, False otherwise
     */
    public boolean hasEntity(Vec2i pos) {
        return getEntity(pos) != null;
    }


    /**
     * Gets an iterator for the Terrain entities of the Level
     * @return an iterator of the Terrain entities
     */
    @Override
    public Iterator<Terrain> iterator() {
        return new Array2DIterator<>(terrains);
    }


//    /**
//     * Resizes the # of rows and cols of the Level
//     * @param newNRows : new # of rows for the Level
//     * @param newNCols : new # of cols for the Level
//     */
//    public void resize(int newNRows, int newNCols) {
//        Terrain[][] newTerrains = new Terrain[newNRows][newNCols];
//
//        int copyNRow = Math.min(nRows, newNRows);
//        int copyNCol = Math.min(nCols, newNCols);
//
//        Vec2i min = new Vec2i(0, 0);
//        Vec2i max = new Vec2i(newNCols - 1, newNRows - 1);
//
//        for (int i = 0; i < nRows; i++) {
//            for (int j = 0; j < nCols; j++) {
//                Vec2i pos = new Vec2i(j, i);
//
//                if (!pos.within(min, max))
//                    removeEntity(pos,  false);
//            }
//        }
//
//        for (int i = 0; i < copyNRow; i++) {
//            for (int j = 0; j < copyNCol; j++) {
//                newTerrains[i][j] = terrains[i][j];
//            }
//        }
//
//        nRows = newNRows;
//        nCols = newNCols;
//        terrains = newTerrains;
//
//        for (int i = 0; i < newNRows; i++) {
//            for (int j = 0; j < newNCols; j++) {
//                Vec2i pos = new Vec2i(j, i);
//                if (!hasTerrain(pos))
//                    addEntity(pos, DEFAULT_TERRAIN.get());
//            }
//        }
//    }



}
