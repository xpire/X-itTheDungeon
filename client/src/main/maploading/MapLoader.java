package main.maploading;

import main.Level;
import main.entities.Entity;
import main.math.Vec2i;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * A Class to load in maps from txt files which can then be used in Play Mode and Create Mode
 */
public class MapLoader {

    // TODO: assetLoader, asset to path mapping

    private LevelBuilder builder;

    /**
     * Loads a Level in from a .txt file.
     * First gets the dimensions of the map and initialises the Level
     * Then loads in the body of the Level, using the LevelBuilderContext to map symbols to
     * entities
     * Then sets the Level's objective
     * Finally sets the Key-Door Mapping within the Level
     *
     * @param mapName name of the Level to load in
     * @param path    Path where the Level is saved, root at main
     * @return The Level which was just loaded
     */
    public Level loadLevel(String mapName, String path, boolean isCreateMode) {

        String mapPath = String.format("./%s/%s.txt", path, mapName);

        try (Scanner sc = new Scanner(new File(mapPath))) {
            initLevelWithDimensions(sc, mapName, isCreateMode);
            setObjectives(sc);
            addEntities(sc);
            addKeysAndDoors(sc);

        } catch (FileNotFoundException | NumberFormatException | InvalidMapException e) {
            System.out.println("MapLoader Error: " + e.getMessage());
            e.printStackTrace();
            return null;
        }

        return builder.getLevel();
    }


    public Level loadLevel(String mapName, String path) {
        return loadLevel(mapName, path, false);
    }


    // Extract dimensions, initialise the level
    private void initLevelWithDimensions(Scanner sc, String mapName, boolean isCreateMode) throws InvalidMapException {

        if (!sc.hasNextLine()) throw new InvalidMapException("Empty Map");

        String[] line = readLine(sc);
        if (line.length != 2) throw new InvalidMapException("Bad map size");

        int nRow = Integer.parseInt(line[0]);
        int nCol = Integer.parseInt(line[1]);

        // TODO: fixed size 30.0
        builder = new LevelBuilder(nRow, nCol, 30, mapName, isCreateMode);
    }


    // Extract objective
    private void setObjectives(Scanner sc) throws InvalidMapException {

        if (!sc.hasNextLine()) throw new InvalidMapException("No objective");

        String[] line = readLine(sc);
        if (line.length == 0 || (line.length == 1) && line[0].length() == 0) {
            System.out.println("Warning: No objective specified");
            return;
        }

        builder.setObjectives(new ArrayList<>(Arrays.asList(line)));
    }


    // Extract grid entities, skip keys and doors for later
    private void addEntities(Scanner sc) throws InvalidMapException {
        String[] line;

        for (int i = 0; i < builder.getNRows(); i++) {
            if (!sc.hasNextLine()) throw new InvalidMapException("Incorrect number of rows");

            line = readLine(sc);

            if (line.length != builder.getNCols()) throw new InvalidMapException("Incorrect number of columns");

            for (int j = 0; j < builder.getNCols(); j++) {
                for (char entity : line[j].toCharArray()) {
                    builder.makeAndAttach(new Vec2i(j, i), entity);
                }
            }
        }
    }

    // Extract keys and doors
    private void addKeysAndDoors(Scanner sc) throws InvalidMapException {
        String[] line;

        while (sc.hasNextLine()) {

            line = readLine(sc);
            if (line.length == 0 || line.length == 1 && line[0].equals("")) break;
            if (line.length != 4) throw new InvalidMapException("Invalid Key-Door mapping");

            Vec2i keyPos = new Vec2i(
                    Integer.parseInt(line[0]),
                    Integer.parseInt(line[1]));

            Vec2i doorPos = new Vec2i(
                    Integer.parseInt(line[2]),
                    Integer.parseInt(line[3]));

            builder.addKeyDoor(keyPos, doorPos);
        }
    }


    private String[] readLine(Scanner sc) {
        return sc.nextLine().split("\\s+");
    }

}
