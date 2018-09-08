package MapLoading;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class LoadMap {

    public static void main(String[] args) {
        MapInterpreter mapInt = new MapInterpreter();
        Scanner sc = null;

        try {
            sc = new Scanner(new File(args[0]));

            int i = -2, numRow = 0, numCol = 0;
            String[] objective = null;

            while (sc.hasNextLine()) {
                String[] line = sc.nextLine().split("\\s+");
                if (i == -2) {
                    numRow = Integer.parseInt(line[0]);
                    numCol = Integer.parseInt(line[1]);
                    i++;
                } else if (i == -1) {
                    objective = line;
                    i++;
                } else if (i < numRow) {
                    if (line.length != numCol) {
                        System.out.println("Input map error");
                        break;
                    }

                    TileMap tileMap = new TileMap(numRow, numCol);

                    for (int j = 0; j < numCol; j++) {
                        tileMap.setTile(i, j, mapInt.getTileEntities(line[j]));
                    }

                    i++;
                } else {
                    //key mapping code
                }

            }

            if (objective != null) {
                System.out.println("Map objectives are: ");
                for (String str : objective) {
                    System.out.print(str);
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } finally {
            if (sc != null) sc.close();
        }

    }

}
