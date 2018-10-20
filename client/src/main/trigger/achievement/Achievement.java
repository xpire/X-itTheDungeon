package main.trigger.achievement;


import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;

/**
 * In-game achievements that are earned when some criteria is met
 */
/*
TODO: REFACTOR AS A TARGET COUNT TRIGGER
 */
public class Achievement {

    private String name;
    private String description;

    private int target;
    private int value;
    private ReadOnlyBooleanWrapper isAchieved = new ReadOnlyBooleanWrapper(false);

    public Achievement(String name, String description, int target, int value) {
        this.name = name;
        this.description = description;
        this.target = target;
        this.value  = value;
        this.isAchieved.set(value >= target);
    }

    public void onUpdate(int value) {
        this.value = value;

        if (isAchieved.get()) return;

        if (value >= target) {
            isAchieved.set(true);
            System.out.println("Achievement Unlocked: " + name);
        }
    }


    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ReadOnlyBooleanProperty isAchieved() {
        return isAchieved.getReadOnlyProperty();
    }

    public int getValue() {
        return value;
    }

    public int getTarget() {
        return target;
    }

    @Override
    public String toString() {
        return String.format("%s: %s [%d/%d]\n", name, description, value, target);
    }
}
