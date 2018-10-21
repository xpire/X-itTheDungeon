package main.trigger.objective;

import main.trigger.Trigger;

/**
 * Abstract class for the achievement targets
 */
public abstract class TargetCountTrigger extends Trigger {

    protected int target;
    protected int count;

    /**
     * Generic constructor
     * @param target : the achievement target
     * @param count : the current count
     */
    protected TargetCountTrigger(int target, int count) {
        this.target = target;
        this.count = count;
    }

    /**
     * Check if achievement has been triggered
     * @return
     */
    public boolean isTriggered() {
        return count >= target;
    }

    /**
     * Getter for the target
     * @return the target
     */
    public int getTarget() {
        return target;
    }


    /**
     * Getter for the count
     * @return the count
     */
    public int getCount() {
        return count;
    }

    /**
     * Sets the target
     * @param target the new target
     */
    public void setTarget(int target) {
        this.target = target;
        post();
    }
}
