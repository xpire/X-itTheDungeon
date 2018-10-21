package main.events;

import javafx.event.Event;
import javafx.event.EventType;
import main.entities.terrain.Door;

/**
 * An event which indicates that a door has been removed from a drafts
 */
public class DoorEvent extends GameEvent {

    public static final EventType<DoorEvent> ANY
            = new EventType<>(GameEvent.ANY,"DOOR");

    public static final EventType<DoorEvent> DOOR_REMOVED
            = new EventType<>(DoorEvent.ANY,"DOOR_REMOVED");

    public static final EventType<DoorEvent> DOOR_OPENED
            = new EventType<>(DoorEvent.ANY,"DOOR_OPENED");

    private Door door;

    /**
     * Constructor for the Door Event
     * @param eventType type of event
     * @param door Door which has been removed
     */
    public DoorEvent(EventType<? extends Event> eventType, Door door) {
        super(eventType);
        this.door = door;
    }

    /**
     * Getter for the removed door
     * @return the removed door
     */
    public Door getDoor() {
        return door;
    }
}
