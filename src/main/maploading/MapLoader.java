package main.maploading;

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
    public Level loadLevel(String mapName, String path) {
        LevelBuilder levelBuilder = new LevelBuilder();
        Level level = null;

        StringBuilder mapPath = new StringBuilder("./src/main/");
        mapPath.append(path).append("/").append(mapName).append(".txt");

        Scanner sc = null;

        try {
            sc = new Scanner(new File(mapPath.toString()));
            String[] line;
            int numRow, numCol;

            /*
                Extract dimensions, initialise the Level
             */

            if (sc.hasNextLine()) {
                line = sc.nextLine().split("\\s+");
                if (line.length != 2) {
                    System.out.println("Error: bad map size");
                    return null;
                }

                numRow = Integer.parseInt(line[0]);
                numCol = Integer.parseInt(line[1]);

                level = new Level(numRow, numCol, 30.0, mapName);
                //change flag when required

            } else {
                System.out.println("Error: Empty Map");
                return null;
            }

            /*
                Extract Level's body
                Skips Keys and Doors for later
             */

            for (int i = 0; i < numRow; i++) {
                if (sc.hasNextLine()) {
                    line = sc.nextLine().split("\\s+");
                    if (line.length != numCol) {
                        System.out.println("Error: Inconsistent Map");
                        return null;
                    }

                    for (int j = 0; j < numCol; j++) {

                        Vec2i pos = new Vec2i(j, i);

                        char[] entities = line[j].toCharArray();
                        for (char entity : entities) {
                            try {
                                levelBuilder.buildEntity(entity, pos, level);
                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                            }
                        }
                    }
                }
            }

            /*
                Extract objectives
             */

            if (sc.hasNextLine()) {
                line = sc.nextLine().split("\\s+");
                if (line.length > 0) {
                    ArrayList<String> objectives = new ArrayList<>(Arrays.asList(line));
                    level.setObjectives(objectives);
                } else {
                    System.out.println("Warning: No objectives specified");
                    //return null;
                }
            } else {
                System.out.println("Error: Bad map @ extract objectives");
                return null;
            }

            /*
                Extract Keys and Doors
             */

            while (sc.hasNextLine()) {
                line = sc.nextLine().split("\\s+");

                if (line.length == 0) break;
                else if (line.length != 4) {
                    System.out.println("Error: invalid Key-Door mapping");
                    return null;
                }

                Vec2i keyCoord = new Vec2i(
                        Integer.parseInt(line[0]),
                        Integer.parseInt(line[1]));
                Vec2i doorCoord = new Vec2i(
                        Integer.parseInt(line[2]),
                        Integer.parseInt(line[3]));

                if (!level.isValidGridPos(keyCoord) || !level.isValidGridPos(doorCoord)) {
                    System.out.println("Error: invalid Key/Door position");
                    return null;
                }

                try {
                    levelBuilder.buildKeyDoor(level, keyCoord, doorCoord);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } finally {
            if (sc != null) sc.close();
        }

        return level;
    }

    public static void main(String[] args) {
        MapLoader mapLoader = new MapLoader();

        Level level = mapLoader.loadLevel("map1", "levels");

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
