package main.app.engine;

import javafx.scene.control.Alert;

/**
 * Class which prints out alerts to the application
 */
public class AlertHelper {

    /**
     * Displays an alert to the screen
     * @param alertType type of alert
     * @param title : title of the alert
     * @param message : alert message
     */
    public static void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }
}
