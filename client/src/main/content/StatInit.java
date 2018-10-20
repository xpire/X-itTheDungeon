package main.content;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import main.events.*;

import java.util.function.Predicate;

public class StatInit {

    private EventBus playModeBus;
    private IntStat stats;

    public StatInit(EventBus playModeBus, IntStat stats) {
        this.playModeBus = playModeBus;
        this.stats = stats;
    }

    public void init() {
        addCounter(DeathEvent.DEATH_BY_FALL, IntStat.Key.NUM_PIT_DEATHS, DeathEvent::isAvatar);
        addCounter(DeathEvent.DEATH_BY_BOMB, IntStat.Key.NUM_BOMB_DEATHS, DeathEvent::isAvatar);

        addCounter(LevelEvent.LEVEL_PASSED, IntStat.Key.MAX_LEVEL_CONQUERED,
                e -> e.getLevel() > stats.get(IntStat.Key.MAX_LEVEL_CONQUERED));

        addCounter(EnemyEvent.ENEMY_KILLED,             IntStat.Key.NUM_ENEMIES_KILLED);
        addCounter(TreasureEvent.TREASURE_COLLECTED,    IntStat.Key.NUM_TREASURES_COLLECTED);
        addCounter(BoulderEvent.BOULDER_BOMBED,         IntStat.Key.NUM_BOULDERS_BOMBED);

        addCounter(DeathEvent.DEATH_BY_ARROW,   IntStat.Key.NUM_ENEMIES_KILLED_WITH_ARROW,      e -> !e.isAvatar());
        addCounter(DeathEvent.DEATH_BY_BOMB,    IntStat.Key.NUM_ENEMIES_KILLED_WITH_BOMB,       e -> !e.isAvatar());
        addCounter(DeathEvent.DEATH_BY_ATTACK,  IntStat.Key.NUM_ENEMIES_KILLED_WHEN_INVINCIBLE, e -> !e.isAvatar());

        addCounter(DoorEvent.DOOR_OPENED,       IntStat.Key.NUM_DOORS_UNLOCKED);


    }

    private <T extends Event> void add(EventType<T> type, EventHandler<T> handler) {
        playModeBus.addEventHandler(type, handler);
    }

    private <T extends Event> void addCounter(EventType<T> type, IntStat.Key stat) {
        playModeBus.addEventHandler(type, e -> stats.increment(stat));
    }

    private <T extends Event> void addCounter(EventType<T> type, IntStat.Key stat, Predicate<T> condition) {
        playModeBus.addEventHandler(type, e -> {
            if (condition.test(e))
                stats.increment(stat);
        });
    }
}
