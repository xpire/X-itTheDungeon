package server.controller.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import server.model.Header;
import server.model.UserStatus;
import spark.*;

import java.util.List;

/**
 * The utility class that handles actions of request
 * @author Peiyu Tang 2018 Oct.
 */
public class RequestUtil {
    private static class userContent {
        public String username;
        public String password;

        public userContent(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public String getUsername() { return username; }
        public String getPassword() { return password;}
    }


    private static class addNewMap {
        public String mapName;
        public String mapContent;
    }

    /**
     * The body of the request is in JSON format as follows
     * {
     *     "username": String,
     *     "password": String
     * }
     * @param request Current request
     * @return Query username
     */
    public static String getQueryUsername(Request request) { return new Gson().fromJson(request.body(), userContent.class).getUsername(); }

    @Deprecated
    /**
     * Changes behavior of Gson and parse only username
     * @param request The incoming request
     * @return The username
     */
    public static String getOnlyMapName(Request request) {
        GsonBuilder builder = new GsonBuilder();
        builder.setExclusionStrategies(JsonUtil.onlyUsername);
        Gson gson = builder.create();
        return gson.fromJson(request.body(), userContent.class).getUsername();
    }

    /**
     *  JSON format is same as above.
     * @param request current request
     * @return Query password
     */
    public static String getQueryPassword(Request request) { return new Gson().fromJson(request.body(), userContent.class).getPassword(); }

    public static String getSessionCurrentUser(Request request) { return request.session().attribute("currentUser"); }

    /**
     * JSON format is as follows
     * {
     *     "name": String,
     *     "mapName": String
     * }
     * Getting the requested username of the maps
     * @param request the user request for
     * @return The wanted user
     */
    public static String getWantedUser(Request request) { return new Gson().fromJson(request.body(), Header.class).getName(); }

    /**
     * Return the name of the wanted maps
     * @param request The user request
     * @return The wanted maps name of the user
     */
    public static String getWantedMap(Request request) { return new Gson().fromJson(request.body(), Header.class).getMapname(); }

    /**
     * JSON format for request is as follows
     * {
     *      "mapName": String,
     *      "mapContent": String
     * }
     * Get the content of the uploaded maps
     * @param request The current request of the user
     * @return The content user uploaded maps
     */
    public static String getUploadedMap(Request request) { return new Gson().fromJson(request.body(), addNewMap.class).mapContent; }

    /**
     * Get the name of the maps given by the user
     * @param request The current request of the user
     * @return The name of the user entered maps
     */
    public static String getUploadedMapName(Request request) { return new Gson().fromJson(request.body(), addNewMap.class).mapName; }
    public static String getUpdateName(Request request) { return new Gson().fromJson(request.body(), UserStatus.class).getUsername(); }

    /**
     * JSON format is as follows
     * {
     *     "username": String
     *     "enemyKilled": Integer
     *     "treasureCollected": Integer
     *     "maxLevel" : Integer
     *     "bombsKilled" : Integer
     *     "levelStar" : Integer[1...m]
     * }
     *
     * The followin function retrieves information from the user request about the statistical achievement about the specific user
     * @param request The request of the user in current time instance
     * @return any if the attribute requested
     */
    public static Integer getenemyKilled(Request request) { return new Gson().fromJson(request.body(), UserStatus.class).getEnemyKilled(); }

    public static Integer gettreasureCollected(Request request) { return new Gson().fromJson(request.body(), UserStatus.class).getTreasureCollected();}

    public static Integer getmaxLevel(Request request) { return new Gson().fromJson(request.body(), UserStatus.class).getMaxLevel(); }

    public static Integer getbombsKilled(Request request) { return new Gson().fromJson(request.body(), UserStatus.class).getBombsKilled(); }

    public static List<Integer> getLevelStar(Request request) { return new Gson().fromJson(request.body(), UserStatus.class).getLevelStar(); }
}
