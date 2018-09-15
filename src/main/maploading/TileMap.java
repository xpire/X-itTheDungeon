package main.maploading;

import main.entities.Entity;
import main.entities.Key;
import main.math.Vec2i;

import java.util.ArrayList;
import java.util.List;

public class TileMap {

    private int nRows;
    private int nCols;

    private Tile[][] tiles;
    private ArrayList<String> objectives;

    public TileMap(int nRows, int nCols) {
        this.nRows = nRows;
        this.nCols = nCols;
        this.objectives = new ArrayList<>();

        this.tiles = new Tile[nRows][nCols];

        for (int i = 0; i < nRows; i++) {
            for (int j = 0; j < nCols; j++) {
                tiles[i][j] = new Tile();
            }
        }
    }

    public int getNRows() {
        return nRows;
    }

    public int getNCols() {
        return nCols;
    }

    public ArrayList<String> getObjectives() {
        return objectives;
    }

    public void setObj(ArrayList<String> obj) {
        this.objectives = obj;
    }

    public void resize(int newNRow, int newNCol) {
        Vec2i newDim = new Vec2i(newNRow, newNCol);
        if (!newDim.within(new Vec2i(4, 4), new Vec2i(64, 64))) {
            System.out.println("Error: map size must be between 4x4 and 64x64");
            return;
        }

        Tile[][] resized = new Tile[newNRow][newNCol];
        for (int i = 0; i < newNRow; i++) {
            for (int j = 0; j < newNCol; j++) {
                resized[i][j] = new Tile();
            }
        }

        int copyNRow = (nRows < newNRow) ? nRows : newNRow;
        int copyNCol = (nCols < newNCol) ? nCols : newNCol;

        for (int i = 0; i < copyNRow; i++) {
            for (int j = 0; j < copyNCol; j++) {
                resized[i][j] = tiles[i][j];
            }
        }
        this.tiles = resized;
        this.nRows = newNRow;
        this.nCols = newNCol;
    }

    public void displayTileMap() {
        System.out.println(nRows + "\t" + nCols);
        for (int i = 0; i < nRows; i++) {
            for (int j = 0; j < nCols; j++) {
                Tile t = getTile(new Vec2i(j, i));

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

        System.out.print("Objectives are:");
        if (getObjectives().isEmpty())
            System.out.print(" no objectives set");
        for (String s : getObjectives()) {
            System.out.print(" " + s);
        }
        System.out.println();
    }

    public Tile getTile(Vec2i pos) {
        if (!isValidGridPos(pos)) return null;
        return tiles[pos.getY()][pos.getX()];
    }

    public boolean isValidGridPos(Vec2i pos) {
        if (!pos.withinX(0, getNCols() - 1)) return false;
        if (!pos.withinY(0, getNRows() - 1)) return false;
        return true;
    }

    public ArrayList<Key> findKeys() {
        ArrayList<Key> keys = new ArrayList<>();

        for (int i = 0; i < nRows; i++) {
            for (int j = 0; j < nCols; j++) {
                Key k = tiles[i][j].getKey();
                if (k != null) keys.add(k);
            }
        }

        return keys;
    }
}
