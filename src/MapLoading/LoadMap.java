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
            while (sc.hasNextLine()) {
                String[] line = sc.nextLine().split("\\s+");

            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } finally {
            if (sc != null) sc.close();
        }

    }

}
