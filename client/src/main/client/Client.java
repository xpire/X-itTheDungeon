package main.client;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import javafx.scene.control.Alert;
import main.app.Main;
import main.app.engine.AlertHelper;
import main.client.structure.LoginStructure;
import main.client.structure.MapStructure;
import main.client.structure.ReqStructure;
import main.client.structure.StatsStructure;
import main.client.util.LocalManager;
import main.client.util.Routes;
import main.client.validator.Validator;

import java.util.HashMap;

/**
 * A client class that handles the transition of data between the server
 * criteria for an allowed server is as following:
 * {
 *      Username must be any characters within the whitelist within 30 characters
 *      Password must be any characters within whitelist within 50 characters
 *      Password is hidden away when user types
 *      User is not given information on the the correctness of username or password
 *      User cannot log on twice with or without the same account
 * }
 * @author Peiyu Tang 2018 Oct.
 */
public class Client {
    // Below are checks for password
    public static final int minUsername = 6;
    public static final int maxUsername = 15;
    public static final int minPassword = 6;
    public static final int maxPassword = 30;

    private boolean Loggedin;
    private String LoggedUser;

    public Client() {
        Loggedin = false;
        LoggedUser = null;
    }

    /**
     * Attempt on login using the given request parameters
     * @param username Given username
     * @param password Given Password
     * @return The message from the server
     */
    public String attemptLogin (String username, String password) {
        if (!Loggedin) {
            // is all input correct
            if (new Validator()
                    .checkBasics()
                    .checkChars()
                    .checkLength(minUsername, maxUsername)
                    .validate(username) &&
                    new Validator()
                            .checkBasics()
                            .checkChars()
                            .checkLength(minPassword, maxPassword)
                            .validate(password)
            ) {
                // Parse into Gson and send to request
                String res = Post(
                        Routes.LOGIN,
                        new Gson().toJson(new LoginStructure(username, password), LoginStructure.class)
                );

                if (res == null) { return "Internet error. "; }

                if (!res.equals(Routes.LOGIN_FAILED)) {
                    Loggedin = true;
                    LoggedUser = username;

                    if (!LocalManager.hasLogged(username)) {
                        LocalManager.addUser(username);
                    }
                }
                return res;
            }
            else {
                return Routes.INVALID_INPUT;
            }
        }
        else { return Routes.ALREADY_LOGGED_IN; }
    }

    /**
     * Attempt to logout using the given usernmame
     * @return The response from the server
     */
    public String attemptLogout() {
        if (Loggedin) {
            // No error checking as user is not given an input
            String res = Post(
                    Routes.LOGOUT,
                    new Gson().toJson(new LoginStructure(LoggedUser, null))
            );

            // check if its logged in
            if (res.equals(Routes.LOGOUT_SUCC)) {
                Loggedin = false;
                LoggedUser = null;
            }
            return res;
        }
        else { return Routes.LOGOUT_NOT_LOGGED_IN; }
    }

    //TODO Can register when logged in as a user ? Maybe do it from the GUI
    /**
     * Handle for which the user attempts to register a new user
     * @param username The provided username
     * @param password The provided password
     * @return
     */
    public String attemptRegister(String username, String password) {
        // is all input correct
        if (new Validator()
                .checkBasics()
                .checkChars()
                .checkLength(minUsername, maxUsername)
                .validate(username) &&
                new Validator()
                        .checkBasics()
                        .checkChars()
                        .checkLength(minPassword,maxPassword)
                        .validate(password)
        ) {
            return Post(
                    Routes.REGISTER,
                    new Gson().toJson(new LoginStructure(username, password))
            );
        } else { return Routes.INVALID_INPUT; }
    }

    /**
     * Pull all globally made maps(Only headers) into the local directory
     * @return All global repo
     */
    public String pullGlobalMaps() {
        if (Loggedin) { return Get(Routes.PULL_ALL); }
        else { return Routes.NOT_LOGGED_IN; }
    }

    // TODO make sure that if the user is not logged in do not allow them to pull the map

    /**
     * Request a specific map of the database on server
     * @param reqname The user who made the requested name
     * @param mapName The name of the map being requested
     * @return The content of the map that has been pulled down
     */
    public String RequestSpecMap(String reqname, String mapName) {
        if (Loggedin) {
            return Post(
                    Routes.PULL_ONE, new Gson().toJson(
                            new ReqStructure(reqname, mapName),
                            ReqStructure.class
                    )
            );
            // As the option does not render, this case is trivial
        } else { return Routes.NOT_LOGGED_IN; }
    }


    /**
     * Attempt at uploading the map to the server
     * @param mapName The name of the map chosen
     * @param mapContent The content of the map
     * @return The message from the server
     */
    public String attemptUpload(String mapName, String mapContent) {
        if (Loggedin) {
            return Post(
                    Routes.UPLOAD_ONE,
                    new Gson().toJson(
                            new MapStructure(
                                    mapName,
                                    mapContent
                            ),
                            MapStructure.class
                    )
            );
        }
        else { return Routes.NOT_LOGGED_IN; }
    }

    /**
     * @param statContent representation of statistics
     * @return The message returned by the server.
     */
    public String uploadStats(String statContent) {
        if (Loggedin) {
            HashMap<String, String> body = new HashMap<>();
            body.put("userName", Main.currClient.LoggedUser);
            body.put("statContent", statContent);

            return Post(
                    Routes.UPDATE,
                    new Gson().toJson(body, HashMap.class)
            );
        }
        else { return Routes.NOT_LOGGED_IN; }
    }

    public String downloadStats() {
        if (Loggedin) {
            HashMap<String, String> body = new HashMap<>();
            body.put("userName", Main.currClient.LoggedUser);

            System.out.println(Post(
                    Routes.GET_STAT,
                    new Gson().toJson(body, HashMap.class)
            ));

            return Post(
                    Routes.GET_STAT,
                    new Gson().toJson(body, HashMap.class)
            );
        }
        else { return Routes.NOT_LOGGED_IN; }
    }

    /**
     * Handles all post actions performed by the client
     * @param route The requested route
     * @param body the Body of request
     * @return The response of the POST request
     */
    private String Post(String route, String body) {
        try {
            HttpResponse<String> res = Unirest
                .post(Routes.LanRoute + route)
                .body(body)
                .asString();
            return res.getBody();
        }
        catch (UnirestException e){
            System.out.println("Not connected");
            AlertHelper.showAlert(Alert.AlertType.ERROR,"Network Error","Internet not connected.");
        }
        return null;
    }

    /**
     * Handle a unique get request
     * @param route the requested route of the user
     * @return the response from the server
     */
    private String Get(String route) {
        try {
            HttpResponse<String> res = Unirest.get(Routes.LanRoute + route).asString();
            return res.getBody();
        }
        catch (UnirestException e) {
            System.out.println("Not connected");
            AlertHelper.showAlert(Alert.AlertType.ERROR,"Network Error","Internet not connected.");
        }
        return null;
    }

    /**
     * Checks if a user is logged in
     * @return true if logged in
     */
    public boolean isLoggedin() { return Loggedin; }

    /**
     * Gets the user that is logged in
     * @return the user that is logged in
     */
    public String getLoggedUser() { return LoggedUser; }
}
