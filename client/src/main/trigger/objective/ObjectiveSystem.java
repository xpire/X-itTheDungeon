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

    /**
     * Generic constructor
     * @param bus : eventbus
     */
    public ObjectiveSystem(EventBus bus) {
        this.eventBus = bus;
    }

    /**
     * Adds an objective to the system
     * @param objective the objective to be added
     */
    public void addObjective(Objective objective) {
        objectives.add(objective);
        objective.getTrigger().activate(eventBus);
    }

    /**
     * Gets the iterator of objective views
     * @return the iterator of objective views
     */
    public Iterator<ObjectiveView> getObjectiveViews() {
        return objectives.stream()
                .map(o -> o.getView())
                .iterator();
    }

    /**
     * Check if all objectives have been triggered
     * @return true if all objectives have been triggered
     */
    public boolean checkTriggeredAll() {
        return objectives.stream().allMatch(Objective::isTriggered);
    }

    /**
     * Gets the level objectives in string form
     * @return String of objectives
     */
    public String getObjectives() {
        StringBuilder sb = new StringBuilder();

        for (Objective objective : objectives) {
            sb.append(objective.toString()).append("\t");
        }

        return sb.toString();
    }

    /**
     * Clears all current objectives
     */
    public void clearObjectives() {
        for (Objective objective : objectives) {
            objective.getTrigger().deactivate(eventBus);
        }
        objectives.clear();
    }
}