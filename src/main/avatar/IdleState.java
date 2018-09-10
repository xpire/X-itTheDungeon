package main.avatar;

import javafx.scene.input.KeyCode;
import main.Game;
import main.math.Vec2i;

public class IdleState extends AvatarState {

    public IdleState(Avatar avatar) {
        super(avatar);
    }

    public void update(double delta) {
        Vec2i pos = avatar.getGridPos();

        if (Game.input.isDown(KeyCode.UP)) {
            pos.add(0, -1);
        }
        else if (Game.input.isDown(KeyCode.DOWN)) {
            pos.add(0, 1);
        }
        else if (Game.input.isDown(KeyCode.LEFT)) {
            pos.add(-1, 0);
        }
        else if (Game.input.isDown(KeyCode.RIGHT)) {
            pos.add(1, 0);
        }


        avatar.moveTo(pos);
//        if ( !pos.equals(avatar.getGridPos()) ) {
//
//            avatar.moveTo(pos);
//            avatar.enterState(avatar.getMoveState());
//        }
    }
}
