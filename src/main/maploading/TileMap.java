package main.maploading;

import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import main.core.Entity;
import main.core.ViewComponent;
import main.math.Vec2f;

import java.util.ArrayList;

public class TileMap extends Entity{


    private int nRows = 0;
    private int nCols = 0;

    private Tile[][] tiles;
    private ArrayList<String> objectives;

    private ViewComponent viewComponent;

    public TileMap(int nRows, int nCols) {
        super("TileMap");

        this.tiles = new Tile[nRows][nCols];


        this.nRows = nRows;
        this.nCols = nCols;

        GridPane gridView = new GridPane();
        gridView.setMinSize(400, 400);

        for (int i = 0; i < nRows; i++) {
            for (int j = 0; j < nCols; j++) {
                tiles[i][j] = new Tile();

                Rectangle tile = new Rectangle(20, 20);

                int r = (i + j) * 255 / (nCols + nRows);
                int g = j * 255 / nCols;
                int b = i * 255 / nRows;
                tile.setFill(Color.rgb(r, g, b));
                gridView.add(tile, i, j);
            }
        }

        viewComponent = new ViewComponent(gridView);
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

    public void setObj(ArrayList<String> obj) {
        this.objectives = obj;
    }

    public ArrayList<String> getObjectives() {
        return objectives;
    }

    public void assignDoor(int[] keyCoord, int[] doorCoord) {
        getTile(keyCoord).setDoor(doorCoord);
    }

    public Tile getTile(int[] coord) {
        int row = coord[0], col = coord[1];
        return tiles[row][col];
    }

    public Vec2f tileCentreToWorld(int r, int c){
        return new Vec2f(r, c);
    }
}
