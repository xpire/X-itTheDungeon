package main.trigger.objective;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import main.events.EventBus;
import main.trigger.Trigger;

/**
 * Class which tracks the "all switches activated" game objective
 */
public class FixedCountTrigger extends Trigger {

    /**
    TODO type change required
    **/
    private int target;
    private int count;

    private final EventHandler<Event>  COUNT_UP    = e -> count++;
    private final EventHandler<Event>  COUNT_DOWN  = e -> count--;

    private EventType<Event>  countUpEvent;
    private EventType<Event>  countDownEvent;

    public FixedCountTrigger(EventType<Event> countUpEvent, EventType<Event> countDownEvent, int target) {
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
