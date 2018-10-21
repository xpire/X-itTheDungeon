package main.trigger;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import main.events.EventBus;

import java.util.ArrayList;

/**
 * An abstraction of game Achievements
 */
public abstract class Trigger {

    private final EventHandler<? super Event> POST = e -> this.post();
    private ArrayList<Runnable> listeners = new ArrayList<>();

    /**
     * checks if an objective has been completed
     * @return true if objective is completed, false otherwise
     */
    public abstract boolean isTriggered();

    /**
     * Activates event handling
     * @param bus Event bus to dispatch events
     */
    public abstract void activate(EventBus bus);

    /**
     * Removes event handlers
     * @param bus Event bus to dispatch events
     */
    public abstract void deactivate(EventBus bus);

    /**
     * Adds an event handler to certain triggers
     * @param bus event bus
     * @param type type of event
     * @param handler handler function
     * @param <T> Generic event
     */
    protected <T extends Event> void addEventHandler(EventBus bus, EventType<T> type, EventHandler<? super T> handler) {
        bus.addEventHandler(type, handler);
        bus.addEventHandler(type, POST);
    }

    /**
     * removes event handlers from triggers
     * @param bus the event bus
     * @param type type of the event
     * @param handler handler function
     * @param <T> generic event
     */
    protected <T extends Event> void removeEventHandler(EventBus bus, EventType<T> type, EventHandler<? super T> handler) {
        bus.removeEventHandler(type, handler);
        bus.removeEventHandler(type, POST);
    }

    /**
     * subscribes to the trigger
     */
    public void subscribe(Runnable callback) {
        listeners.add(callback);
    }

    /**
     * Notifies listeners that an even has been triggered
     */
    protected void post() {
        listeners.forEach(Runnable::run);
    }
}
