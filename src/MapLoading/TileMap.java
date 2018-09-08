package MapLoading;

import java.util.ArrayList;

public class TileMap {

    private Tile[][] tiles;
    private ArrayList<String> objectives;


    public TileMap(int nRows, int nCols) {
        this.tiles = new Tile[nRows][nCols];
        this.objectives = new ArrayList<>();
    }

    public ArrayList<String> getObjectives() {
        return objectives;
    }

    public void setObj(ArrayList<String> obj) {
        this.objectives = obj;
    }

    public void setTile(int row, int col, ArrayList<Entity> entities) {
        tiles[row][col].setEntities(entities);
    }

    public void assignDoor(int[] keyCoord, int[] doorCoord) {
        getTile(keyCoord).setDoor(doorCoord);
    }

    public Tile getTile(int[] coord) {
        int row = coord[0], col = coord[1];
        return tiles[row][col];
    }
}
