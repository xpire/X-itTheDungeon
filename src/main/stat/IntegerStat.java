package main.stat;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.function.Consumer;

public class IntegerStat {

    private IntegerProperty value;

    public IntegerStat() {
        this(0);
    }

    public IntegerStat(int defaultValue) {
        value = new SimpleIntegerProperty(defaultValue);
    }

    public void addListener(Consumer<Integer> listener) {
        value.addListener((observable, oldValue, newValue) -> listener.accept((Integer) newValue));
    }

    public int get() {
        return value.get();
    }

    public void add(int increment) {
        value.set(value.get() + increment);
    }

    public void increment() {
        add(1);
    }

    public void set(int newValue) {
        value.set(newValue);
    }
}
