package main.init;

import main.events.EnemyEvent;
import main.events.ExitEvent;
import main.events.SwitchEvent;
import main.events.TreasureEvent;
import main.trigger.Trigger;
import main.trigger.objective.FixedCountTrigger;
import main.trigger.objective.TargetCountTrigger;

import java.util.EnumMap;
import java.util.function.Supplier;


public class ObjectiveFactory {

    private static final EnumMap<Type, Supplier<Trigger>> OBJECTIVES = new EnumMap<>(Type.class);

    static {
        OBJECTIVES.put(Type.EXIT,
                () -> new FixedCountTrigger<>(
                        ExitEvent.EXIT_SUCCESS, null, 1)
                );

        OBJECTIVES.put(Type.ACTIVATE_ALL_SWITCHES,
                () -> new TargetCountTrigger<>(
                        SwitchEvent.SWITCH_CREATED,
                        SwitchEvent.SWITCH_DESTROYED,
                        SwitchEvent.SWITCH_ACTIVATED,
                        SwitchEvent.SWITCH_DEACTIVATED
                ));

        OBJECTIVES.put(Type.COLLECT_ALL_TREASURES,
                () -> new TargetCountTrigger<>(
                        TreasureEvent.TREASURE_CREATED, null,
                        TreasureEvent.TREASURE_COLLECTED, null
                ));

        OBJECTIVES.put(Type.KILL_ALL_ENEMIES,
                () -> new TargetCountTrigger<>(
                        EnemyEvent.ENEMY_CREATED, null,
                        EnemyEvent.ENEMY_KILLED, null
                ));
    }

    public static Trigger makeObjective(Type type) {
        return OBJECTIVES.get(type).get();
    }

    public enum Type {
        EXIT,
        ACTIVATE_ALL_SWITCHES,
        COLLECT_ALL_TREASURES,
        KILL_ALL_ENEMIES
    }
}
