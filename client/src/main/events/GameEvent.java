package main.events;

import javafx.event.Event;
import javafx.event.EventType;

/**
 * Class to abstract the events of the game
 */
public abstract class GameEvent extends Event {

    public static final EventType<GameEvent> ANY = new EventType<>(Event.ANY,"GAME");

    /**
     * Generic constructor
     * @param eventType : type of the event
     */
    public GameEvent(EventType<? extends Event> eventType) {
        super(eventType);
    }
}
