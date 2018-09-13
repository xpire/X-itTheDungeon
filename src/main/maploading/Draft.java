package main.maploading;

import main.entities.Key;
import main.mapsaving.MapSaver;
import main.math.Vec2i;

import java.util.ArrayList;

public class Draft {

    private int nRows;
    private int nCols;

    private Tile[][] tiles;
    private ArrayList<String> objectives;

    public Draft() {
        this(8, 8);
    }

    public Draft(int nRows, int nCols) {
        this.nRows = nRows;
        this.nCols = nCols;
        this.objectives = new ArrayList<>();
    }

    public Draft CreateDraft(String draftName) {
        Draft draft = new Draft(8, 8);

        MapSaver mapSaver = new MapSaver();
        mapSaver.saveMap(draft, draftName);

        return draft;
    }

    public int getNRows() {
        return nRows;
    }

    public int getNCols() {
        return nCols;
    }

    public ArrayList<String> getObjectives() {

        return null;
    }

    public ArrayList<Key> findKeys() {
        ArrayList<Key> keys = new ArrayList<>();



        return keys;
    }

    public Tile getTile(Vec2i pos) {


        return null;
    }

    public void editTile(Vec2i tile, char[] entities) {

    }

    public void resizeDraft(int newRow, int newCol) {

    }

    public void setObjectives(ArrayList<String> obj) {

    }

    public void displayDraft() {

    }

    public static void main(String[] args) {



    }

    public void editorInterpreter() {

    }
}
