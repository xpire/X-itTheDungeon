package server.controller.util;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.reflect.TypeToken;
import server.controller.mapController;
import server.model.Header;
import server.model.Map;
import com.google.gson.Gson;
import spark.ResponseTransformer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * This class handles all json related
 * @author  Peiyu Tang
 */
public class JsonUtil {
    /**
     * Reference from: https://stackoverflow.com/questions/5554217/google-gson-deserialize-listclass-object-generic-type
     * Convert a header to json format
     * @param dataObj requested object
     * @return json object
     */
    public static String headerToJson(Object dataObj) {
        List<Header> dataList = (ArrayList<Header>) dataObj;
        Type listType = new TypeToken<ArrayList<Header>>(){}.getType();
        return new Gson().toJson(dataList, listType);
    }

    /**
     * Interface to the function above
     * @return The json object in string formal
     */
    public static ResponseTransformer headerJson() { return JsonUtil::headerToJson; }

    /**
     * From data object to the String form
     * @param dataObj the Map object
     * @return the Json object in String
     */
    public static String mapToJson(Object dataObj) {
        if (dataObj == null) {
            return mapController.MAP_FAIL;
        }
        else {
            Gson gson = new Gson();
            return gson.toJson(dataObj, Map.class);
        }
    }

    /**
     * Interface to the function above
     * @return The json object
     */
    public static ResponseTransformer mapJson() {
        return JsonUtil::mapToJson;
    }

    public static ExclusionStrategy onlyUsername = new ExclusionStrategy() {
        public boolean shouldSkipField(FieldAttributes fieldAttributes) {
            if ("password".equals(fieldAttributes.getName())){ return true; }
            return false;
        }

        public boolean shouldSkipClass(Class aClass) { return false; }
    };
}
