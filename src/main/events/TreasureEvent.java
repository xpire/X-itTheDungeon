package main.events;

import javafx.event.Event;
import javafx.event.EventType;

public class TreasureEvent extends GameEvent {

    public static final EventType<TreasureEvent> ANY
            = new EventType<>(GameEvent.ANY,"TREASURE");

    public static final EventType<TreasureEvent> TREASURE_CREATED
            = new EventType<>(TreasureEvent.ANY,"TREASURE_CREATED");

    public static final EventType<TreasureEvent> TREASURE_COLLECTED
            = new EventType<>(TreasureEvent.ANY,"TREASURE_COLLECTED");

    public TreasureEvent(EventType<? extends Event> eventType) {
        super(eventType);
    }
}
