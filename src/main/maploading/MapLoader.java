package main.maploading;

import main.Level;
import main.entities.Entity;
import main.math.Vec2i;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;

/**
 * A Class to load in maps from txt files which can then be used in Play Mode and Create Mode
 */
public class MapLoader {

    // TODO: assetLoader, asset to path mapping

    /**
     * Loads a Level in from a .txt file.
     * First gets the dimensions of the map and initialises the Level
     * Then loads in the body of the Level, using the LevelBuilder to map symbols to
     * entities
     * Then sets the Level's objectives
     * Finally sets the Key-Door Mapping within the Level
     *
     * @param mapName name of the Level to load in
     * @param path Path where the Level is saved, root at main
     * @return The Level which was just loaded
     */
    public Level loadLevel(String mapName, String path, boolean isCreateMode) {

        Level level;
        LevelBuilder levelBuilder = new LevelBuilder(); //TODO shouldn't it be named entityBuilder

        String mapPath = String.format("./src/main/%s/%s.txt", path, mapName);

        try (Scanner sc = new Scanner(new File(mapPath))) {
            level = initLevelWithDimensions(sc, mapName, isCreateMode);
            setObjectives(sc, level);
            addEntities(sc, level, levelBuilder);
            addKeysAndDoors(sc, level, levelBuilder);

        } catch (FileNotFoundException | NumberFormatException | InvalidMapException e) {
            System.out.println("MapLoader Error: " + e.getMessage());
            return null;
        }

        return level;
    }


    public Level loadLevel(String mapName, String path) {
        return loadLevel(mapName, path, false);
    }


    // Extract dimensions, initialise the level
    private Level initLevelWithDimensions(Scanner sc, String mapName, boolean isCreateMode) throws InvalidMapException {

        if (!sc.hasNextLine()) throw new InvalidMapException("Empty Map");

        String[] line = readLine(sc);
        if (line.length != 2) throw new InvalidMapException("Bad map size");

        int nRow = Integer.parseInt(line[0]);
        int nCol = Integer.parseInt(line[1]);

        // TODO: fixed size 30.0
        return new Level(nRow, nCol, 30.0, mapName, isCreateMode);
    }


    // Extract objectives
    private void setObjectives(Scanner sc, Level level) throws InvalidMapException {

        if (!sc.hasNextLine()) throw new InvalidMapException("No objectives");

        String[] line = readLine(sc);
        if (line.length == 0) System.out.println("Warning: No objectives specified");

        ArrayList<String> objectives = new ArrayList<>(Arrays.asList(line));
        level.setObjectives(objectives);
    }

    // Extract grid entities, skip keys and doors for later
    private void addEntities(Scanner sc, Level level, LevelBuilder builder) throws InvalidMapException {
        String[] line;

        for (int i = 0; i < level.getNRows(); i++) {
            if (!sc.hasNextLine()) throw new InvalidMapException("Incorrect number of rows");

            line = readLine(sc);

            if (line.length != level.getNCols()) throw new InvalidMapException("Incorrect number of columns");

            for (int j = 0; j < level.getNCols(); j++) {
                for (char entity : line[j].toCharArray()) {
                    builder.addEntity(entity, new Vec2i(j, i), level);
                }
            }
        }
    }

    // Extract keys and doors
    private void addKeysAndDoors(Scanner sc, Level level, LevelBuilder levelBuilder) throws InvalidMapException {
        String[] line;

        while (sc.hasNextLine()) {

            line = readLine(sc);
            if (line.length == 0) break;
            if (line.length != 4) throw new InvalidMapException("Invalid Key-Door mapping");

            Vec2i keyPos = new Vec2i(
                    Integer.parseInt(line[0]),
                    Integer.parseInt(line[1]));

            Vec2i doorPos = new Vec2i(
                    Integer.parseInt(line[2]),
                    Integer.parseInt(line[3]));

            if (!level.isValidGridPos(keyPos) || !level.isValidGridPos(doorPos))
                throw new InvalidMapException("Invalid Key/Door Position");

            levelBuilder.buildKeyDoor(level, keyPos, doorPos);
        }
    }


    private String[] readLine(Scanner sc) {
        return sc.nextLine().split("\\s+");
    }






    public static void main(String[] args) {
        MapLoader mapLoader = new MapLoader();

        Level level = mapLoader.loadLevel("map1", "levels", false);

        level.displayLevel();


        //testing meta data
        for (int i = 0; i < level.getNRows(); i++) {
            for (int j = 0; j < level.getNCols(); j++) {
                Iterator<Entity> it = level.getEntitiesAt(new Vec2i(j, i));
                while (it.hasNext()) {
                    Entity e = it.next();

                    if (e.getMetaData() != null) {
                        System.out.println(e.getMetaData());
                    }

                }
            }
        }
    }
}
