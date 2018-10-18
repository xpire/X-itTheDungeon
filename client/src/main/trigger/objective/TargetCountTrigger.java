package main.trigger.objective;

import main.trigger.Trigger;

public abstract class TargetCountTrigger extends Trigger {

    protected int target;
    protected int count;

    public boolean isTriggered() {
        return count >= target;
    }

    public int getTarget() {
        return target;
    }

    public int getCount() {
        return count;
    }
}
