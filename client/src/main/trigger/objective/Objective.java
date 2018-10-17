package main.trigger.objective;

import main.trigger.objective.ObjectiveView;
import main.trigger.Trigger;

public class Objective<T extends TargetCountTrigger> {

    private T model;
    private ObjectiveView<T> view;

    public Objective(T model, ObjectiveView<T> view) {
        this.model = model;
        this.view = view;
    }

    public T getTrigger() {
        return model;
    }

    public ObjectiveView<T> getView() {
        return view;
    }
}
