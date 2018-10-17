package server.controller;

import server.controller.util.MapReader;
import server.controller.util.RequestUtil;
import spark.*;

import static server.controller.util.RequestUtil.*;

/**
 * The class which handles map data manipulation.
 * @author Peiyu Tang 2018 Oct.
 */
public class mapController {

    private static final String NO_LOGIN = "Not logged in";
    private static final String NO_VAR = "No variable";
    private static final String DUP_MAP = "Duplicate map names";
    private static final String MAP_SUCC = "Create map successful";
    public static final String MAP_FAIL = "Cannot find specified map.";

    /**
     * Handling all maps fetching
     */
    public static Route getAllMaps = (Request request, Response response) -> {
        // User is logged in, all user given the same
        if (!loginController.userIsLoggedIn(request)) { return NO_LOGIN; }
        else { return new MapReader().getHeader(); }
    };

    /**
     * Handle when user request a specific map
     */
    public static Route getOne = (Request request, Response response) -> {
        // If the user has logged in
        if (!loginController.userIsLoggedIn(request)) { return NO_LOGIN; }
        else {
            String currUser = RequestUtil.getWantedUser(request);
            String wantedMap = RequestUtil.getWantedMap(request);

            if (currUser.isEmpty() || wantedMap.isEmpty()) { return NO_VAR; }
            else { return new MapReader().getMap(currUser, wantedMap); }
        }
    };

    /**
     * Handle adding new maps
     */
    public static Route addNewMap = (Request request, Response response) -> {
        // If the user has logged in
        if (!loginController.userIsLoggedIn(request)) { return NO_LOGIN; }
        else {
            // Get all attribute
            String userName = getSessionCurrentUser(request);
            String mapName = getUploadedMapName(request);
            String mapContent = getUploadedMap(request);

            boolean status = new MapReader().addMap(
                    userName,
                    mapName,
                    mapContent
            );

            if (!status) return DUP_MAP;
            return MAP_SUCC;
        }
    };
}
