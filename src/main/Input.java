package main;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCharacterCombination;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.HashMap;

public class Input implements EventHandler<KeyEvent> {

    private HashMap<KeyCode, Boolean> prevKeys = new HashMap<>();
    private HashMap<KeyCode, Boolean> keys = new HashMap<>();


    public void onKeyEvent(KeyEvent evt) {

        KeyCode code = evt.getCode();

        if (evt.getEventType() == KeyEvent.KEY_PRESSED) {
            keys.put(code, true);

        } else if (evt.getEventType() == KeyEvent.KEY_RELEASED) {
            keys.put(evt.getCode(), false);
        }
    }


    public void update() {
        prevKeys = new HashMap<>(keys);
    }

    public void processInputs() {

    }


    public Boolean isDown(KeyCode code) {
        return !wasHeld(code) && isHeld(code);
    }

    /**
     * Sets the given KeyCode to isDown mode (FOR TESTING PURPOSES)
     * @param code
     */
    public void setIsDown(KeyCode code) {
        prevKeys.put(code, false);
        keys.put(code, true);
    }

    public Boolean isUp(KeyCode code) {
        return wasHeld(code) && !isHeld(code);
    }

    /**
     * Sets the given keyCode to isUp mode (FOR TESTING PURPOSES)
     * @param code
     */
    public void setIsUp(KeyCode code) {
        prevKeys.put(code, true);
        keys.put(code, false);
    }

    public Boolean isHeld(KeyCode code) {
        return keys.getOrDefault(code, false);
    }

    private Boolean wasHeld(KeyCode code) {
        return prevKeys.getOrDefault(code, false);
    }

    @Override
    public void handle(KeyEvent event) {
        onKeyEvent(event);
    }
}
