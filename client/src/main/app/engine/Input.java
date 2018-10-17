package main.app.engine;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.HashMap;

public class Input {

    // 1. allow binding switching

    // 2. allow for multiple user actions per key-code by
    // storing a list of user actions instead

//    private boolean isProcessing = false;
    private boolean isListening = false;

    private HashMap<KeyCode, UserAction> bindings = new HashMap<>();

    public void startListening() {
        isListening = true;
    }

    public void stopListening() {
        isListening = false;
        bindings.values().forEach(UserAction::stopAction);
    }
//    public void startProcessing() {
//        isProcessing = true;
//    }
//
//    public void stopProcessing() {
//        isProcessing = false;
//    }

    public void onKeyEvent(KeyEvent evt) {
        if (!isListening) return;

        KeyCode code = evt.getCode();

        if (evt.getEventType() == KeyEvent.KEY_PRESSED) {
            startAction(code);
        } else if (evt.getEventType() == KeyEvent.KEY_RELEASED) {
            stopAction(code);
        }
    }

    public void addBinding(KeyCode code, UserAction action) {
        bindings.put(code, action);
    }

    public void removeBinding(KeyCode code) {
        bindings.remove(code);
    }

    public void update() {
        bindings.values().forEach(UserAction::update);
    }

    private void startAction(KeyCode code) {
        if (bindings.containsKey(code))
            bindings.get(code).startAction();
    }

    private void stopAction(KeyCode code) {
        if (bindings.containsKey(code))
            bindings.get(code).stopAction();
    }
}
