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

        @Override
        public boolean equals(Object obj) {
            if (obj.getClass() != LocalStructure.class) return false;
            LocalStructure redefined = (LocalStructure) obj;
            return this.username.equals(redefined.username) &&
                    this.mapname.equals(redefined.mapname) &&
                    this.mapContent.equals(redefined.mapContent);
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

    // Fetches the header of all the local maps to display for play
    public static ArrayList<LocalStructure> fetchLocalDraft(String loggedUser) {
        File usrDir;

        if (loggedUser.equals("default")) { usrDir = new File(PATH + loggedUser + "/"); }
        else { usrDir = new File(PATH + loggedUser + "/drafts/"); }

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

    private static String parseMap(File mapContent) {
        System.out.println(mapContent);
        try (BufferedReader reader = new BufferedReader(new FileReader(mapContent))) {
            StringBuilder contentMap = new StringBuilder();
            String Line;

            while ((Line = reader.readLine()) != null) {
                contentMap.append(Line);
            }

            return contentMap.toString();
        } catch (IOException e) {e.printStackTrace();}
        System.out.println("Shouldn't be here. check parseMap");
        return null;
    }

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
}
