package main.events;

import javafx.event.Event;
import javafx.event.EventType;

public class EnemyEvent extends GameEvent {

    public static final EventType<EnemyEvent> ANY
            = new EventType<>(GameEvent.ANY,"ENEMY");

    public static final EventType<EnemyEvent> ENEMY_CREATED
            = new EventType<>(EnemyEvent.ANY,"ENEMY_CREATED");

    public static final EventType<EnemyEvent> ENEMY_KILLED
            = new EventType<>(EnemyEvent.ANY,"ENEMY_KILLED");

    public EnemyEvent(EventType<? extends Event> eventType) {
        super(eventType);
    }
}
