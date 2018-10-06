package main.achivement;

import javafx.event.EventHandler;
import main.events.EventBus;
import main.events.SwitchEvent;

/**
 * Class which tracks the "all switches activated" game objective
 */
public class AllSwitchesOnAchievement extends Achievement{

    private int numSwitches = 0;
    private int numActive = 0;

    private final EventHandler<SwitchEvent> SWITCH_CREATED      = e -> numSwitches++;
    private final EventHandler<SwitchEvent> SWITCH_DESTROYED    = e -> numSwitches--;
    private final EventHandler<SwitchEvent> SWITCH_ACTIVATED    = e -> numActive++;
    private final EventHandler<SwitchEvent> SWITCH_DEACTIVATED  = e -> numActive--;


    @Override
    public void activate(EventBus bus) {
        bus.addEventHandler(SwitchEvent.SWITCH_CREATED,     SWITCH_CREATED);
        bus.addEventHandler(SwitchEvent.SWITCH_DESTROYED,   SWITCH_DESTROYED);
        bus.addEventHandler(SwitchEvent.SWITCH_ACTIVATED,   SWITCH_ACTIVATED);
        bus.addEventHandler(SwitchEvent.SWITCH_DEACTIVATED, SWITCH_DEACTIVATED);
    }

    @Override
    public void deactivate(EventBus bus) {
        bus.removeEventHandler(SwitchEvent.SWITCH_CREATED,     SWITCH_CREATED);
        bus.removeEventHandler(SwitchEvent.SWITCH_DESTROYED,   SWITCH_DESTROYED);
        bus.removeEventHandler(SwitchEvent.SWITCH_ACTIVATED,   SWITCH_ACTIVATED);
        bus.removeEventHandler(SwitchEvent.SWITCH_DEACTIVATED, SWITCH_DEACTIVATED);
    }

    public boolean isCompleted() {
        System.out.println("Switches: " + numSwitches + " numActive: " + numActive);
        return numSwitches == numActive;
    }
}
