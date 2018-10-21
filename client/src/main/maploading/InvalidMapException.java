package main.maploading;

/**
 * Invalid maps throw these exceptions
 */
public class InvalidMapException extends Exception {

    public InvalidMapException(String message) {
        super(message);
    }

}
