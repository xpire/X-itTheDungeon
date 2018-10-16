package server.controller;

import server.controller.util.RequestUtil;
import spark.*;
import static server.controller.util.RequestUtil.*;

/**
 * Handles all operations relating to user attempting to login into the system.
 * @author Peiyu Tang 2018 Sept.
 */
public class loginController {
    public static final String LOGIN_FAILED = "Login Failed: Incorrect username or password. Dx";
    public static final String LOGIN_SUCC = "Login Successful. xD";
    public static final String ALREADY_LOGGED_IN = "Login Failed: Already Logged in. Dx";

    public static final String LOGOUT_NO_SUCH_USER = "Logout Failed: No such user. Dx";
    public static final String LOGOUT_NOT_LOGGED_IN = "Logout Failed: Not Logged in. Dx";
    public static final String LOGOUT_SUCC = "Logout Success. xD";

    public static final String REGISTER_FAIL = "Register Fail: User exists. Dx";
    public static final String REGISTER_SUCC = "Register Success.";

    /**
     * Handle input attempts from user
     */
    public static Route handleLoginPost = (Request request, Response response) -> {
        // Is the username and password correct
        if (!userController.authenticate(getQueryUsername(request), getQueryPassword(request))) { return LOGIN_FAILED; }
        if (userIsLoggedIn(request)) {return ALREADY_LOGGED_IN;}

        Session currSess = request.session(true);
        // Change the session definition of the current session
        currSess.attribute("currentUser", getQueryUsername(request));
        currSess.attribute("loggedOut", false);
        return LOGIN_SUCC;
    };


    // TODO how to handle multiple sessions ?
    /**
     * Handle logout functionality
     */
    public static Route handleLogoutPost = (Request request, Response response) -> {
        // Check if this string works
        String currUser = request.session().attribute("currentUser");
        // Check if the user doesn't exists
        if (!userController.hasUser(currUser) || !userIsLoggedIn(request)) { return LOGOUT_NO_SUCH_USER; }
        if (!userIsLoggedIn(request)) { return LOGOUT_NOT_LOGGED_IN; }

        // Remove user from current session
        request.session().removeAttribute("currentUser");
        request.session().attribute("loggedOut", true);
        return LOGOUT_SUCC;
    };

    /**
     * How to handle register requests from user
     */
    public static Route handleRegisterPost = (Request request, Response response) -> {
        // Check if the user exists
        String currUser = getQueryUsername(request);

        // Check if the user does exist
        if (userController.hasUser(currUser)) { return REGISTER_FAIL; }

        // Change the database
        String currPassword = getQueryPassword(request);
        if (currPassword == null || currPassword.isEmpty()) { return REGISTER_FAIL; }

        userController.addUser(currUser, currPassword);

        return REGISTER_SUCC;
    };

    /**
     * Check if the user has currently signed in or not
     * @param request current user of the user
     * @return if the user has signed in or not
     */
    public static boolean userIsLoggedIn(Request request) {
        return RequestUtil.getSessionCurrentUser(request) != null;
    }
}
