package main.maploading;

import javafx.scene.Group;
import main.Level;
import main.content.ObjectiveFactory;
import main.math.Vec2i;

import java.io.File;
import java.util.ArrayList;

import static main.content.ObjectiveFactory.makeObjective;

/**
 * Class which contains the logic for building maps in the Creative Lab
 */
public class DraftBuilder {

    private Level level;

    /**
     * Constructor when loading in an existing drafts
     * @param level : the existing drafts
     */
    public DraftBuilder(Level level) {
        this.level = level;
    }

    /**
     * Constructor when creating a new drafts
     * @param nRows # of rows for the Draft
     * @param nCols # of cols for the Draft
     * @param draftName name of the Draft
     */
    public DraftBuilder(int nRows, int nCols, String draftName) {
        level = new Level(nRows, nCols, 30.0, draftName, true);
    }

    /**
     * Gets the drafts name
     * @return name of the drafts
     */
    public String getName() {
        return level.getName();
    }

    /**
     * Displays the current state of the drafts, in symbol format
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
     * Resizes the dimensions of the drafts
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

    /**
     * Getter for the associated level
     * @return the level
     */
    public Level getLevel() {
        return level;
    }

    /**
     * Remove all entities at a position
     * @param pos the position
     */
    public void eraseEntitiesAt(Vec2i pos) {
        level.removeAllAt(pos, true);
    }

    /**
     * Remove all set objectives
     */
    public void clearObjectives() {
        level.clearObjectives();
    }

    /**
     * List the current objectives in string form
     * @return
     */
    public String listObjectives() {
        return level.listObjectives();
    }

    /**
     * Getter for the view
     * @return the levels view
     */
    public Group getView() {
        return level.getView();
    }

    /**
     * Sets the name of the level
     * @param newName the new name
     */
    public void setName(String newName) {
        level.setName(newName);
    }

    /**
     * Saves a level as a file
     * @param mapName : name of the map
     * @param path : path to save to
     * @return the File saved
     */
    public File toFile(String mapName, String path) {
        return level.toFile(mapName, path);
    }

    /**
     * Checks if a level is trivially completable
     * @return true if all objectives are already done
     */
    public boolean isTrivialLevel() {
        return level.checkAchievedAllObjectives();
    }

    /**
     * checks if the level contains an exit
     * @return true if exit is found
     */
    public boolean containsExit() {
        return level.containsExit();
    }
}