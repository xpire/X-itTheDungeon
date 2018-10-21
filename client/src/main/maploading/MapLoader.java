package main.maploading;

import main.Level;
import main.math.Vec2i;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * A Class to load in maps from txt files which can then be used in Play Mode and Create Mode
 */
public class MapLoader {

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

    /**
     * Overloaded method setting createMode to flase
     * @param mapName : name of the map
     * @param path : path to the map
     * @return : the loaded level
     */
    public Level loadLevel(String mapName, String path) {
        return loadLevel(mapName, path, false);
    }


    /**
     * Initialises a Level with given dimensions
     * @param sc : Scanner to read
     * @param mapName : name of the map
     * @param isCreateMode : if the map is being loaded for the creative mode
     * @throws InvalidMapException : No dimensions set
     */
    private void initLevelWithDimensions(Scanner sc, String mapName, boolean isCreateMode) throws InvalidMapException {

        if (!sc.hasNextLine()) throw new InvalidMapException("Empty Map");

        String[] line = readLine(sc);
        if (line.length != 2) throw new InvalidMapException("Bad map size");

        int nRow = Integer.parseInt(line[0]);
        int nCol = Integer.parseInt(line[1]);

        builder = new LevelBuilder(nRow, nCol, 30, mapName, isCreateMode);
    }


    /**
     * Sets the objectives of the level
     * @param sc : scanner to read to file
     * @throws InvalidMapException : No objectives set
     */
    private void setObjectives(Scanner sc) throws InvalidMapException {

        if (!sc.hasNextLine()) throw new InvalidMapException("No objective");

        String[] line = readLine(sc);
        if (line.length == 0 || (line.length == 1) && line[0].length() == 0) {
            System.out.println("Warning: No objective specified");
            return;
        }

        builder.setObjectives(new ArrayList<>(Arrays.asList(line)));
    }


    /**
     * Adds entities to the level
     * @param sc : scanner to read the file
     * @throws InvalidMapException : bad entities found
     */
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

    /**
     * Adds keys and doors to the map
     * @param sc : scanner to read the file
     * @throws InvalidMapException : key door mapping is invalid
     */
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

    /**
     * Read a line fromm the txt file
     * @param sc : scanner to read the file
     * @return the line read in from the file, split by whitespace
     */
    private String[] readLine(Scanner sc) {
        return sc.nextLine().split("\\s+");
    }

}
