package main.avatar;

import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import main.Game;
import main.GameWorld;
import main.math.Vec2d;
import main.math.Vec2i;


public class Avatar {

    private GameWorld world;

    private Vec2i gridPos;
    private Circle view;


    private AvatarState state;

    private AvatarState idleState = new IdleState(this);
    private AvatarState moveState = new MoveState(this);

    public Avatar(GameWorld world) {

        this.gridPos = new Vec2i(0, 0);
        this.world = world;

        enterState(idleState);

        view = new Circle();
        view.setRadius(10);
        view.setFill(Color.AQUA);
    }


    public void update(double delta) {
//        state.update(delta);

        Vec2i pos = new Vec2i(gridPos);

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


        if ( !pos.equals(gridPos) ) {
            if (world.isPassable(pos)) {
                moveTo(pos);
            }
        }
    }


    public void moveTo(Vec2i pos) {
        gridPos = new Vec2i(pos);
    }

    public Vec2i getGridPos(){
        return new Vec2i(gridPos);
    }


    public void render() {
        Vec2d pos = world.gridPosToWorldPosCentre(gridPos);
        view.setTranslateX(pos.getX());
        view.setTranslateY(pos.getY());
    }

    public Node getView() {
        return view;
    }


    public void enterState(AvatarState state) {
        this.state = state;
        state.enter();
    }

    public AvatarState getIdleState() {
        return idleState;
    }

    public AvatarState getMoveState() {
        return moveState;
    }
}
