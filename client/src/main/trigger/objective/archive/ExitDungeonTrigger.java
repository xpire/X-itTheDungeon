package main.trigger.objective.archive;

import javafx.event.EventHandler;
import main.events.EventBus;
import main.events.ExitEvent;
import main.trigger.Trigger;

/**
 * Class which tracks the "Pass through the exit" achievement
 */
public class ExitDungeonTrigger extends Trigger {

    private boolean hasExited = false;

    private final EventHandler<ExitEvent> EXIT_SUCCESS = e -> hasExited = true;

    @Override
    public void activate(EventBus bus) {
        bus.addEventHandler(ExitEvent.EXIT_SUCCESS, EXIT_SUCCESS);
    }

    @Override
    public void deactivate(EventBus bus) {
        bus.removeEventHandler(ExitEvent.EXIT_SUCCESS, EXIT_SUCCESS);
    }

    @Override
    public boolean isTriggered() {
        return hasExited;
    }
}
