package main.events;

import javafx.event.Event;
import javafx.event.EventType;


public class AvatarEvent extends GameEvent {

    public static final EventType<AvatarEvent> ANY
            = new EventType<>(GameEvent.ANY,"AVATAR");

    public static final EventType<AvatarEvent> AVATAR_TURN_ENDED
            = new EventType<>(AvatarEvent.ANY,"AVATAR_TURN_ENDED");

    public AvatarEvent(EventType<? extends Event> eventType) {
        super(eventType);
    }
}
