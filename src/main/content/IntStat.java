package main.content;


import javafx.beans.property.SimpleIntegerProperty;

import java.util.function.Consumer;

public class IntStat extends GameStat<IntStat.Key, SimpleIntegerProperty> {

    public IntStat() {
        super(Key.class);
        for (Key key : Key.values())
            stats.put(key, new SimpleIntegerProperty());
    }

    public void add(Key key, int value) {
        set(key, get(key) + value);
    }

    public void increment(Key key) {
        set(key, get(key) + 1);
    }

    public int get(Key key) {
        return stats.get(key).get();
    }

    public void set(Key key, int value) {
        stats.get(key).set(value);
    }

    public void addListener(Key key, Consumer<Integer> listener) {
        getStat(key).addListener((observable, oldValue, newValue) -> listener.accept((Integer) newValue));
    }

    public enum Key {
        NUM_PIT_DEATHS,
        MAX_LEVEL_CONQUERED,
        NUM_ENEMIES_KILLED,
        NUM_BOSS_KILLED,
        NUM_STARS_COLLECTED,
        NUM_TREASURES_COLLECTED,
        NUM_BOULDERS_BOMBED,
        NUM_ENEMIES_KILLED_WITH_BOMB,
        NUM_ENEMIES_KILLED_WITH_ARROW,
        NUM_ENEMIES_KILLED_WHEN_INVINCIBLE,
        NUM_DOORS_UNLOCKED
    }
}
