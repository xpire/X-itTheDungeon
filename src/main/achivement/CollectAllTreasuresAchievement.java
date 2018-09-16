package main.achivement;

import javafx.event.EventHandler;
import main.events.EventBus;
import main.events.TreasureEvent;

public class CollectAllTreasuresAchievement extends Achievement{

    private int numTreasures = 0;
    private int numCollected = 0;

    private final EventHandler<TreasureEvent> TREASURE_CREATED      = e -> numTreasures++;
    private final EventHandler<TreasureEvent> TREASURE_COLLECTED    = e -> numCollected++;

    @Override
    public void activate(EventBus bus) {
        bus.addEventHandler(TreasureEvent.TREASURE_CREATED,     TREASURE_CREATED);
        bus.addEventHandler(TreasureEvent.TREASURE_COLLECTED,   TREASURE_COLLECTED);
    }

    @Override
    public void deactivate(EventBus bus) {
        bus.removeEventHandler(TreasureEvent.TREASURE_CREATED,     TREASURE_CREATED);
        bus.removeEventHandler(TreasureEvent.TREASURE_COLLECTED,   TREASURE_COLLECTED);
    }

    public boolean isCompleted() {
        return numTreasures == numCollected;
    }
}
