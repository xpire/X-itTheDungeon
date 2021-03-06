package main.trigger.objective;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import main.events.EventBus;

/**
 * Class which tracks the "all switches activated" game objective
 */
public class FixedTargetTrigger<T extends Event> extends TargetCountTrigger {

    private final EventHandler<T>  COUNT_UP    = e -> count++;
    private final EventHandler<T>  COUNT_DOWN  = e -> count--;

    private EventType<T>  countUpEvent;
    private EventType<T>  countDownEvent;

    public FixedTargetTrigger(EventType<T> countUpEvent, EventType<T> countDownEvent, int target) {
        super(target, 0);
        this.countUpEvent   = countUpEvent;
        this.countDownEvent = countDownEvent;
    }

    @Override
    public void activate(EventBus bus) {
        if (countUpEvent   != null) addEventHandler(bus, countUpEvent,   COUNT_UP);
        if (countDownEvent != null) addEventHandler(bus, countDownEvent, COUNT_DOWN);
    }

    @Override
    public void deactivate(EventBus bus) {
        if (countUpEvent   != null) removeEventHandler(bus, countUpEvent,   COUNT_UP);
        if (countDownEvent != null) removeEventHandler(bus, countDownEvent, COUNT_DOWN);
    }
}
