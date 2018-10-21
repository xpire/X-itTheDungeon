package server.controller;

import server.controller.util.RequestUtil;
import server.controller.util.UserReader;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Controls all the user and identity related operations, nothings is fetched into the system
 * buffer, therefore must check the local directory
 * @author Peiyu Tang 2018 Sept.
 */
public class userController {

    /**
     * Handle when user request a specific maps
     */
    public static Route updateStatus = (Request request, Response response) -> {
        // Is the user logged in?
        if (!loginController.userIsLoggedIn(request)) { return("Not logged in"); }
        else {
            UserReader.UpdateUser(request);
            return("Update successful");
        }
    };
    public static Route getStatus = (Request request, Response response) -> UserReader.retrieve(request);

    /**
     * Authenticate the user by hashing the inputted password using the stored salt,
     * then comparing the generated hashed password to the stored hashed password
     * @param username User provided username
     * @param password User provided password
     * @return If the user have entered the correct password
     */
    public static boolean authenticate(String username, String password) { return hasUser(username, password); }

    /**
     * Check if the user exists in the data base
     * @param username Provided username is in the database
     * @param password Check the password is a match
     * @return If the user is match
     */
    public static boolean hasUser(String username, String password) { return new UserReader().hasUser(username, password); }

    /**
     * @param userName Requested username
     * @return If the data
     */
    public static boolean hasUser(String userName) { return new UserReader().hasUser(userName); }

    /**
     * Add an user requested password to the data base
     * @param currUser The query username
     * @param currPassword the query password
     */
    public static void addUser(String currUser, String currPassword) { new UserReader().addUser(currUser, currPassword); }
}
