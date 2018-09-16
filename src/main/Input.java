package main;

import javafx.event.EventHandler;
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

    public Boolean isDown(KeyCode code) {
        return !wasHeld(code) && isHeld(code);
    }

    public Boolean isUp(KeyCode code) {
        return wasHeld(code) && !isHeld(code);
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
