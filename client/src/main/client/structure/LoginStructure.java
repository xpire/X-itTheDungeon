package  main.client.structure;

/**
 * Class representing the login data type
 */
public class LoginStructure {
    public String username;
    public String password;

    /**
     * Generic constructor
     * @param username : username of the Gamer
     * @param password : password of the Gamer
     */
    public LoginStructure(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
