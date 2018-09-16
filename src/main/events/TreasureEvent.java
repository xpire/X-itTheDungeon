package main.events;

import javafx.event.Event;
import javafx.event.EventType;

/**
 * Event to indicate when a Treasure has been created or collected
 * from the Level
 */
public class TreasureEvent extends GameEvent {

    public static final EventType<TreasureEvent> ANY
            = new EventType<>(GameEvent.ANY,"TREASURE");

    public static final EventType<TreasureEvent> TREASURE_CREATED
            = new EventType<>(TreasureEvent.ANY,"TREASURE_CREATED");

    public static final EventType<TreasureEvent> TREASURE_COLLECTED
            = new EventType<>(TreasureEvent.ANY,"TREASURE_COLLECTED");

    /**
     * Constructor for the Treasure Event
     * @param eventType type of the event
     */
    public TreasureEvent(EventType<? extends Event> eventType) {
        super(eventType);
    }
}
