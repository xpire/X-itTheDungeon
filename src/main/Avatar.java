package main;

import javafx.scene.input.KeyCode;
import javafx.scene.shape.Circle;
import main.math.Vec2f;
import main.math.Vec2i;

public class Avatar {

    private Game game;

    private Vec2f mapPos;
    private Vec2i gridPos;
    private Circle view = new Circle();

    public Avatar(Game game) {

        this.gridPos = new Vec2i(0, 0);
        this.mapPos = new Vec2f(0.0f, 0.0f);
        this.game = game;
    }


    public void update(float delta) {


        if (Game.input.isHeld(KeyCode.UP)) {
            gridPos.add(0, -1);
        }
        else if (Game.input.isHeld(KeyCode.DOWN)) {
            gridPos.add(0, 1);
        }
        else if (Game.input.isHeld(KeyCode.LEFT)) {
            gridPos.add(-1, 0);
        }
        else if (Game.input.isHeld(KeyCode.RIGHT)) {
            gridPos.add(1, 0);
        }



    }

    public void moveTo(int row, int col) {
//        TileMap map = game.getMap();
//        main.math.Vec2f pos = map.tileCentreToWorld(row, col);


    }

    public void render() {
//        view.setTranslateX(pos.getX());
//        view.setTranslateY(pos.getY());
    }
}
