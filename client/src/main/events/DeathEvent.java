package main.events;

import javafx.event.Event;
import javafx.event.EventType;

/**
 * Event which indicates when an avatar has passed through an exit
 */

public class DeathEvent extends GameEvent {

    public static final EventType<DeathEvent> ANY
            = new EventType<>(GameEvent.ANY,"DEATH");

    public static final EventType<DeathEvent> DEATH_BY_ATTACK
            = new EventType<>(DeathEvent.ANY,"DEATH_BY_ATTACK");

    public static final EventType<DeathEvent> DEATH_BY_ARROW
            = new EventType<>(DeathEvent.ANY,"DEATH_BY_ARROW");

    public static final EventType<DeathEvent> DEATH_BY_BOMB
            = new EventType<>(DeathEvent.ANY,"DEATH_BY_BOMB");

    public static final EventType<DeathEvent> DEATH_BY_FALL
            = new EventType<>(DeathEvent.ANY,"DEATH_BY_FALL");

    private boolean isAvatar;

    public DeathEvent(EventType<? extends Event> eventType, boolean isAvatar) {
        super(eventType);
        this.isAvatar = isAvatar;
    }

    public boolean isAvatar() {
        return isAvatar;
    }
}
