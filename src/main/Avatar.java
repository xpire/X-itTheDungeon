package main;

import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import main.math.Vec2d;
import main.math.Vec2i;


public class Avatar {

    private GameWorld world;

    private Vec2i gridPos;
    private Circle view;


    public Avatar(GameWorld world) {

        this.gridPos = new Vec2i(0, 0);
        this.world = world;


        view = new Circle();
        view.setRadius(10);
        view.setFill(Color.AQUA);
    }


    public void update(double delta) {

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

//        gridPos.clip(new Vec2i(0, 0), world.getDimesions());
    }


    public void moveTo(Vec2i pos) {
        gridPos = new Vec2i(pos);
    }

    public Vec2i getGridPos(){
        return new Vec2i(gridPos);
    }


    public void render() {
        Vec2d pos = world.gridPosToWorldPos(gridPos);
        view.setTranslateX(pos.getX());
        view.setTranslateY(pos.getY());
    }

    public Node getView() {
        return view;
    }
}
