package main.events;

import javafx.event.Event;
import javafx.event.EventType;

public class ExitEvent extends GameEvent {

    public static final EventType<ExitEvent> ANY
            = new EventType<>(GameEvent.ANY,"EXIT");

    public static final EventType<ExitEvent> EXIT_SUCCESS
            = new EventType<>(ExitEvent.ANY,"EXIT_SUCCESS");

    public ExitEvent(EventType<? extends Event> eventType) {
        super(eventType);
    }
}
