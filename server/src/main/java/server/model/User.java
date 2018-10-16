package server.model;

public class User {
    private String username;
    private String salt;
    private String hashedPassword;

    public User(String username, String salt, String hashedPassword) {
        this.username = username;
        this.salt = salt;
        this.hashedPassword = hashedPassword;
    }

    /**
     * @return Get username
     */
    public String getUsername() { return username; }

    /**
     * @return The hashed password
     */
    public String getHashedPassword() { return hashedPassword; }

    /**
     * @return The salt of the user
     */
    public String getSalt() { return salt; }
}
