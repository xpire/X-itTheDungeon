package  main.client.validator;

/**
 * Checks if input is null or empty
 */
public class BasicsChecker implements Checker {

    /**
     * Default constructor
     */
    public BasicsChecker() { }

    @Override
    public boolean inputCheck(String userInput) {
        return userInput.isEmpty() && (userInput != null);
    }
}
