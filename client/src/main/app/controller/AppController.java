package main.app.controller;

import main.app.model.AppScreen;

public abstract class AppController<T extends AppScreen> {

    protected T screen;

    public AppController(T screen) {
        this.screen = screen;
    }

    public void switchScreen(AppScreen newScreen) {
        screen.onDestroyed();
        newScreen.start();
    }
}
