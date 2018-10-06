package main.init;

import main.events.AvatarDeathEvent;
import main.events.EnemyEvent;
import main.events.EventBus;
import main.stat.IntegerStat;
import main.stat.StatisticType;
import main.stat.Statistics;

public class StatisticsInitialiser {

    EventBus playModeBus;

    public StatisticsInitialiser(EventBus playModeBus) {
        this.playModeBus = playModeBus;
    }

    public void init(Statistics stats) {
        stats.addStat(StatisticType.NUM_ENEMIES_KILLED,     new IntegerStat()); // TODO CAN BE REFACTORED
        stats.addStat(StatisticType.NUM_TREASURES_COLLECTED, new IntegerStat());
        stats.addStat(StatisticType.NUM_PIT_DEATHS,         new IntegerStat());
        stats.addStat(StatisticType.MAX_LEVEL_CONQUERED,    new IntegerStat());

        playModeBus.addEventHandler(EnemyEvent.ENEMY_KILLED, e -> {
            stats.increment(StatisticType.NUM_ENEMIES_KILLED);
        });

        playModeBus.addEventHandler(AvatarDeathEvent.AVATAR_DEATH, e -> {
            if (e.wasPlummeted()) {
                stats.increment(StatisticType.NUM_PIT_DEATHS);
            }
        });
    }
}
