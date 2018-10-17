package  main.client.validator;

public class BasicsChecker implements  Checker {
    public BasicsChecker() { }

    @Override
    public boolean inputCheck(String userInput) {
        return userInput.isEmpty() && (userInput != null);
    }
}
