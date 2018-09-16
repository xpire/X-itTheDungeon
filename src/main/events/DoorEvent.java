package main.events;

import javafx.event.Event;
import javafx.event.EventType;
import main.entities.terrain.Door;

public class DoorEvent extends GameEvent {

    public static final EventType<DoorEvent> ANY
            = new EventType<>(GameEvent.ANY,"DOOR");

    public static final EventType<DoorEvent> DOOR_REMOVED
            = new EventType<>(DoorEvent.ANY,"DOOR_REMOVED");

    private Door door;

    public DoorEvent(EventType<? extends Event> eventType, Door door) {
        super(eventType);
        this.door = door;
    }

    public Door getDoor() {
        return door;
    }
}
