package main.events;

import javafx.event.Event;
import javafx.event.EventType;
import main.entities.prop.Boulder;
import main.entities.terrain.Door;

/**
 * An event which indicates that a door has been removed from a drafts
 */
public class BoulderEvent extends GameEvent {

    public static final EventType<BoulderEvent> ANY
            = new EventType<>(GameEvent.ANY,"BOULDER");

    public static final EventType<BoulderEvent> BOULDER_BOMBED
            = new EventType<>(BoulderEvent.ANY,"BOULDER_BOMBED");

    public BoulderEvent(EventType<? extends Event> eventType) {
        super(eventType);
    }
}
