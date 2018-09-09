package main.maploading;

import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import main.Entities.Entity;
import main.core.ViewComponent;
import main.math.Vec2d;
import main.math.Vec2i;

import java.util.ArrayList;

public class TileMap{


    private double size;
    private int nRows = 0;
    private int nCols = 0;

    private Tile[][] tiles;
    private ArrayList<String> objectives;

    private ViewComponent viewComponent;

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

                int r = (i + j) * 255 / (nCols + nRows);
                int g = j * 255 / nCols;
                int b = i * 255 / nRows;
                tile.setFill(Color.rgb(r, g, b));
                gridView.add(tile, i, j);
            }
        }

        gridView.gridLinesVisibleProperty().setValue(true);
        viewComponent = new ViewComponent(gridView);
    }

    public double getHeight() {
        return size * nRows;
    }

    public double getWidth() {
        return size * nCols;
    }

    public void setTile(int row, int col, ArrayList<Entity> entities) {
        tiles[row][col].setEntities(entities);
    }

    public Node getView() {
        return viewComponent.getView();
    }

    public int getNRows() {
        return nRows;
    }

    public int getNCols() {
        return nCols;
    }

    public Vec2d gridPosToWorldPos(Vec2i gridPos) {
        return new Vec2d((gridPos.getX() + 0.5) * size, (gridPos.getY() + 0.5) * size);
    }

    public void setObj(ArrayList<String> obj) {
        this.objectives = obj;
    }

    public ArrayList<String> getObjectives() {
        return objectives;
    }

    public Tile getTile(Vec2i coord) {
        int row = coord.getX(), col = coord.getY();
        return tiles[row][col];
    }

}
