package main.events;

import javafx.event.Event;
import javafx.event.EventType;
import main.stat.Statistics;

public class StatisticEvent extends GameEvent{


    public static final EventType<StatisticEvent> ANY
            = new EventType<>(GameEvent.ANY,"STATISTIC");

    public static final EventType<StatisticEvent> STATISTIC_UPDATED
            = new EventType<>(StatisticEvent.ANY,"STATISTIC_UPDATED");

    /**
     * Constructor for the Exit Event
     * @param eventType type of the event
     */
    public StatisticEvent(EventType<? extends Event> eventType, Statistics stats) {
        super(eventType);
    }
}
