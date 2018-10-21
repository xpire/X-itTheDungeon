package main.app.engine;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.HashMap;

/**
 * Class which handles input during a game
 */
public class Input {

    private boolean isListening = false;

    private HashMap<KeyCode, UserAction> bindings = new HashMap<>();

    /**
     * Begins listening for input
     */
    public void startListening() {
        isListening = true;
    }

    /**
     * Stops listening for input
     */
    public void stopListening() {
        isListening = false;
        bindings.values().forEach(UserAction::stopAction);
    }

    /**
     * Actions when a key is pressed
     * @param evt the key event which occurs
     */
    public void onKeyEvent(KeyEvent evt) {
        if (!isListening) return;

        KeyCode code = evt.getCode();

        if (evt.getEventType() == KeyEvent.KEY_PRESSED) {
            startAction(code);
        } else if (evt.getEventType() == KeyEvent.KEY_RELEASED) {
            stopAction(code);
        }
    }

    /**
     * Adds a key binding to the game
     * @param code : Keyboard stroke
     * @param action : In game action
     */
    public void addBinding(KeyCode code, UserAction action) {
        bindings.put(code, action);
    }

    /**
     * Updates the status of keys which correspond to actions
     */
    public void update() {
        bindings.values().forEach(UserAction::update);
    }

    /**
     * Begins an key-action
     * @param code the corresponding keystroke
     */
    private void startAction(KeyCode code) {
        if (bindings.containsKey(code))
            bindings.get(code).startAction();
    }

    /**
     * Ends a key-action
     * @param code : the corresponding keystroke
     */
    private void stopAction(KeyCode code) {
        if (bindings.containsKey(code))
            bindings.get(code).stopAction();
    }
}
