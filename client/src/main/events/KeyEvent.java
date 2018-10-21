package main.events;

import javafx.event.Event;
import javafx.event.EventType;
import main.entities.pickup.Key;
import main.entities.terrain.Door;

/**
 * Event which indicates when a key has been removed in a drafts
 */
public class KeyEvent extends GameEvent {

    public static final EventType<KeyEvent> ANY
            = new EventType<>(GameEvent.ANY,"KEY");

    public static final EventType<KeyEvent> KEY_REMOVED
            = new EventType<>(KeyEvent.ANY,"KEY_REMOVED");

    private Key key;

    /**
     * Constructor for the Key Event
     * @param eventType type of the event
     * @param key key which was removed
     */
    public KeyEvent(EventType<? extends Event> eventType, Key key) {
        super(eventType);
        this.key = key;
    }

    /**
     * Checks if a door is the matching door for the removed key
     * @param door door to check
     * @return true if door matches the key, false otherwise
     */
    public boolean isMatchingDoor(Door door) {
        return key.isMatchingDoor(door);
    }
}
