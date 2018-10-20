package main.maploading;

import javafx.scene.Node;
import main.Level;
import main.content.ObjectiveFactory;
import main.entities.Entity;
import main.math.Vec2i;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

import static main.content.ObjectiveFactory.makeObjective;

/**
 * Class which contains the logic for building maps in the Creative Lab
 */
public class DraftBuilder {

    private Level level;

    /**
     * Constructor when loading in an existing draft
     * @param level : the existing draft
     */
    public DraftBuilder(Level level) {
        this.level = level;
    }

    /**
     * Constructor when creating a new draft
     * @param nRows # of rows for the Draft
     * @param nCols # of cols for the Draft
     * @param draftName name of the Draft
     */
    public DraftBuilder(int nRows, int nCols, String draftName) {
        level = new Level(nRows, nCols, 30.0, draftName, true);
    }

    /**
     * Gets the draft name
     * @return name of the draft
     */
    public String getName() {
        return level.getName();
    }

    /**
     * Displays the current state of the draft, in symbol format
     */
    public void displayLevel() {
        level.displayLevel();
    }

    public void setObjective(ArrayList<String> objectives) throws InvalidMapException {

        for (String objective : objectives) {
            try {
                level.addObjective(makeObjective(ObjectiveFactory.Type.valueOf(objective)));
            } catch (IllegalArgumentException e) {
                throw new InvalidMapException("Invalid Objective Code: " + objective);
            }
        }
    }

    /**
     * Resizes the dimensions of the draft
     * @param newNRow new # of rows
     * @param newNCol new # of cols
     */
    public void resize(int newNRow, int newNCol) {
        level.resize(newNRow, newNCol);
    }

    /**
     * Getter for the # of rows
     * @return the # of rows
     */
    public int getNRows() {
        return level.getNRows();
    }

    /**
     * Getter for the # of cols
     * @return the # of cols
     */
    public int getNCols() {
        return level.getNCols();
    }

    /**
     * Saves the current state of the draft to a .txt file which can
     * be later loaded by the MapLoader
     *
     * Opens a new txt file for writing,
     * Writes the dimensions
     * Writes the Levels body
     * Writes the objective
     * Writes the key-door mapping
     *
     * @param mapName name of the draft
     * @param path save location, root at main
     */
    public void saveMap(String mapName, String path) {
        String mapPath = String.format("./src/%s/%s.txt", path, mapName);

        BufferedWriter w = null;
        try {
            w = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(
                                    new File(mapPath))));

            int nRow = getNRows();
            int nCol = getNCols();

            //set the map size
            w.write(nRow + "\t" + nCol);
            w.append("\n");

            //setting objectives
            w.write(level.listObjectives());
            w.newLine();

            //set the main body of the map
            StringBuilder metaData = new StringBuilder();

            for (int i = 0; i < nRow; i++) {
                for (int j = 0; j < nCol; j++) {
                    Vec2i pos = new Vec2i(j, i);
                    StringBuilder sb = new StringBuilder();

                    Iterator<Entity> it = level.getEntitiesAt(pos);

                    while (it.hasNext()) {
                        Entity e = it.next();

                        sb.append(e.getSymbol());
                        if (e.getMetaData() != null)
                            metaData.append(e.getMetaData()).append("\n");
                    }

                    w.write(sb.toString() + "\t");
                }
                w.newLine();
            }

            //set the key-door mapping
            w.write(metaData.toString());

        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (w != null) w.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Adds a non key-door entity to the level at a specified position
     * @param tile the position to add the entity to
     * @param entity the entity to be added - cannot be a key/door
     */
    public void editTileGUI(Vec2i tile, String entity) {
        LevelBuilder levelBuilder = new LevelBuilder(level);

        try {
            levelBuilder.makeAndAttach(tile, entity.charAt(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a key/door pairing to the level at specified positions
     * @param key the position of the key
     * @param door the position of the door
     */
    public void editTileKeyDoorGUI(Vec2i key, Vec2i door) {
        LevelBuilder levelBuilder = new LevelBuilder(level);

        try {
            levelBuilder.addKeyDoor(key, door);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Level getLevel() {
        return level;
    }

    public void eraseEntitiesAt(Vec2i pos) {
        level.removeAllAt(pos, true);
    }

    public void clearObjectives() {
        level.clearObjectives();
    }

    public String listObjectives() {
        return level.listObjectives();
    }

    public Node getView() {
        return level.getView();
    }
}