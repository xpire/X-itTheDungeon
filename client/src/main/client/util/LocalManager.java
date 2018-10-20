package main.client.util;


import main.app.Main;

import java.util.ArrayList;

//TODO This needs to be done !!!!!
public class LocalManager {
    public class LocalStructure {
        String username;
        String mapname;
        String mapContent;
    }

    private static String PATH = "./src/asset/local/";
    public static boolean hasMapLocal(String mapInfo, String currUser) {
        if (Main.currClient.isLoggedin()) { }
        else { }
        return false;
    }

    /**
     * Download the map and put in local directory
     * @param mapInfo the provided request by the user
     */
    public static void addMap(String mapInfo, String currUser) {
    }

    // Has this user logged on before?
    public static boolean hasLogged(String username) {
        return false;
    }

    // Create a new directory with a specific user
    public static void addUser(String username) {
    }

    // Fetches the header of all the local maps to display for play
    public static ArrayList<String> fetchLocal(String loggedUser) {
        return null;
    }
}
