package MapLoading;

import java.util.ArrayList;

public class TileMap {

    private Tile[][] tiles;

    public TileMap(int nRows, int nCols) {
        this.tiles = new Tile[nRows][nCols];
    }

    public void setTile(int row, int col, ArrayList<Entity> entities) {
        tiles[row][col].setEntities(entities);
    }

}
