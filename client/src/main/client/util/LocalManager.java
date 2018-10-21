package main.client.util;

import com.google.gson.Gson;
import main.app.Main;
import main.client.structure.ReqStructure;
import main.content.GameConfig;
import main.persistence.JsonPersistor;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Manages all server requests and parses data
 */
public class LocalManager {

    /**
     * Class representing maps stored in GSON form
     */
    public static class LocalStructure {
        public String username;
        public String mapname;
        public String mapContent;

        /**
         * Generic constructor
         * @param username : username of Gamer
         * @param mapname : name of the stored map
         * @param mapContent : content of map in single string
         */
        public LocalStructure(String username, String mapname, String mapContent) {
            this.username = username;
            this.mapname = mapname;
            this.mapContent = mapContent;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj.getClass() != LocalStructure.class) return false;
            LocalStructure redefined = (LocalStructure) obj;
            return this.username.equals(redefined.username) &&
                    this.mapname.equals(redefined.mapname) &&
                    this.mapContent.equals(redefined.mapContent);
        }

    }

    /**
     * Generic constructor
     */
    public class MapStruct {
        /**
         * Class representing the file name of a level
         */
        public class Header {
            public String name;
            public String mapname;
        }
        Header header;
        String content;
    }


    private static String PATH = "./src/asset/local/";
    private static File localFiles = new File(PATH);

    /**
     * Creates a local directory for a user
     * @param username : username of the Gamer
     */
    public static void addUser(String username) {

        if (!new File(PATH + username).mkdirs() ||
                !new File(PATH + username + "/" + "drafts").mkdirs() ||
                !new File(PATH + username + "/" + "downloads").mkdirs() ||
                !new File(PATH + username + "/" + "stats").mkdirs()) {
            System.out.println("Internal Error: Make directory failed.");
        }
    }

    /**
     * Check if the user has logged in before
     * @param username : username of the Gamer
     * @return true if they have logged in before
     */
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

    /**
     * Checks if a map exists locally
     * @param request : request to server
     * @param path : path of the map
     * @return true if map exists
     */
    public static boolean hasMapLocal(ReqStructure request, String path) {
        File[] files = new File(PATH + Main.currClient.getLoggedUser() + "/" + path + "/").listFiles();

        for (File x: files) {
            System.out.println(x.getName());
        }

        if (files == null || files.length == 0) { return false; }
        else {
            return Arrays.stream(files)
                    .map(e -> e.getName().equals(request.mapname + ".json"))
                    .filter(e -> e)
                    .findFirst()
                    .orElse(false);
        }
    }

    /**
     * Download the map and put in local directory
     * @param request the provided request by the user
     * @param requestedUser : the Gamer's username
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

    /**
     * Overloaded method
     * @param username : Gamer's username
     * @param mapName : name of map being added
     * @param mapContent : String content of the map
     * @param where : path of the map
     */
    public static void addMap(String username, String mapName, String mapContent, String where) {

        System.out.println(mapName);
        File wantDir;
        if (!where.equals("default")) {
            wantDir = new File(PATH + username + "/drafts/" + mapName + ".json");
        } else {
            wantDir = new File(PATH + "default/" + mapName + ".json");
        }

        System.out.println(wantDir.getName());
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
            }
            else { System.out.println("Cannot create file."); }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void loadConfig() {
        Main.gameConfig = GameConfig.load(getConfigPath());
    }

    public static void saveConfig() {
        new JsonPersistor().save(getConfigPath(), Main.gameConfig, GameConfig.SerialisationProxy.getBuilder().create());
    }

    public static void uploadConfig() {
        saveConfig();
        Main.currClient.uploadStats(getGsonConverter().toJson(Main.gameConfig));
    }

    public static void downloadConfig() {
        Main.gameConfig = getGsonConverter().fromJson(Main.currClient.downloadStats(), GameConfig.class);
        saveConfig();
    }

    private static Gson getGsonConverter() {
        return GameConfig.SerialisationProxy.getBuilder().create();
    }

    private static String getConfigPath() {
        if (Main.currClient.isLoggedin()) {
            return PATH + Main.currClient.getLoggedUser() + "/stats/statistic.json";
        }
        else {
            return PATH + "default/stats/statistic.json";
        }
    }


    // Fetches the header of all the local maps to display for play
    /**
     * Gets the local maps
     * @param loggedUser : Username of the Gamer
     * @return : List of local maps
     */
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

    /**
     * Gets the local drafts
     * @param loggedUser : username of the Gamer
     * @return : List of local drafts
     */
    public static ArrayList<LocalStructure> fetchLocalDraft(String loggedUser) {
        File usrDir;

        if (loggedUser.equals("default")) { usrDir = new File(PATH + loggedUser + "/"); }
        else { usrDir = new File(PATH + loggedUser + "/drafts/"); }

        File[] files = usrDir.listFiles();

        if (files != null || files.length == 0)
            return Arrays.stream(files)
                    .filter(e -> !e.getName().equals("stats"))
                    .map(LocalManager::parseStruct)
                    .collect(Collectors.toCollection(ArrayList::new));
        else
            return new ArrayList<>();
    }

    /**
     * Converts a file to a LocalStructure
     * @param file : file to be converted
     * @return the LocalStructure
     */
    private static LocalStructure parseStruct(File file) {
        // Parse the file into a map
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            // Find the json object
            String json = reader.readLine();
            return new Gson().fromJson(json, LocalStructure.class);
        } catch (FileNotFoundException e) {
            System.out.println("Error in getting local maps.");
        } catch (IOException e) {
            System.out.println("Error in getting maps.");
        }
        return null;
    }

    /**
     * Adds a draft locally
     * @param mapName : name of the draft
     * @param mapContent : Drafts file
     */
    public static void LocalDraftAdd(String mapName, File mapContent) {

        String mapStringContent = parseMap(mapContent);

        System.out.println(mapContent);

        if (mapStringContent == null) { System.out.println("Publish operations failed"); }
        if (Main.currClient.isLoggedin()) {
            if (hasLogged(Main.currClient.getLoggedUser())) {
                addMap(
                        Main.currClient.getLoggedUser(),
                        mapName,
                        mapStringContent,
                        "null"
                );
            }
            else {
                addMap(
                        Main.currClient.getLoggedUser(),
                        mapName,
                        mapStringContent,
                        "default"
                );
            }
        }
        else {
            addMap(
                    "Unknown",
                    mapName,
                    mapStringContent,
                    "default"
            );
        }
        if (mapContent.delete()){ System.out.println("File deletion complete!!"); }
        else { System.out.println("Something is wrong with buffer"); }
    }

    /**
     * Converts a file to a String
     * @param mapContent File to be converted
     * @return the single string
     */
    private static String parseMap(File mapContent) {
        System.out.println(mapContent);
        try (BufferedReader reader = new BufferedReader(new FileReader(mapContent))) {
            StringBuilder contentMap = new StringBuilder();
            String Line;

            while ((Line = reader.readLine()) != null) {
                contentMap.append(Line);
                contentMap.append("\n");
            }

            return contentMap.toString();
        } catch (IOException e) {e.printStackTrace();}
        System.out.println("Shouldn't be here. check parseMap");
        return null;
    }

    /**
     * Moves a draft to a specific location
     * @param x place to move to
     */
    public static void moveDraftTo(LocalStructure x) {
        // NOTE this might be null but shouldnt

        // shouldnt be null
        // Find file in default folder
        File correctFile = Arrays.stream(new File(PATH + "default/").listFiles())
                .filter(e -> parseStruct(e).equals(x))
                .findFirst()
                .orElse(null);

        if (correctFile == null) { System.out.println("This shouldnt be null"); }

        else {
            String content = "";
            // copy file content
            try (BufferedReader reader = new BufferedReader(new FileReader(correctFile))) {
                content = reader.readLine();
            } catch (IOException e) { e.printStackTrace(); }

            // delete content
            if (correctFile.delete()) { System.out.println("File deleted correctly"); }
            // move to correct directory
            File adjustedFile = new File(PATH + Main.currClient.getLoggedUser() + "/" + "drafts/" + x.mapname + ".json");

            try {
                if (adjustedFile.createNewFile()) {
                    System.out.println("Create file. good");
                    FileWriter writer = new FileWriter(adjustedFile);
                    writer.write(content);
                    writer.close();
                }
            }
            catch (IOException e) { e.printStackTrace(); }
        }
    }

    /**
     * Overloaded method
     * @param x : place to move to
     * @param changedName : new name of the draft
     */
    public static void moveDraftTo(LocalStructure x, String changedName) {
        // NOTE this might be null but shouldnt

        // shouldnt be null
        // Find file in default folder
        File correctFile = Arrays.stream(new  File(PATH + "default/").listFiles())
                .filter(e -> parseStruct(e).equals(x))
                .findFirst()
                .orElse(null);

        if (correctFile == null) {
            System.out.println("This shouldnt be null");
        }

        else {
            String content = "";
            // copy file content
            try (BufferedReader reader = new BufferedReader(new FileReader(correctFile))) {
                content = reader.readLine();
            } catch (IOException e) { e.printStackTrace(); }

            // delete content
//            if (correctFile.delete()) { System.out.println("File deleted correctly"); }
            // move to correct directory
            File adjustedFile = new File(PATH + Main.currClient.getLoggedUser() + "/" + "drafts/" + changedName + ".json");

            try {
                if (adjustedFile.createNewFile()) {
                    System.out.println("Create file. good");
                    FileWriter writer = new FileWriter(adjustedFile);

                    LocalStructure buf = new Gson().fromJson(content, LocalStructure.class);
                    buf.mapname = changedName;
                    content = new Gson().toJson(buf, LocalStructure.class);

                    writer.write(content);
                    writer.close();
                }
            }
            catch (IOException e) { e.printStackTrace(); }
        }
    }

    // User is logged in
    public static void delMap(LocalStructure x) {
        File[] files = new File(PATH + Main.currClient.getLoggedUser() + "/downloads/").listFiles();

        File correctFile = Arrays.stream(files)
                .filter(e -> parseStruct(e).equals(x))
                .findFirst()
                .orElse(null);

        if (correctFile != null) {
            if (correctFile.delete()) {
                System.out.println("Delete nice.");
            }
        }
    }

    // User is logged in
    public static void delMapDraft(LocalStructure x, String who) {
        File[] files = new File(PATH + who + "/drafts/").listFiles();

        File correctFile = Arrays.stream(files)
                .filter(e -> parseStruct(e).equals(x))
                .findFirst()
                .orElse(null);

        if (correctFile != null) {
            if (correctFile.delete()) {
                System.out.println("Delete nice.");
            }
        }
    }
}
