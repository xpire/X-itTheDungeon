package main.client.util;

import com.google.gson.Gson;
import main.app.Main;
import main.client.structure.ReqStructure;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

//TODO This needs to be done !!!!!
public class LocalManager {

    public static class LocalStructure {
        public String username;
        public String mapname;
        public String mapContent;

        public LocalStructure(String username, String mapname, String mapContent) {
            this.username = username;
            this.mapname = mapname;
            this.mapContent = mapContent;
        }
    }

    public class MapStruct {
        public class Header {
            public String name;
            public String mapname;
        }
        Header header;
        String content;
    }


    private static String PATH = "./src/asset/local/";
    private static File localFiles = new File(PATH);

    public static void addUser(String username) {
        if (!new File(PATH + username).mkdirs()) { System.out.println("Internal Error: Make directory failed."); }
    }

    // Has this user logged on before?
    public static boolean hasLogged(String username) {
        File[] fileList = localFiles.listFiles();
        if (fileList == null || fileList.length == 0) { return false; }
        else {
            return Arrays
                    .stream(fileList)
                    .map(e -> e.getName().equals(username))
                    .filter(e -> e)
                    .findFirst()
                    .orElse(false);
        }
    }

    // Does this map exist?
    public static boolean hasMapLocal(ReqStructure request) {
        File[] files = new File(PATH + request.name +"/").listFiles();

        if (files == null || files.length == 0) { return false; }
        else {
            return Arrays.stream(files)
                    .map(e -> e.getName().equals(request.mapname + ".json"))
                    .filter(e -> e)
                    .findFirst()
                    .orElse(false);
        }
    }

    //TODO Auto logout functionality is not set
    /**
     * Download the map and put in local directory
     * @param request the provided request by the user
     */
    public static void addMap(ReqStructure request, String requestedUser) {
        File wantDir = new File(PATH + requestedUser + "/" + request.mapname + ".json");
        try {
            if (wantDir.createNewFile()) {
                FileWriter writer = new FileWriter(wantDir);
                String map = Main.currClient.RequestSpecMap(request.name, request.mapname);
                //Type type = new TypeToken<Map<String, String>>(){}.getType();
                MapStruct data = new Gson().fromJson(map, MapStruct.class);

                writer.write(
                        new Gson().toJson(
                            new LocalStructure(
                                    data.header.name,
                                    data.header.mapname,
                                    data.content
                            ), LocalStructure.class
                        )
                );
                writer.close();
            }
            else {
                System.out.println("Cannot create file: ");
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Fetches the header of all the local maps to display for play
    public static ArrayList<LocalStructure> fetchLocal(String loggedUser) {
        File usrDir = new File(PATH + loggedUser + "/");
        File[] files = usrDir.listFiles();

        if (files != null || files.length == 0)
            return Arrays.stream(files)
                .map(LocalManager::parseStruct)
                .collect(Collectors.toCollection(ArrayList::new));
        else
            return new ArrayList<>();
    }

    private static LocalStructure parseStruct(File file) {
        // Parse the file into a map
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            // Find the json object
            String json = reader.readLine();
            return new Gson().fromJson(json, LocalStructure.class);
        }
        catch (FileNotFoundException e) { System.out.println("Error in getting local maps."); }
        catch (IOException e) { System.out.println("Error in getting maps."); }
        return null;
    }
}
