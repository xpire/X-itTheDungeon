package main.events;

import javafx.event.Event;
import javafx.event.EventType;

public class LevelEvent extends GameEvent{

    public static final EventType<LevelEvent> ANY
            = new EventType<>(GameEvent.ANY,"LEVEL");

    public static final EventType<LevelEvent> LEVEL_PASSED
            = new EventType<>(LevelEvent.ANY,"LEVEL_PASSED");

    public static final EventType<LevelEvent> LEVEL_FAILED
            = new EventType<>(LevelEvent.ANY,"LEVEL_FAILED");

    private int level;

    public LevelEvent(EventType<? extends Event> eventType, int level) {
        super(eventType);
        this.level = level;
    }

    public int getLevel() {
        return this.level;
    }
}
