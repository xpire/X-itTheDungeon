package main.maploading;

import javafx.scene.Node;
import main.entities.Entity;
import main.math.Vec2d;
import main.math.Vec2i;

import java.util.ArrayList;
import java.util.Iterator;

public class Level {

    private TileMap tileMap;


    public Level(Vec2i dim) {
        this(dim.getX(), dim.getY());
    }

    public Level(int nRows, int nCols) {
        this(nRows, nCols, 10.0);
    }

    public Level(int nRows, int nCols, double size) {
        this.tileMap = new TileMap(nRows, nCols, size);
    }


    /*
    Tile Property
     */
    public boolean isPassable(Vec2i pos) {
        return getTile(pos).isPassable();
    }



    /*
    Tile Access
     */

    public Tile getTile(Vec2i pos) {
        return tileMap.getTile(pos);
    }

    public Iterator<Entity> getEntities(Vec2i pos) {
        return tileMap.getEntities(pos);
    }

    public void addEntity(int x, int y, Entity entity) {
        tileMap.addEntity(x, y, entity);
    }

    public void addNewEntity(int x, int y, Entity entity) {
        tileMap.addNewEntity(x, y, entity);
    }


    public void setTile(int x, int y, ArrayList<Entity> entities) {
        tileMap.setTile(x, y, entities);
    }


    public boolean isValidGridPos(Vec2i pos) {
        return tileMap.isValidGridPos(pos);
    }

    public void displayTileMap() {
        tileMap.displayTileMap();
    }


    /*
    Objectives
     */

    public void setObj(ArrayList<String> obj) {
        tileMap.setObj(obj);
    }

    public ArrayList<String> getObjectives() {
        return tileMap.getObjectives();
    }


    /*
    Grid to World Position
     */

    public Vec2d gridPosToWorldPos(Vec2i gridPos) {
        return tileMap.gridPosToWorldPos(gridPos);
    }


    public Vec2d gridPosToWorldPosCentre(Vec2i gridPos) {
        return tileMap.gridPosToWorldPosCentre(gridPos);
    }


    /*
    Dimensions and View
     */
    public int getNRows() {
        return tileMap.getNRows();
    }

    public int getNCols() {
        return tileMap.getNCols();
    }

    public double getHeight() {
        return tileMap.getHeight();
    }

    public double getWidth() {
        return tileMap.getWidth();
    }

    public Node getView() {
        return tileMap.getView();
    }

    public void moveTo(Entity e, Vec2i from, Vec2i to) {
        Tile curr = getTile(from);
        if (!curr.removeEntity(e)) return;

        Tile next = getTile(to);
        next.addEntity(e);
    }

    public void moveTo(Entity e, int x, int y) {
        tileMap.moveTo(e, x, y);
    }

    public boolean isPassableFor(Entity e, Vec2i target) {

        Iterator<Entity> it = getEntities(target);
        while (it.hasNext()) {

            Entity tileEntity = it.next();

            if (!tileEntity.isPassableFor(e)) {
                return false;
            }
        }

        return true;
    }

    public void removeEntity(Entity entity) {
        tileMap.removeEntity(entity);
    }
}
