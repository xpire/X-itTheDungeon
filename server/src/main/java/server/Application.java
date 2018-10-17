package server;

import server.controller.*;
import server.controller.util.*;
import static server.controller.util.JsonUtil.headerJson;
import static server.controller.util.JsonUtil.mapJson;
import static spark.Spark.*;

/**
 * Main class to run the server, as well as read all map from directory
 * Main controller class.
 * @author Peiyu Tang 2018 Sept.
 */
public class Application {
    public static void main(String[] args) {
        // Initialize server status
        InitServer();

        // Handles Login and logout
        post(Path.Web.LOGIN, loginController.handleLoginPost);
        post(Path.Web.LOGOUT, loginController.handleLogoutPost);
        post(Path.Web.REGISTER, loginController.handleRegisterPost);

        // Handles request in requesting map from client
        post(Path.Web.REQUESTONE, mapController.getOne, mapJson());
        get(Path.Web.REQUESTALL, mapController.getAllMaps, headerJson());
        post(Path.Web.UPLOAD, mapController.addNewMap);
        post(Path.Web.UPDATE, userController.updateStatus);

        // Fix the response type to json
        after((req, res) -> { res.type("application/json"); });
    }

    /**
     *  Initialize features of server and session features
     */
    private static void InitServer() {
        port(8080);
        // Session time is set at 30 mins currently, however can be set for higher in later
        staticFiles.expireTime(1800);
    }

    public static void forceStop(String errMsg) {
        System.out.println(errMsg);
        stop();
    }

}
