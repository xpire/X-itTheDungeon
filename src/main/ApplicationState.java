package main;

import javafx.stage.Stage;

public interface ApplicationState {

    public void load(Game game, Stage stage) throws Exception;


}
