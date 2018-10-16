package main.events;

import javafx.event.Event;
import javafx.event.EventType;

/**
 * Event which indicates when an avatar has passed through an exit
 */
public class ExitEvent extends GameEvent {

    public static final EventType<ExitEvent> ANY
            = new EventType<>(GameEvent.ANY,"EXIT");

    public static final EventType<ExitEvent> EXIT_SUCCESS
            = new EventType<>(ExitEvent.ANY,"EXIT_SUCCESS");

    /**
     * Constructor for the Exit Event
     * @param eventType type of the event
     */
    public ExitEvent(EventType<? extends Event> eventType) {
        super(eventType);
    }
}
