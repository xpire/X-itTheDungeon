package main.app;

import javafx.scene.input.KeyCode;
import main.app.engine.UserAction;

public class GameConfig {

    private double width = 960;
    private double height = 640;

    private KeyBinding keyBinding = new KeyBinding();

    public GameConfig() {

        keyBinding.addBinding(KeyCode.K, new UserAction() {
            @Override
            protected void onActionBegin() {

            }
        });
    }
}
