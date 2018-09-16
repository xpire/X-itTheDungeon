package main.events;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

public abstract class GameEvent extends Event {

    public static final EventType<GameEvent> ANY = new EventType<>(Event.ANY,"GAME");

    public GameEvent(EventType<? extends Event> eventType) {
        super(eventType);
    }

    public GameEvent(Object source, EventTarget target, EventType<? extends Event> eventType) {
        super(source, target, eventType);
    }
}
