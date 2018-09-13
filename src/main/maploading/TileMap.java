package main.maploading;

import java.util.ArrayList;

public class TileMap {

    private int nRows;
    private int nCols;

    private Tile[][] tiles;
    private ArrayList<String> objectives;

    public int getNRows() {
        return nRows;
    }

    public int getNCols() {
        return nCols;
    }

}
