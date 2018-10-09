package main.events;

import javafx.event.Event;
import javafx.event.EventType;
import main.content.IntStat;

public class StatisticEvent extends GameEvent{


    public static final EventType<StatisticEvent> ANY
            = new EventType<>(GameEvent.ANY,"STATISTIC");

    public static final EventType<StatisticEvent> STATISTIC_UPDATED
            = new EventType<>(StatisticEvent.ANY,"STATISTIC_UPDATED");

    public StatisticEvent(EventType<? extends Event> eventType, IntStat stats) {
        super(eventType);
    }
}
