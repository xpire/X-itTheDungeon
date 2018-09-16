package main.app;

import javafx.stage.Stage;

public interface AppState {
    void load(Game game, Stage stage) throws Exception;
}