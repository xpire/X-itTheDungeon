package main.events;

import javafx.event.Event;
import javafx.event.EventType;

public class AchievementEvent extends GameEvent {

    public static final EventType<AchievementEvent> ANY
            = new EventType<>(GameEvent.ANY,"ACHIEVEMENT");

    public static final EventType<AchievementEvent> DOOR_REMOVED
            = new EventType<>(AchievementEvent.ANY,"ACHIEVEMENT_COMPLETED");

    private String name;

    /**
     * Constructor for the Door Event
     * @param eventType type of event
     * @param name Door which has been removed
     */
    public AchievementEvent(EventType<? extends Event> eventType, String name) {
        super(eventType);
        this.name = name;
    }

    /**
     * Getter for the removed door
     * @return the removed door
     */
    public String getName() {
        return name;
    }
}
