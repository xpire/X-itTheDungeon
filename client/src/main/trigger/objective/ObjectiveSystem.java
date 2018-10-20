package main.trigger.objective;

import main.events.EventBus;

import java.util.ArrayList;
import java.util.Iterator;

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

    public String getObjectives() {
        StringBuilder sb = new StringBuilder();

        for (Objective objective : objectives) {
            sb.append(objective.toString()).append("\t");
        }

        return sb.toString();
    }

    public void clearObjectives() {
        for (Objective objective : objectives) {
            objective.getTrigger().deactivate(eventBus);
        }
        objectives.clear();
    }
}