package  main.client.validator;

/**
 * Checker interface
 */
public interface Checker {
    /**
     * Check that the those input are allowed to be send to server
     * @param userInput the inputted chars
     * @return If the strings can be requested into the server
     */
    boolean inputCheck(String userInput);
}
