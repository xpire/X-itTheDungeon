package main.maploading;

import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import main.component.ViewComponent;
import main.entities.Entity;
import main.entities.Key;
import main.math.Vec2d;
import main.math.Vec2i;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

public class TileMap{


    private double size;
    private int nRows = 0;
    private int nCols = 0;

    private Tile[][] tiles;
    private ArrayList<String> objectives;

    private ViewComponent view;


    private Function<Vec2i, Vec2d> gridToWorld = gridPos -> gridPosToWorldPosCentre(gridPos);


    public TileMap(Vec2i dim) {
        this(dim.getX(), dim.getY());
    }


    public TileMap(int nRows, int nCols) {
        this(nRows, nCols, 10.0);
    }


    public TileMap(int nRows, int nCols, double size) {
        this.tiles = new Tile[nRows][nCols];

        this.size = size;
        this.nRows = nRows;
        this.nCols = nCols;


        GridPane gridView = new GridPane();
        gridView.setMinSize(getWidth(), getHeight());

        for (int i = 0; i < nRows; i++) {
            for (int j = 0; j < nCols; j++) {
                tiles[i][j] = new Tile();

                Rectangle tile = new Rectangle(size, size);
                tile.setFill(Color.rgb(200, 200, 200));

                gridView.add(tile, i, j);
            }
        }

        gridView.gridLinesVisibleProperty().setValue(true);
        view = new ViewComponent(gridView);
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
        if (!isValidGridPos(pos)) return null;

        return tiles[pos.getY()][pos.getX()];
    }

    public Iterator<Entity> getEntities(Vec2i pos) {
        return getTile(pos).getEntities().iterator();
    }

    public void addEntity(int x, int y, Entity entity) {
        entity.moveTo(x, y);
        view.addNode(entity.getView());
        tiles[y][x].addEntity(entity);
    }

    public void addNewEntity(int x, int y, Entity entity) {
        entity.setMap(this);
        addEntity(x, y, entity);
    }


    public void setTile(int x, int y, ArrayList<Entity> entities) {
        for (Entity e : entities) {
            addNewEntity(x, y, e);
        }
    }


    public boolean isValidGridPos(Vec2i pos) {
        if (!pos.withinX(0, getNCols() - 1)) return false;
        if (!pos.withinY(0, getNRows() - 1)) return false;
        return true;
    }

    public ArrayList<Key> findKeys() {
        ArrayList<Key> keys = new ArrayList<>();

        for (int y = 0; y < nRows; y++) {
            for (int x = 0; x < nCols; x++) {
                Key k = tiles[y][x].getKey();
                if (k != null) keys.add(k);
            }
        }

        return keys;
    }

    public void displayTileMap() {
        for (int i = 0; i < nRows; i++) {
            for (int j = 0; j < nCols; j++) {
                Vec2i pos = new Vec2i(i, j);
                Tile t = getTile(pos);
                StringBuilder sb = new StringBuilder();

                if (t.isEmpty()) sb.append('.');
                else {
                    List<Entity> ent = t.getEntities();
                    for (Entity e : ent) {
                        char ch = e.getSymbol();
                        sb.append(ch);
                    }
                }
                System.out.print((sb.toString() + "\t"));
            }
            System.out.println();
        }
    }


    /*
    Objectives
     */

    public void setObj(ArrayList<String> obj) {
        this.objectives = obj;
    }


    public ArrayList<String> getObjectives() {
        return objectives;
    }



    /*
    Grid to World Position
     */

    public Vec2d gridPosToWorldPos(Vec2i gridPos) {
        return new Vec2d(gridPos.getX() * size, gridPos.getY() * size);
    }

    public Vec2d gridPosToWorldPosCentre(Vec2i gridPos) {
        return new Vec2d((gridPos.getX() + 0.5) * size, (gridPos.getY() + 0.5) * size);
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

    public void moveTo(Entity e, Vec2i from, Vec2i to) {
        Tile curr = getTile(from);
        if (!curr.removeEntity(e)) return;

        Tile next = getTile(to);
        next.addEntity(e);
    }

    public void moveTo(Entity e, int x, int y) {
        Tile curr = getTile(e.getGridPos());
        if (!curr.removeEntity(e)) return;

        Tile next = getTile(new Vec2i(x, y));
        next.addEntity(e);
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
        getTile(entity.getGridPos()).removeEntity(entity);
        view.removeNode(entity.getView());
    }
}
