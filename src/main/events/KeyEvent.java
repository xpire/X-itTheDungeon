package main.events;

import javafx.event.Event;
import javafx.event.EventType;
import main.entities.pickup.Key;
import main.entities.terrain.Door;

public class KeyEvent extends GameEvent {

    public static final EventType<KeyEvent> ANY
            = new EventType<>(GameEvent.ANY,"KEY");

    public static final EventType<KeyEvent> KEY_REMOVED
            = new EventType<>(KeyEvent.ANY,"KEY_REMOVED");

    private Key key;

    public KeyEvent(EventType<? extends Event> eventType, Key key) {
        super(eventType);
        this.key = key;
    }

    public boolean isMatchingDoor(Door door) {
        return key.isMatchingDoor(door);
    }
}
