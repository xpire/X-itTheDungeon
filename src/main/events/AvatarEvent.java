package main.events;

import javafx.event.Event;
import javafx.event.EventType;

/**
 * Event which indicates when an avatar has passed through an exit
 */
public class AvatarEvent extends GameEvent {

    public static final EventType<AvatarEvent> ANY
            = new EventType<>(GameEvent.ANY,"AVATAR");

    public static final EventType<AvatarEvent> AVATAR_DIED
            = new EventType<>(AvatarEvent.ANY,"AVATAR_DIED");

    public static final EventType<AvatarEvent> AVATAR_TURN_ENDED
            = new EventType<>(AvatarEvent.ANY,"AVATAR_TURN_ENDED");

    /**
     * Constructor for the Exit Event
     * @param eventType type of the event
     */
    public AvatarEvent(EventType<? extends Event> eventType) {
        super(eventType);
    }
}
