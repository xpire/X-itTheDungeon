package main.trigger.objective;


import main.events.EventBus;
import main.trigger.Trigger;

import java.util.ArrayList;

/**
 * Class which manages the set of triggers for a game
 */
public class TriggerSystem {

    private EventBus eventBus;
    private ArrayList<Trigger> triggers = new ArrayList<>();

    public TriggerSystem(EventBus bus) {
        this.eventBus = bus;
    }

    public void addTrigger(Trigger trigger) {
        triggers.add(trigger);
        trigger.activate(eventBus);
    }

    public boolean checkTriggeredAll() {
        return triggers.stream().allMatch(Trigger::isTriggered);
    }
}