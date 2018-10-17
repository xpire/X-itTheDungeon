package server.controller.util;

import com.google.gson.Gson;
import server.Application;
import  server.model.Header;
import server.model.Map;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import static spark.Spark.stop;

/**
 * The class that reads the data base and handles request
 * @author Peiyu Tang 2018 Oct.
 */
public class MapReader {

    private static final String PATH = "./src/main/resources/maps/";
    private ArrayList<File> files;

    //TODO didnt handle null, make sure to test
    public MapReader() {
        File file = new File(PATH);
        files = new ArrayList<>(Arrays.asList(file.listFiles()));
    }

    /**
     * @return List of headers in the map pool
     */
    public List<Header> getHeader() {
        return files
                .stream()
                .map(this::parseHeader)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Parse the header for each file to return
     * @param currFile Current file object to parse
     * @return The headers of the required map
     */
    public Header parseHeader(File currFile) {
        try (BufferedReader reader = new BufferedReader(new FileReader(currFile))) {
            // Parse the json
            String json = reader.readLine();
            Map map = new Gson().fromJson(json, Map.class);
            return map.getHeader();
        }
        catch (IOException e) {Application.forceStop("Internal Server Error: cannot read file"); }
        return null;
    }

    /**
     * Return a specific map
     * @param currUser Requested username
     * @param wantedMap Requested wantedMap
     * @return return the specific map
     */
    public Map getMap(String currUser, String wantedMap) {
        Header currHeader = new Header (currUser, wantedMap);
        return files.stream()
                .map(this::parseMap)
                .filter(e -> e.getHeader().equals(currHeader))
                .findFirst()
                .orElse(null);
    }

    /**
     * Parse the map from a file
     * @param file Requested file
     * @return The map object from
     */
    private Map parseMap(File file) {
        // Parse the file into a map
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            // Find the json object
            String json = reader.readLine();
            return new Gson().fromJson(json,Map.class);
        }
        catch (FileNotFoundException e) {
            System.out.println("Internal Server Error: Not found file in parsing map.");
            stop();
        }
        catch (IOException e) { Application.forceStop("Internal Server Error: Not found file in parsing map."); }
        return null;
    }

    /**
     * Add another map into the database
     * @param userName The requested username
     * @param mapName The Map name preferred
     * @param mapContent the content of the map
     */
    public boolean addMap(String userName, String mapName, String mapContent) {
        String NEW_PATH = PATH + mapName + ".json";
        // Instantiate new map
        Map map = new Map(new Header(userName, mapName), mapContent);
        if (thereIs(map) != null) return false;

        File newFile = new File(NEW_PATH);

        try {
            // Can create file or not
            if (newFile.createNewFile()) {
                FileWriter writer = new FileWriter(NEW_PATH);
                Gson gson = new Gson();
                writer.write(gson.toJson(map));
                writer.close();
                return true;
            }
            else { return false; }
        }
        catch (IOException e) { Application.forceStop("Internal Server Error: Cannot add map due to file error."); }
        return false;
    }

    /**
     * Check that this map can be added or not not, criteria is that maps can be he same name but the user cannot
     * be the same.
     * @param map The requested map
     * @return If the map can be added
     */
    private Map thereIs(Map map) {
        return files
                .stream()
                .map(this::parseMap)
                .filter(x -> checkMap(x, map))
                .findFirst()
                .orElse(null);
    }

    private boolean checkMap(Map eleMap, Map checkMap) { return eleMap.allowCo(checkMap); }


}
