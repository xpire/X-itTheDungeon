package main.app.model;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import main.GameWorld;
import main.app.controller.AppController;
import main.app.controller.PlayLevelController;

public class PlayLevelScreen extends AppScreen {

    {
        title = "X-it the Dungeon";
        fxmlName = "main/app/view/playLevel.fxml";
    }

    private GameWorld world;
    private PlayLevelController controller;

    public PlayLevelScreen(Stage stage) {
        super(stage);
        this.controller = new PlayLevelController(this);
    }


//    @Override
//    public void onStart() {
//        input.startListening();
//    }
//
//    @Override
//    public void onUpdateBe() { input.update(); }
//
//    @Override
//    public void onUpdate() {
//        world.update();
//    }
//
//    @Override
//    public void onAfterUpdate() {}
//
//    @Override
//    public void onStop() {}

    @Override
    protected void beforeSceneDisplay(Scene scene) {
        Pane layer = controller.getDynamicLayer();

        world = new GameWorld(scene);
        layer.getChildren().add(world.getView());
        world.startGame();
    }

    @Override
    protected AppController getController() {
        return controller;
    }
}
