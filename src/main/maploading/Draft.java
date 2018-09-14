package main.maploading;

import main.entities.Key;
import main.mapsaving.MapSaver;
import main.math.Vec2i;

import java.util.ArrayList;

public class Draft {

    private TileMap tileMap;
    private String draftName;

    public Draft(String draftName) {
        this(8, 8, draftName);
    }

    public Draft(int nRows, int nCols, String draftName) {
        tileMap = new TileMap(nRows, nCols, 10.0);
        this.draftName = draftName;
    }

    public int getNRows() {
        return tileMap.getNRows();
    }

    public int getNCols() {
        return tileMap.getNCols();
    }

    public String getDraftName() {
        return draftName;
    }

    public ArrayList<String> getObjectives(){
        return tileMap.getObjectives();
    }

    public void setObj(ArrayList<String> obj) {
        tileMap.setObj(obj);
    }

    public Tile getTile(Vec2i pos) {
        return tileMap.getTile(pos);
    }

    public void displayTileMap() {
        tileMap.displayTileMap();
    }

    public ArrayList<Key> findKeys() {
        return tileMap.findKeys();
    }

    public void resize(int newNRow, int newNCol) {
        tileMap.resize(newNRow, newNCol);
    }

//    move the MapSaver class into here
//    public void saveDraft() {
//
//    }

    public void editTile(Vec2i tile, String entities) {
        Tile t = tileMap.getTile(tile);
        MapInterpreter mapInterpreter = new MapInterpreter();

        t.setEntities(mapInterpreter.getTileEntities(entities));
    }

    public static void main(String[] args) {
        Draft d = new Draft("draft1");

        //this needs to be updated
        MapSaver mapSaver = new MapSaver();
        mapSaver.saveMap(d, d.getDraftName());

        d.editTile(new Vec2i(2,3), "*");
        d.editTile(new Vec2i(4, 5), "*");

        //tile override test
        d.editTile(new Vec2i(2, 3), "O");

        //test multiple entities
        d.editTile(new Vec2i(0, 6), "K/1");
        d.displayTileMap();

        //test resize
        d.resize(5, 5);
        d.displayTileMap();

        d.resize(10, 10);
        d.displayTileMap();

        d.resize(3, 3);
        d.resize(3, 6);
        d.resize(68, 1);

        //test placing empty entity
        System.out.println();
        d.editTile(new Vec2i(3, 3), "*");
        d.displayTileMap();
        System.out.println();
        d.editTile(new Vec2i(3, 3), "");
        d.displayTileMap();
    }

    public void editorInterpreter() {

    }
}
