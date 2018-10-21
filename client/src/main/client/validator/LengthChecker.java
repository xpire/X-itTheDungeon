package  main.client.validator;

/**
 * This checks the user if the length is allowed
 * @author Peiyu Tang 2018 Oct.
 */
public class LengthChecker implements Checker {

    private int minLength;
    private int maxLength;

    /**
     * Generic constructor
     * @param minLength : minimum length
     * @param maxLength : maximum length
     */
    public LengthChecker(int minLength, int maxLength) {
        this.minLength = minLength;
        this.maxLength = maxLength;
    }

    @Override
    public boolean inputCheck(String userInput) { return minLength <= userInput.length() && userInput.length() <= maxLength; }
}
