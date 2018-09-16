package main.events;

import javafx.event.Event;
import javafx.event.EventType;

public class SwitchEvent extends GameEvent {

    public static final EventType<SwitchEvent> ANY
            = new EventType<>(GameEvent.ANY,"SWITCH");

    public static final EventType<SwitchEvent> SWITCH_ACTIVATED
            = new EventType<>(SwitchEvent.ANY,"SWITCH_ACTIVATED");

    public static final EventType<SwitchEvent> SWITCH_DEACTIVATED
            = new EventType<>(SwitchEvent.ANY,"SWITCH_DEACTIVATED");

    public static final EventType<SwitchEvent> SWITCH_CREATED
            = new EventType<>(SwitchEvent.ANY,"SWITCH_CREATED");

    public static final EventType<SwitchEvent> SWITCH_DESTROYED
            = new EventType<>(SwitchEvent.ANY,"SWITCH_DESTROYED");

    public SwitchEvent(EventType<? extends Event> eventType) {
        super(eventType);
    }
}
