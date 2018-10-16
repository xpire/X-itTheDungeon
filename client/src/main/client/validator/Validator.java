package main.client.validator;

import java.util.ArrayList;

/**
 * Class to validate the input given by the user through certain criteria
 * @author  Peiyu Tang 2018 Oct.
 */
public class Validator {
    ArrayList<Checker> currcheckers;
    {
        currcheckers = new ArrayList<>();
    }
    /**
     * Using the validators to see if the String is allowed
     * please call at the end pls
     * @param input The raw input of the user
     * @return If the input passes all the validation
     */
    public boolean validate(String input) {
        return currcheckers.
                stream().
                map(e -> e.inputCheck(input))
                .filter(e -> e)
                .findFirst()
                .orElse(false);
    }

    /**
     * Infer to check the basics
     * @return current instance
     */
    public Validator checkBasics() {
        currcheckers.add(new BasicsChecker());
        return this;
    }

    /**
     * Infer to whitelist
     * @return current instance
     */
    public Validator checkChars() {
        currcheckers.add(new WhiteList());
        return this;
    }

    /**
     * Infer that the client to check for the length of the input
     * @param minLength Minimum length given
     * @param maxLength Maximum length given
     * @return Current instance
     */
    public Validator checkLength (int minLength, int maxLength) {
        currcheckers.add(new LengthChecker(minLength, maxLength));
        return this;
    }

}
