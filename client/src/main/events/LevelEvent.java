package main.events;

import javafx.event.Event;
import javafx.event.EventType;

/**
 * Event describing the completion of a level
 */
public class LevelEvent extends GameEvent{

    public static final EventType<LevelEvent> ANY
            = new EventType<>(GameEvent.ANY,"LEVEL");

    public static final EventType<LevelEvent> LEVEL_PASSED
            = new EventType<>(LevelEvent.ANY,"LEVEL_PASSED");

    public static final EventType<LevelEvent> LEVEL_FAILED
            = new EventType<>(LevelEvent.ANY,"LEVEL_FAILED");

    private int level;

    /**
     * Generic constructor
     * @param eventType : type of event
     * @param level : level its attached to
     */
    public LevelEvent(EventType<? extends Event> eventType, int level) {
        super(eventType);
        this.level = level;
    }

    /**
     * Getter for the level related to the event
     * @return the level
     */
    public int getLevel() {
        return this.level;
    }
}
