package main.avatar;

public class MoveState extends AvatarState {

    private final int DURATION = 5;
    private int counter = 0;

    public MoveState(Avatar avatar) {
        super(avatar);
    }

    public void enter() {
        counter = 0;
    }

    public void update(double delta) {

        counter++;

        if (counter > DURATION) {
//            avatar.enterState(avatar.getIdleState());
        }
    }
}
