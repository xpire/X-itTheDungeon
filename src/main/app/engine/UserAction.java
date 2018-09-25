package main.app.engine;

public abstract class UserAction {

    private boolean wasActive = false;
    private boolean isActive = false;

    protected void onActionBegin() {}

    protected void onAction() {}

    protected void onActionEnd() {}

    public final void update() {
        if (isActive) {
            if (!wasActive)
                onActionBegin();
            onAction();
        }
        else if (wasActive) {
            onActionEnd();
        }

        wasActive = isActive;
    }

    public final void startAction() {
        isActive = true;
    }

    public final void stopAction() {
        isActive = false;
    }
}
