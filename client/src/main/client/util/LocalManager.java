package main.client.util;

import com.google.gson.Gson;
import main.app.Main;
import main.client.structure.ReqStructure;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

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

        if (!new File(PATH + username).mkdirs() ||
                !new File(PATH + username + "/" + "drafts").mkdirs() ||
                !new File(PATH + username + "/" + "downloads").mkdirs()) {
            System.out.println("Internal Error: Make directory failed.");
        }
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
        File[] files = new File(PATH + Main.currClient.getLoggedUser() + "/downloads/").listFiles();
        if (files == null || files.length == 0) { return false; }
        else {
            return Arrays.stream(files)
                    .map(e -> e.getName().equals(request.mapname + ".json"))
                    .filter(e -> e)
                    .findFirst()
                    .orElse(false);
        }
    }

    //TODO Certain username cannot be made in this case, default and unknown
    /**
     * Download the map and put in local directory
     * @param request the provided request by the user
     */
    public static void addMap(ReqStructure request, String requestedUser) {
        File wantDir = new File(PATH + requestedUser + "/downloads/" + request.mapname + ".json");
        try {
            if (wantDir.createNewFile()) {
                FileWriter writer = new FileWriter(wantDir);
                String map = Main.currClient.RequestSpecMap(request.name, request.mapname);
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

    // Overloaded method
    public static void addMap(String username, String mapName, String mapContent, String where) {
        File wantDir;
        if (!where.equals("default")) {
            wantDir = new File(PATH + username + "/downloads/" + mapName + ".json");
        } else {
            wantDir = new File(PATH + "default" + "/downloads/" + mapName + ".json");
        }
        try {
            if (wantDir.createNewFile()) {
                FileWriter writer = new FileWriter(wantDir);
                writer.write(
                        new Gson().toJson(
                                new LocalStructure(
                                        username,
                                        mapName,
                                        mapContent
                                ), LocalStructure.class
                        )
                );
                writer.close();
            } else {
                System.out.println("Cannot create file.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // Fetches the header of all the local maps to display for play
    public static ArrayList<LocalStructure> fetchLocal(String loggedUser) {
        File usrDir = new File(PATH + loggedUser + "/downloads/");
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

    public static void LocalDraftAdd(String mapName, String mapContent) {
        if (Main.currClient.isLoggedin()) {
            if (hasLogged(Main.currClient.getLoggedUser())) {
                addMap(
                        Main.currClient.getLoggedUser(),
                        mapName,
                        mapContent,
                        "null"
                );
            }
            else {
                addMap(
                        Main.currClient.getLoggedUser(),
                        mapName,
                        mapContent,
                        "default"
                );
            }
        }
        else {
            addMap(
                    "Unknown",
                    mapName,
                    mapContent,
                    "default"
            );
        }
    }
}
