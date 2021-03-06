package server.controller.util;

import server.Application;
import server.model.User;
import server.model.UserStatus;
import com.google.gson.Gson;
import org.mindrot.jbcrypt.BCrypt;
import spark.Request;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import static server.controller.util.RequestUtil.*;

/**
 * Handles all user wise operations
 * @author Peiyu Tang 2018 Sept.
 */
public class UserReader {
    private static Gson gson;
    {
        gson = new Gson();
    }

    private final static String PATH1 = "./src/main/resources/users.json";
    private final static String PATH2 = "./src/main/resources/status.json";

    public UserReader() { }

    public static void UpdateUser(Request request) {
        Map<String, String> reqContent = new Gson().fromJson(request.body(), Map.class);

        try {
            BufferedReader currFile = new BufferedReader(new FileReader(PATH2));
            String currLine;
            StringBuilder fileBuffer = new StringBuilder();

            // read all the lines from the prev file
            while ((currLine = currFile.readLine()) != null) {
                // This is the current json
                Map<String, String> status = gson.fromJson(currLine, Map.class);
                if (status.get("userName").equals(reqContent.get("userName"))) { fileBuffer.append(new Gson().toJson(reqContent, Map.class)); }
                else { fileBuffer.append(currLine); }

                fileBuffer.append('\n');
            }

            // Write to the file again
            FileWriter pw = new FileWriter(new File(PATH2));
            pw.write(fileBuffer.toString());
            pw.close();

        }
        catch(FileNotFoundException e) { Application.forceStop("Internal Server Error: Cannot read status file."); }
        catch (IOException e) { Application.forceStop("Internal Server Error: Cannot copy status file."); }
    }

    public static String retrieve(Request request) {
        Map<String, String> reqContent = new Gson().fromJson(request.body(), Map.class);

        try {
            BufferedReader currFile = new BufferedReader(new FileReader(PATH2));
            String currLine;

            // read all the lines from the prev file
            while ((currLine = currFile.readLine()) != null) {
                // This is the current json
                Map<String, String> status = gson.fromJson(currLine, Map.class);
                if (status.get("userName").equals(reqContent.get("userName"))) { return (status.get("statContent")); }
            }
        }
        catch(FileNotFoundException e) { Application.forceStop("Internal Server Error: Cannot read status file."); }
        catch (IOException e) { Application.forceStop("Internal Server Error: Cannot copy status file."); }
        return null;
    }


    /**
     * Check if the user exists in the data base
     * @param username Provided username is in the database
     * @return If the user is match
     */
    public boolean hasUser(String username) {
        try (BufferedReader br = new BufferedReader(new FileReader(PATH1))) {
            String line;
            while ((line = br.readLine()) != null) {
                User curr = gson.fromJson(line, User.class);

                if (curr == null) continue;
                // Check if this username exist
                if (curr.getUsername().equals(username)) { return true; }
            }
            return false;
        }
        catch (IOException e) { Application.forceStop("Internal Server Error: Cannot read file."); }
        return false;
    }

    /**
     * Check if the user exists in the data base
     * @param username Provided username is in the database
     * @param password Check the password is a match
     * @return If the user is match
     */
    public boolean hasUser(String username, String password) {
        //System.out.println(password);

        try (BufferedReader br = new BufferedReader(new FileReader(PATH1))) {
            String line;
            // Loop through all lines
            while ((line = br.readLine()) != null) {
                User curr = gson.fromJson(line, User.class);
                if (curr == null) continue;

                // Check username
                if (username.equals(curr.getUsername()) && (BCrypt.hashpw(password, curr.getSalt()).equals(curr.getHashedPassword()))) { return true; }
            }
            return false;
        }
        catch (IOException e) { Application.forceStop("Internal Server Error: cannot read file."); }
        return false;
    }

    /**
     * Create a user and append to last place and add an empty status of a user
     * @param currUser Queried username
     * @param currPassword Queried to add password
     */
    public void addUser(String currUser, String currPassword) {
        try {
            FileWriter nWriter = new FileWriter(new File("./src/main/resources/users.json"), true);
            FileWriter sWriter = new FileWriter(new File("./src/main/resources/status.json"),true);

            String newSalt = BCrypt.gensalt();
            User addUser = new User(
                    currUser,
                    newSalt,
                    BCrypt.hashpw(currPassword, newSalt)
            );

            // Write the actual function
            try { nWriter.write(gson.toJson(addUser, User.class) + "\n"); }
            catch (Exception e) { Application.forceStop("Error writing to file"); }

            Map<String, String> temp = new HashMap<>();

            temp.put("userName", addUser.getUsername());
            temp.put("statContent", "");

            // Write the actual function
            try { sWriter.write(gson.toJson(temp, Map.class) + "\n"); }
            catch (Exception e) { Application.forceStop("Error writing to file"); }

            nWriter.close();
            sWriter.close();
        } catch (IOException e){ Application.forceStop("Internal Server error: cannot write to file."); }
    }
}