package main.content;

import java.util.EnumMap;

public class GameStat<T extends Enum<T>, V> {

    protected EnumMap<T, V> stats;

    public GameStat(Class<T> type) {
        stats = new EnumMap<>(type);
    }

    public void setStat(T key, V value) {
        stats.put(key, value);
    }

    public V getStat(T key) {
        return stats.get(key);
    }
}
