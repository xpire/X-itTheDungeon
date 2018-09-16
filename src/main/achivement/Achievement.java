package main.achivement;

import main.events.EventBus;

/**
 * An abstraction of game Achievements
 */
public abstract class Achievement {

    /**
     * checks if an objective has been completed
     * @return true if objective is completed, false otherwise
     */
    public abstract boolean isCompleted();

    /**
     * Activates event handling
     * @param bus Event bus to dispatch events
     */
    public void activate(EventBus bus){};

    /**
     * Removes event handlers
     * @param bus Event bus to dispatch events
     */
    public void deactivate(EventBus bus){};
}
