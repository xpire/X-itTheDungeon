package main.init;

import main.content.IntStat;
import main.events.AvatarDeathEvent;
import main.events.EnemyEvent;
import main.events.EventBus;
import main.events.LevelEvent;

public class StatInit {

    EventBus playModeBus;

    public StatInit(EventBus playModeBus) {
        this.playModeBus = playModeBus;
    }

    public void init(IntStat stats) {
        playModeBus.addEventHandler(EnemyEvent.ENEMY_KILLED, e -> {
            System.out.println("Killed: " + stats.get(IntStat.Key.NUM_ENEMIES_KILLED));
            stats.increment(IntStat.Key.NUM_ENEMIES_KILLED);
        });

        playModeBus.addEventHandler(AvatarDeathEvent.AVATAR_DEATH, e -> {
            if (e.wasPlummeted()) {
                stats.increment(IntStat.Key.NUM_PIT_DEATHS);
            }
        });

        playModeBus.addEventHandler(LevelEvent.LEVEL_PASSED, e -> {
            System.out.println("Conquered: " + stats.get(IntStat.Key.MAX_LEVEL_CONQUERED));
            if (e.getLevel() > stats.get(IntStat.Key.MAX_LEVEL_CONQUERED)) {
                stats.increment(IntStat.Key.MAX_LEVEL_CONQUERED);
                System.out.println("Conquered: " + stats.get(IntStat.Key.MAX_LEVEL_CONQUERED));
            }
        });
    }
}
