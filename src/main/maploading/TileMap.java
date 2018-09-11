package main.maploading;

import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import main.component.ViewComponent;
import main.entities.Entity;
import main.math.Vec2d;
import main.math.Vec2i;

import java.util.ArrayList;
import java.util.Iterator;
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
        return tiles[pos.getY()][pos.getX()];
    }

    public Iterator<Entity> getEntities(Vec2i pos) {
        return getTile(pos).getEntities();
    }

    public void addEntity(int row, int col, Entity entity) {
        entity.moveTo(col, row);
        view.addNode(entity.getView());
        tiles[row][col].addEntity(entity);
    }

    public void addNewEntity(int row, int col, Entity entity) {
        entity.setGridToWorld(gridToWorld);
        entity.moveTo(col, row);
        view.addNode(entity.getView());
        tiles[row][col].addEntity(entity);
    }


    public void setTile(int row, int col, ArrayList<Entity> entities) {
        for (Entity e : entities) {
            addNewEntity(row, col, e);
        }
    }


    public boolean isValidGridPos(Vec2i pos) {
        if (!pos.withinX(0, getNCols() - 1)) return false;
        if (!pos.withinY(0, getNRows() - 1)) return false;
        return true;
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
}
