package main.achivement;

import javafx.event.EventHandler;
import main.events.EventBus;
import main.events.ExitEvent;

public class ExitDungeonAchievement extends Achievement{

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

    public boolean isCompleted() {
        return hasExited;
    }
}
