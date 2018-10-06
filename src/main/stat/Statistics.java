package main.stat;

import java.util.HashMap;
import java.util.function.Consumer;

public class Statistics {

    private HashMap<StatisticType, IntegerStat> stats = new HashMap<>();

    public void addStat(StatisticType type, IntegerStat stat) {
        stats.put(type, stat);
    }

    public IntegerStat getStat(StatisticType type) {
        return stats.get(type);
    }

    public void increment(StatisticType type) {
        stats.get(type).increment();
    }

    public void addListener(StatisticType type, Consumer<Integer> listener) {
        stats.get(type).addListener(listener);
    }
}
