package main.events;

import javafx.event.Event;
import javafx.event.EventType;

/**
 * Event which indicates when an avatar has passed through an exit
 */

public class AvatarDeathEvent extends GameEvent {

    public static final EventType<AvatarDeathEvent> AVATAR_DEATH
            = new EventType<>(GameEvent.ANY,"AVATAR_DEATH");

    private DeathType deathType;

    private AvatarDeathEvent(EventType<? extends Event> eventType, DeathType type) {
        super(eventType);
        this.deathType = type;
        System.out.println("Type: " + type);
    }

    public static AvatarDeathEvent death() {
        return new AvatarDeathEvent(AVATAR_DEATH, DeathType.UNDEFINED);
    }

    public static AvatarDeathEvent deathByAttack() {
        return new AvatarDeathEvent(AVATAR_DEATH, DeathType.ATTACKED);
    }

    public static AvatarDeathEvent deathByFalling() {
        return new AvatarDeathEvent(AVATAR_DEATH, DeathType.FELL);
    }

    public static AvatarDeathEvent deathByExplosion() {
        return new AvatarDeathEvent(AVATAR_DEATH, DeathType.BOMBED);
    }

    public boolean wasAttacked() {
        return deathType == DeathType.ATTACKED;
    }

    public boolean wasBombed() {
        return deathType == DeathType.BOMBED;
    }

    public boolean wasPlummeted() {
        return deathType == DeathType.FELL;
    }

    private enum DeathType {
        UNDEFINED, ATTACKED, BOMBED, FELL
    }


}
