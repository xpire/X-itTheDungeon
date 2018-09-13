package main.maploading;

import main.entities.Door;
import main.entities.Key;
import main.math.Vec2i;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class MapLoader {

    /*
    Heavy commenting/refactoring required for others to understand
     */
    public Level getTileMap(String mapName) {
        MapInterpreter mapInt = new MapInterpreter();
        Level level = null;

        StringBuilder mapPath = new StringBuilder("./src/main/levels/");
        mapPath.append(mapName);

        Scanner sc = null;

        try {
            sc = new Scanner(new File(mapPath.toString()));
            String[] line;
            int numRow = 0, numCol = 0;

            if (sc.hasNextLine()) {
                line = sc.nextLine().split("\\s+");
                if (line.length != 2) {
                    System.out.println("Error: bad map size");
                    return null;
                }

                numRow = Integer.parseInt(line[0]);
                numCol = Integer.parseInt(line[1]);

                level = new Level(numRow, numCol);

            } else System.out.println("Error: Empty Map");

            if (level == null) {
                System.out.println("Error: Map did not load");
                return null;
            }

            for (int i = 0; i < numRow; i++) {
                line = sc.nextLine().split("\\s+");

                if (line.length != numCol) {
                    System.out.println("Error: Inconsistent Map");
                    return null;
                }

                for (int j = 0; j < numCol; j++) {
                    level.setTile(i, j, mapInt.getTileEntities(line[j]));
                }
            }

            if (sc.hasNextLine()) {
                line = sc.nextLine().split("\\s+");
                ArrayList<String> objectives = new ArrayList<>(Arrays.asList(line));
                level.setObj(objectives);
            } else System.out.println("Error: No objectives specified");

            while (sc.hasNextLine()) {
                line = sc.nextLine().split("\\s+");
                if (line.length != 4) {
                    System.out.println("Error: Key-Door mapping incorrect");
                    break;
                }

                Vec2i keyCoord = new Vec2i(
                        Integer.parseInt(line[0]),
                        Integer.parseInt(line[1]));
                Vec2i doorCoord = new Vec2i(
                        Integer.parseInt(line[2]),
                        Integer.parseInt(line[3]));

                Tile keyTile = level.getTile(keyCoord);
                Key k = keyTile.getKey();
                if (k == null) {
                    System.out.println("No key found at given coordinates");
                    continue;
                }

                Tile doorTile = level.getTile(doorCoord);
                Door d = doorTile.getDoor();
                if (d == null) {
                    System.out.println("No door found at given coordinates");
                    continue;
                }

                k.setMatchingDoor(d);

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

        Level level = mapLoader.getTileMap("map1");

        int nRow = level.getNRows(), nCol = level.getNCols();
        System.out.println(nRow + "\t" + nCol);

//        for (int i = 0; i < nRow; i++) {
//            for (int j = 0; j < nCol; j++) {
//                Vec2i coord = new Vec2i(i, j);
//                level.getTile(coord).listEntities();
//                System.out.print("\t");
//            }
//            System.out.println();
//        }

        level.displayTileMap();

        for (String s : level.getObjectives()) {
            System.out.println(s);
        }

    }
}
