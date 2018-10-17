package main.trigger.objective;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import main.events.EventBus;
import main.trigger.Trigger;

/**
 * Class which tracks the "all switches activated" game objective
 */
public class FixedCountTrigger<T extends Event> extends Trigger {

    /**
    TODO type change required
    **/
    private int target;
    private int count;

    private final EventHandler<T>  COUNT_UP    = e -> count++;
    private final EventHandler<T>  COUNT_DOWN  = e -> count--;

    private EventType<T>  countUpEvent;
    private EventType<T>  countDownEvent;

    public FixedCountTrigger(EventType<T> countUpEvent, EventType<T> countDownEvent, int target) {
        this.countUpEvent   = countUpEvent;
        this.countDownEvent = countDownEvent;
        this.target = target;
        this.count  = 0;
    }

    @Override
    public void activate(EventBus bus) {
        if (countUpEvent   != null) bus.addEventHandler(countUpEvent,   COUNT_UP);
        if (countDownEvent != null) bus.addEventHandler(countDownEvent, COUNT_DOWN);
    }

    @Override
    public void deactivate(EventBus bus) {
        if (countUpEvent   != null) bus.removeEventHandler(countUpEvent,   COUNT_UP);
        if (countDownEvent != null) bus.removeEventHandler(countDownEvent, COUNT_DOWN);
    }

    @Override
    public boolean isTriggered() {
        return count >= target;
    }
}
