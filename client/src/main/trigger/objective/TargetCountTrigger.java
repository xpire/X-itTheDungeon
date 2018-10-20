package main.trigger.objective;

import main.trigger.Trigger;

public abstract class TargetCountTrigger extends Trigger {

    protected int target;
    protected int count;

    protected TargetCountTrigger() {
        this(0, 0);
    }

    protected TargetCountTrigger(int target, int count) {
        this.target = target;
        this.count = count;
    }

    public boolean isTriggered() {
        return count >= target;
    }

    public int getTarget() {
        return target;
    }

    public int getCount() {
        return count;
    }

    public void setTarget(int target) {
        this.target = target;
        post();
    }

    public void setCount(int count) {
        this.count = count;
        post();
    }
}
