package main.client.validator;

/**
 * Perform whitelisting on all allowed characters.
 * May perform badly on corrupted characters, can change if have time
 * @author Peiyu Tang
 */
public class WhiteList implements Checker {

    public WhiteList() { }

    @Override
    public boolean inputCheck(String userInput) {
        return userInput
                .chars()
                .mapToObj(this::isAllowed)
                .filter(e -> !e)
                .findFirst()
                .map(e -> !e)
                .orElse(true);
    }

    /**
     * If this character is allowed
     * @param c the inputted character
     * @return if e is allowed
     */
    private boolean isAllowed(int c) {
        return (c >= 'a' && c <= 'z') ||
                (c >= 'A' && c <= 'Z') ||
                (c >= '0' && c <= '9');
    }
}
