package main.trigger.objective;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import main.events.EventBus;
import main.events.SwitchEvent;
import main.trigger.Trigger;

/**
 * Class which tracks the "all switches activated" game objective
 */
public class DynamicTargetTrigger<T extends Event> extends TargetCountTrigger {

    private final EventHandler<T>  TARGET_UP   = e -> target++;
    private final EventHandler<T>  TARGET_DOWN = e -> target--;
    private final EventHandler<T>  COUNT_UP    = e -> count++;
    private final EventHandler<T>  COUNT_DOWN  = e -> count--;

    private EventType<T>  targetUpEvent;
    private EventType<T>  targetDownEvent;
    private EventType<T>  countUpEvent;
    private EventType<T>  countDownEvent;

    public DynamicTargetTrigger(EventType<T> targetUpEvent, EventType<T> targetDownEvent,
                                EventType<T> countUpEvent, EventType<T> countDownEvent) {
        this.targetUpEvent      = targetUpEvent;
        this.targetDownEvent    = targetDownEvent;
        this.countUpEvent       = countUpEvent;
        this.countDownEvent     = countDownEvent;
    }

    @Override
    public void activate(EventBus bus) {
        if (targetUpEvent   != null) addEventHandler(bus, targetUpEvent,     TARGET_UP);
        if (targetDownEvent != null) addEventHandler(bus, targetDownEvent,   TARGET_DOWN);
        if (countUpEvent    != null) addEventHandler(bus, countUpEvent,      COUNT_UP);
        if (countDownEvent  != null) addEventHandler(bus, countDownEvent,    COUNT_DOWN);
    }

    @Override
    public void deactivate(EventBus bus) {
        if (targetUpEvent   != null) removeEventHandler(bus, targetUpEvent,     TARGET_UP);
        if (targetDownEvent != null) removeEventHandler(bus, targetDownEvent,   TARGET_DOWN);
        if (countUpEvent    != null) removeEventHandler(bus, countUpEvent,      COUNT_UP);
        if (countDownEvent  != null) removeEventHandler(bus, countDownEvent,    COUNT_DOWN);
    }
}
