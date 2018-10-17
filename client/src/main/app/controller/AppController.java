package main.app.controller;

import main.app.model.AppScreen;

public abstract class AppController {

    protected AppScreen screen;

    public AppController(AppScreen screen) {
        this.screen = screen;
    }

    public void switchScreen(AppScreen newScreen) {
        screen.onDestroyed();
        newScreen.start();
    }
}
