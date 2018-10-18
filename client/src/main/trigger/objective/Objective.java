package main.trigger.objective;

import main.trigger.objective.ObjectiveView;
import main.trigger.Trigger;

public class Objective<T extends TargetCountTrigger> {

    private T trigger;
    private ObjectiveView<T> view;

    public Objective(T trigger, ObjectiveView<T> view) {
        this.trigger = trigger;
        this.view = view;
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
}
