package main.app;

import javafx.scene.input.KeyCode;
import main.app.engine.UserAction;

import java.util.HashMap;

public class KeyBinding {

    private HashMap<KeyCode, UserAction> bindings = new HashMap<>();

    public void addBinding(KeyCode code, UserAction action) {
        bindings.put(code, action);
    }

    public void removeBinding(KeyCode code) {
        bindings.remove(code);
    }
}
