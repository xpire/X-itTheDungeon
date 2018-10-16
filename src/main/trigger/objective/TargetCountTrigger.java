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
public class TargetCountTrigger extends Trigger {

    private int target = 0;
    private int count = 0;

    private final EventHandler<Event>  TARGET_UP   = e -> target++;
    private final EventHandler<Event>  TARGET_DOWN = e -> target--;
    private final EventHandler<Event>  COUNT_UP    = e -> count++;
    private final EventHandler<Event>  COUNT_DOWN  = e -> count--;

    private EventType<Event>  targetUpEvent;
    private EventType<Event>  targetDownEvent;
    private EventType<Event>  countUpEvent;
    private EventType<Event>  countDownEvent;

    public TargetCountTrigger(EventType<Event> targetUpEvent, EventType<Event> targetDownEvent,
                              EventType<Event> countUpEvent, EventType<Event> countDownEvent) {
        this.targetUpEvent      = targetUpEvent;
        this.targetDownEvent    = targetDownEvent;
        this.countUpEvent       = countUpEvent;
        this.countDownEvent     = countDownEvent;
    }


    @Override
    public void activate(EventBus bus) {
        if (targetUpEvent   != null) bus.addEventHandler(targetUpEvent,     TARGET_UP);
        if (targetDownEvent != null) bus.addEventHandler(targetDownEvent,   TARGET_DOWN);
        if (countUpEvent    != null) bus.addEventHandler(countUpEvent,      COUNT_UP);
        if (countDownEvent  != null) bus.addEventHandler(countDownEvent,    COUNT_DOWN);
    }

    @Override
    public void deactivate(EventBus bus) {
        if (targetUpEvent   != null) bus.removeEventHandler(targetUpEvent,     TARGET_UP);
        if (targetDownEvent != null) bus.removeEventHandler(targetDownEvent,   TARGET_DOWN);
        if (countUpEvent    != null) bus.removeEventHandler(countUpEvent,      COUNT_UP);
        if (countDownEvent  != null) bus.removeEventHandler(countDownEvent,    COUNT_DOWN);
    }

    @Override
    public boolean isTriggered() {
        return count >= target;
    }
}
