package main.events;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Group;


/**
 * Shared Event Dispatcher System
 */
public class EventBus {

    private Group eventHandlers = new Group();

    public <T extends Event> void addEventHandler(EventType<T> type, EventHandler<? super T> handler) {
        eventHandlers.addEventHandler(type, handler);
    }

    public <T extends Event> void removeEventHandler(EventType<T> type, EventHandler<? super T> handler) {
        eventHandlers.removeEventHandler(type, handler);
    }

    /**
     * Sends out events to listeners
     * @param event event to be sent out
     */
    public void postEvent(Event event) {
        eventHandlers.fireEvent(event);
    }
}
