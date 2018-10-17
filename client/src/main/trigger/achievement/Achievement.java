package main.trigger.achievement;


import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;

/**
 * In-game achievements that are earned when some criteria is met
 */
public class Achievement {

    private String name;
    private String description;

    private int target;
    private ReadOnlyBooleanWrapper isAchieved = new ReadOnlyBooleanWrapper(false);

    public Achievement(String name, String description, int target) {
        this.name = name;
        this.description = description;
        this.target = target;
    }

    public void onUpdate(int value) {
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
}
