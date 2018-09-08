package MapLoading;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class LoadMap {

    public static void main(String[] args) {
        MapInterpreter mapInt = new MapInterpreter();
        TileMap tileMap = null;
        Scanner sc = null;

        try {
            sc = new Scanner(new File(args[0]));

            int numRow = 0, numCol = 0;
            String[] line;

            if (sc.hasNextLine()) {
                line = sc.nextLine().split("\\s+");
                if (line.length != 2) {
                    System.out.println("Error: Bad Map Size");
                    return;
                }
                numRow = Integer.parseInt(line[0]);
                numCol = Integer.parseInt(line[1]);
            } else System.out.println("Error: Empty Map");

            for (int i = 0; i < numRow; i++) {
                line = sc.nextLine().split("\\s+");

                //quickly check that the right number of columns are present
                if (line.length != numCol) {
                    System.out.println("Error: Inconsistent Map");
                    break;
                }

                tileMap = new TileMap(numRow, numCol);

                for (int j = 0; j < numCol; j++) {
                    tileMap.setTile(i, j, mapInt.getTileEntities(line[j]));
                }
            }

            if (tileMap == null) {
                System.out.println("Map not loaded properly");
                return;
            }

            if (sc.hasNextLine()) {
                line = sc.nextLine().split("\\s+");
                ArrayList<String> objectives = new ArrayList<>(Arrays.asList(line));
                tileMap.setObj(objectives);
            } else System.out.println("Error: No Objectives Specified");

            while (sc.hasNextLine()) {
                line = sc.nextLine().split(",");
                if (line.length != 4) {
                    System.out.println("Key-Door Map went wrong");
                    break;
                }

                int[] keyCoord = new int[2];
                keyCoord[0] = Integer.parseInt(line[0]);
                keyCoord[1] = Integer.parseInt(line[1]);
                int[] doorCoord = new int[2];
                doorCoord[0] = Integer.parseInt(line[2]);
                doorCoord[1] = Integer.parseInt(line[3]);

                tileMap.assignDoor(keyCoord, doorCoord);

            }

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } finally {
            if (sc != null) sc.close();
        }

        // quick testing code
        if (tileMap == null) {
            System.out.println("Map not loaded properly");
            return;
        }

        ArrayList<String> testObjectives = tileMap.getObjectives();
        System.out.println("Map objectives are:");
        for (String str : testObjectives) {
            System.out.print(" " + str);
        }
    }
}
