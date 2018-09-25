package main.app.engine;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.HashMap;

public class Input2 {

    // 1. allow binding switching
    //

    // 2. allow for multiple user actions per key-code by
    // storing a list of user actions instead
    private HashMap<KeyCode, UserAction> bindings = new HashMap<>();

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



    public void addBinding(KeyCode code, UserAction action) {
        bindings.put(code, action);
    }

    public void removeBinding(KeyCode code) {
        bindings.remove(code);
    }


    public void processInput() {

    }

    public void update() {


        prevKeys = new HashMap<>(keys);
    }

    public void clear() {
        prevKeys.clear();
        keys.clear();
    }


    public Boolean isDown(KeyCode code) {
        return !wasHeld(code) && isHeld(code);
    }

    public Boolean isUp(KeyCode code) {
        return wasHeld(code) && !isHeld(code);
    }


    private Boolean isHeld(KeyCode code) {
        return keys.getOrDefault(code, false);
    }

    private Boolean wasHeld(KeyCode code) {
        return prevKeys.getOrDefault(code, false);
    }
}
