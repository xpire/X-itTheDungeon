package main.trigger.objective;


import main.events.EventBus;
import main.trigger.Trigger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.function.Function;

/**
 * Class which manages the set of triggers for a game
 */
public class ObjectiveSystem {

    private ArrayList<Objective> objectives = new ArrayList<>();
    private EventBus eventBus;

    public ObjectiveSystem(EventBus bus) {
        this.eventBus = bus;
    }

    public void addObjective(Objective objective) {
        objectives.add(objective);
        objective.getTrigger().activate(eventBus);
    }

    public Iterator<ObjectiveView> getObjectiveViews() {
        return objectives.stream()
                .map(o -> o.getView())
                .iterator();
    }

    public boolean checkTriggeredAll() {
        return objectives.stream().allMatch(Objective::isTriggered);
    }
}