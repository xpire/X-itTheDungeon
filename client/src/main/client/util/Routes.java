package  main.client.util;

public class Routes {
    // Below are log in routes
    public static final String LanRoute = "http://192.168.0.7:8080/";
    public static final String LOGIN = "login/";
    public static final String LOGOUT = "logout/";
    public static final String REGISTER = "register/";

    // Map operation routes
    public static final String UPLOAD_ONE = "upload/";
    public static final String PULL_ONE = "reqone/";
    public static final String UPDATE = "update/";

    // Get routes
    public static final String PULL_ALL = "reqall/";

    // Error messages
    public static final String LOGIN_FAILED = "Login Failed: Incorrect username or password. Dx";
    public static final String LOGOUT_NOT_LOGGED_IN = "Logout Failed: Not Logged in. Dx";
    public static final String LOGOUT_SUCC = "Logout Success. xD";

    // Below are feedback messages sent back to User
    public static final String INVALID_INPUT = "Login Failed: Please check your input. xD";
    public static final String ALREADY_LOGGED_IN = "Login Failed: You have already Logged in. ";
    public static final String NOT_LOGGED_IN = "Login to see what other people have made. xD";
}
