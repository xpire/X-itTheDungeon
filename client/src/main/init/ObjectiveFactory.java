package main.init;

import main.events.EnemyEvent;
import main.events.ExitEvent;
import main.events.SwitchEvent;
import main.events.TreasureEvent;
import main.trigger.objective.*;

import java.util.EnumMap;
import java.util.function.Function;
import java.util.function.Supplier;


public class ObjectiveFactory {

    private static final EnumMap<Type, Supplier<Objective>> OBJECTIVES = new EnumMap<>(Type.class);

    static {
        registerObjective(
                Type.EXIT,
                () -> new FixedTargetTrigger<>(ExitEvent.EXIT_SUCCESS, null, 1),
                t -> "Exit the Dungeon" + (t.isTriggered() ? " XD" : "")
        );

        registerObjective(
                Type.ACTIVATE_ALL_SWITCHES,
                () -> new DynamicTargetTrigger<>(
                        SwitchEvent.SWITCH_CREATED,
                        SwitchEvent.SWITCH_DESTROYED,
                        SwitchEvent.SWITCH_ACTIVATED,
                        SwitchEvent.SWITCH_DEACTIVATED),
                t -> "Switches Activated: " + t.getCount() + "/" + t.getTarget()
        );

        registerObjective(
                Type.COLLECT_ALL_TREASURES,
                () -> new DynamicTargetTrigger<>(
                        TreasureEvent.TREASURE_CREATED, null,
                        TreasureEvent.TREASURE_COLLECTED, null),
                t -> "Treasures Collected: " + t.getCount() + "/" + t.getTarget()
        );

        registerObjective(
                Type.KILL_ALL_ENEMIES,
                () -> new DynamicTargetTrigger<>(
                        EnemyEvent.ENEMY_CREATED, null,
                        EnemyEvent.ENEMY_KILLED, null),
                t -> "Enemies Killed: " + t.getCount() + "/" + t.getTarget()
        );
    }

    public static Objective makeObjective(Type type) {
        return OBJECTIVES.get(type).get();
    }


    private static <T extends TargetCountTrigger> void registerObjective(
            Type type, Supplier<T> triggerSupplier, Function<T, String> labelText) {

        OBJECTIVES.put(type, () -> {
            T t = triggerSupplier.get();
            return new Objective<>(t, new ObjectiveView<>(t, labelText));
        });
    }

    public enum Type {
        EXIT,
        ACTIVATE_ALL_SWITCHES,
        COLLECT_ALL_TREASURES,
        KILL_ALL_ENEMIES
    }
}
