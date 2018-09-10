package main.avatar;

import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import main.Game;
import main.GameWorld;
import main.component.GridPositionComponent;
import main.entities.Entity;
import main.math.Vec2d;
import main.math.Vec2i;


public class Avatar extends Entity {


    private AvatarState state;

    private AvatarState idleState = new IdleState(this);
    private AvatarState moveState = new MoveState(this);

    private GameWorld world;

    public Avatar(GridPositionComponent gpc, GameWorld world) {

        super("Avatar", gpc);
        moveTo(0,0);

        enterState(idleState);

        this.world = world;

        Circle circle = new Circle();
        circle.setRadius(10);
        circle.setFill(Color.AQUA);

        view.addNode(circle);
        view.setCentre(new Vec2d(0, 0));
    }


    public void update(double delta) {
//        state.update(delta);

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
            if (world.isPassable(pos)) {
                moveTo(pos);
            }
            else {
                world.push(this, pos);
            }
        }
    }


    public void render() {
        // should pull it back out
//        Vec2d pos = world.gridPosToWorldPosCentre(gridPos);
//        view.setTranslateX(pos.getX());
//        view.setTranslateY(pos.getY());
    }

    public void enterState(AvatarState state) {
        this.state = state;
        state.enter();
    }

//    public AvatarState getIdleState() {
//        return idleState;
//    }
//
//    public AvatarState getMoveState() {
//        return moveState;
//    }
}
