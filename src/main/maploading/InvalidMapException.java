package main.maploading;

public class InvalidMapException extends Exception {

    public InvalidMapException() {
        super();
    }

    public InvalidMapException(String message) {
        super(message);
    }

    public InvalidMapException(String message, Throwable cause) {
        super(message, cause);
    }
}
