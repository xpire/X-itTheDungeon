package main.achivement;

import main.events.EventBus;

public abstract class Achievement {

    public abstract boolean isCompleted();

    public void activate(EventBus bus){};
    public void deactivate(EventBus bus){};
}
