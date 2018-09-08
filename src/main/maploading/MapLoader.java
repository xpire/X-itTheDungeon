package main.maploading;

import main.math.Vec2i;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class MapLoader {

    public TileMap getTileMap(String mapName) {
        MapInterpreter mapInt = new MapInterpreter();
        TileMap tileMap = null;

        Scanner sc = null;

        try {
            sc = new Scanner(new File(mapName));
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

                tileMap = new TileMap(numRow, numCol);

            } else System.out.println("Error: Empty Map");

            if (tileMap == null) {
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
                    tileMap.setTile(i, j, mapInt.getTileEntities(line[j]));
                }
            }

            if (sc.hasNextLine()) {
                line = sc.nextLine().split("\\s+");
                ArrayList<String> objectives = new ArrayList<>(Arrays.asList(line));
                tileMap.setObj(objectives);
            } else System.out.println("Error: No objectives specified");

            while (sc.hasNextLine()) {
                line = sc.nextLine().split(",");
                if (line.length != 4) {
                    System.out.println("Error: Key-Door mapping incorrect");
                    break;
                }

                Vec2i keyCoord = new Vec2i(
                        Integer.parseInt(line[0]),
                        Integer.parseInt(line[1]));
                Vec2i doorCoord = new Vec2i(
                        Integer.parseInt(line[2]),
                        Integer.parseInt(line[3])
                );

                tileMap.assignDoor(keyCoord, doorCoord);
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } finally {
            if (sc != null) sc.close();
        }

        return tileMap;
    }

    public static void main(String[] args) {
        MapLoader mapLoader = new MapLoader();

        TileMap tileMap = mapLoader.getTileMap(args[0]);

        int nRow = tileMap.getNRows(), nCol = tileMap.getNCols();
        for (int i = 0; i < nRow; i++) {
            for (int j = 0; j < nCol; j++) {
                Vec2i coord = new Vec2i(i, j);
                tileMap.getTile(coord).listEntities();
                System.out.print("\t");
            }
            System.out.println();
        }

    }
}
