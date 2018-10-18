package main.trigger;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import main.events.EventBus;

import java.util.ArrayList;
import java.util.function.Consumer;

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


    protected <T extends Event> void addEventHandler(EventBus bus, EventType<T> type, EventHandler<? super T> handler) {
        bus.addEventHandler(type, handler);
        bus.addEventHandler(type, POST);
    }

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

    public void unsubscribe(Runnable callback) {
        listeners.remove(callback);
    }

    protected void post() {
        listeners.forEach(Runnable::run);
    }
}
