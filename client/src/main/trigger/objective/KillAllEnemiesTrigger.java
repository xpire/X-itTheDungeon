package main.trigger.objective;

import javafx.event.EventHandler;
import main.events.EnemyEvent;
import main.events.EventBus;
import main.trigger.Trigger;

/**
 * Class which tracks the "Kill all enemies" game objective
 */
public class KillAllEnemiesTrigger extends Trigger {

    private int numEnemies = 0;
    private int numKilled = 0;

    private final EventHandler<EnemyEvent> ENEMY_CREATED = e -> numEnemies++;
    private final EventHandler<EnemyEvent> ENEMY_KILLED = e -> numKilled++;

    @Override
    public void activate(EventBus bus) {
        bus.addEventHandler(EnemyEvent.ENEMY_CREATED,  ENEMY_CREATED);
        bus.addEventHandler(EnemyEvent.ENEMY_KILLED,   ENEMY_KILLED);
    }

    @Override
    public void deactivate(EventBus bus) {
        bus.removeEventHandler(EnemyEvent.ENEMY_CREATED, ENEMY_CREATED);
        bus.removeEventHandler(EnemyEvent.ENEMY_KILLED, ENEMY_KILLED);
    }

    @Override
    public boolean isTriggered() {
        return numEnemies == numKilled;
    }
}
