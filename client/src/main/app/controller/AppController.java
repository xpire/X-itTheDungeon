package main.app.controller;

import main.app.model.AppScreen;

/**
 * Abstract controller class
 * @param <T> : The generic controller
 */
public abstract class AppController<T extends AppScreen> {

    protected T screen;

    /**
     * Generic constructor
     * @param screen : the corresponding screen
     */
    public AppController(T screen) {
        this.screen = screen;
    }

    /**
     * Switches to a new Screen
     * @param newScreen : Screen to display
     */
    public void switchScreen(AppScreen newScreen) {
        screen.onDestroyed();
        newScreen.start();
    }
}
