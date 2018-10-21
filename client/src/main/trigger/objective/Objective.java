package main.trigger.objective;

/**
 * Class representing level objectives
 * @param <T> A trigger
 */
public class Objective<T extends TargetCountTrigger> {

    private T trigger;
    private ObjectiveView<T> view;
    private String name;

    /**
     * Generic constructor
     * @param name : name of objective
     * @param trigger : trigger amount
     * @param view : view of the objective
     */
    public Objective(String name, T trigger, ObjectiveView<T> view) {
        this.trigger = trigger;
        this.view = view;
        this.name = name;
    }

    /**
     * Getter for the trigger
     * @return the trigger
     */
    public T getTrigger() {
        return trigger;
    }

    /**
     * Checks if the objective is triggered
     * @return true if is triggered
     */
    public boolean isTriggered() {
        return trigger.isTriggered();
    }

    /**
     * Gets the objective view
     * @return the objectives view
     */
    public ObjectiveView<T> getView() {
        return view;
    }

    @Override
    public String toString() {
        return name;
    }
}
