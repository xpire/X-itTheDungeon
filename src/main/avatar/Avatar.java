package main.avatar;

import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import main.Game;
import main.GameWorld;
import main.entities.Entity;
import main.math.Vec2d;
import main.math.Vec2i;


public class Avatar extends Entity {

    protected GameWorld world;

    public Avatar(GameWorld world) {
        super("Avatar");
        this.world = world;
    }


    @Override
    public void onCreated() {
        Circle circle = new Circle();
        circle.setRadius(10);
        circle.setFill(Color.AQUA);

        view.addNode(circle);
        view.setCentre(new Vec2d(0, 0));
    }


    public void update(double delta) {
        Vec2i pos = new Vec2i(getGridPos());

        if (Game.input.isDown(KeyCode.UP)) {
            pos._add(0, -1);
        }
        else if (Game.input.isDown(KeyCode.DOWN)) {
            pos._add(0, 1);
        }
        else if (Game.input.isDown(KeyCode.LEFT)) {
            pos._add(-1, 0);
        }
        else if (Game.input.isDown(KeyCode.RIGHT)) {
            pos._add(1, 0);
        }


        if ( !pos.equals(getGridPos()) ) {
//            if (world.isPassable(pos)) {
//                moveTo(pos);
//            }
////            else {
////                world.push(this, pos);
////            }

            world.moveEntity(this, pos);
        }
    }


    public void render() {
        // should pull it back out
//        Vec2d pos = world.gridPosToWorldPosCentre(gridPos);
//        view.setTranslateX(pos.getX());
//        view.setTranslateY(pos.getY());
    }
}
