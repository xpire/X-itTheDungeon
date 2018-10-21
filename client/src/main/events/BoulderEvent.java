package main.events;

import javafx.event.Event;
import javafx.event.EventType;

/**
 * An event which indicates that a door has been removed from a drafts
 */
public class BoulderEvent extends GameEvent {

    public static final EventType<BoulderEvent> ANY
            = new EventType<>(GameEvent.ANY,"BOULDER");

    public static final EventType<BoulderEvent> BOULDER_BOMBED
            = new EventType<>(BoulderEvent.ANY,"BOULDER_BOMBED");

    /**
     * Generic constructor
     * @param eventType : type of event
     */
    public BoulderEvent(EventType<? extends Event> eventType) {
        super(eventType);
    }
}
