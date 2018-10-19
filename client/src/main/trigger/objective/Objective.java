package main.trigger.objective;

public class Objective<T extends TargetCountTrigger> {

    private T trigger;
    private ObjectiveView<T> view;
    private String name;

    public Objective(String name, T trigger, ObjectiveView<T> view) {
        this.trigger = trigger;
        this.view = view;
        this.name = name;
    }

    public T getTrigger() {
        return trigger;
    }

    public boolean isTriggered() {
        return trigger.isTriggered();
    }

    public ObjectiveView<T> getView() {
        return view;
    }

    @Override
    public String toString() {
        return name;
    }
}
