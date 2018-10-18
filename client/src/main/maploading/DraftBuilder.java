package main.maploading;

import main.Level;
import main.entities.Entity;
import main.math.Vec2i;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

/**
 * Class which contains the logic of the CreateMode class
 * Talks to the level class for information
 * Also implements draft saving, deleting, editing
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

    /**
     * Checks if a coordinate is a valid tile on the draft
     * @param tile
     * @return
     */
    public boolean isValidGridPos(Vec2i tile) {
        return level.isValidGridPos(tile);
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
        StringBuilder mapPath = new StringBuilder("./src/main/");
        mapPath.append(path).append("/").append(mapName).append(".txt");

        BufferedWriter w = null;
        try {

            //beautiful use of Decorator Pattern
            w = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(
                                    new File(mapPath.toString()))));

            int nRow = getNRows();
            int nCol = getNCols();

            //set the map size
            w.write(nRow + "\t" + nCol);
            w.append("\n");

//            //set the objective
//            ArrayList<String> obj = level.getObjectives();
//            for (String s : obj) {
//                w.write(s + "\t");
//            }
//            w.newLine();

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

    //TODO: this method is now redundant - only used in terminal creative mode
    //TODO: remove before submission
    /**
     * Adds Entities to a certain position on the Level
     *
     * Uses the LevelBuilderContext class
     * If a key/door is added, user will be prompted to give the
     * coordinates of the matching door/key
     *
     * @param tile position to add entities to
     * @param entities String of entities to add (in symbol format)
     * @param sc instance of a scanner if they wish to add a key/door
     */
    public void editTile(Vec2i tile, String entities, Scanner sc) { //TODO remove need for scanner
        LevelBuilder levelBuilder = new LevelBuilder(level);

        char[] eachEnt = entities.toCharArray();
        for (char ent : eachEnt) {
            try {
                if (ent != 'K' && ent != '|') {
                    levelBuilder.makeAndAttach(tile, ent);

                } else {//// if (ent == 'K') {

                    String[] matching = null;
                    Vec2i matchingPos = null;

                    while (matching == null) {
                        System.out.println("Please set the matching Key/Door");
                        if (sc.hasNextLine()) {
                            matching = sc.nextLine().split("\\s+");
                            if (matching.length != 2) {
                                System.out.println("Error: must be a tile location");
                                matching = null;
                                continue;
                            }

                            matchingPos = new Vec2i(Integer.parseInt(matching[0]),
                                    Integer.parseInt(matching[1]));

                            if (!level.isValidGridPos(matchingPos) || matchingPos.equals(tile)) {
                                System.out.println("Error: invalid tile (note: matching tile must be distinct)");
                                matching = null;
                            }
                        }
                    }

                    if (ent == 'K') {
                        levelBuilder.addKeyDoor(tile, matchingPos);
                    } else {
                        levelBuilder.addKeyDoor(matchingPos, tile);
                    }
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void editTileGUI(Vec2i tile, String entity) {
        LevelBuilder levelBuilder = new LevelBuilder(level);

        try {
            levelBuilder.makeAndAttach(tile, entity.charAt(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void editTileKeyDoorGUI(Vec2i key, Vec2i door) {
        LevelBuilder levelBuilder = new LevelBuilder(level);

        try {
            levelBuilder.addKeyDoor(key, door);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    /**
     * Deletes the currently open draft and exists Create mode
     * @param draftName name of the draft
     * @return true if draft was deleted, false otherwise
     */
    public boolean deleteDraft(String draftName) {
        String draftPath = String.format("./src/main/drafts/%s.txt", draftName);
        File draft = new File(draftPath);

        return draft.delete();
    }

    public Level getLevel() {
        return level;
    }

    public void eraseEntitiesAt(Vec2i pos) {
        level.removeAllAt(pos, true);
    }
}