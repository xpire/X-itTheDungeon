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
    private int value;
    private ReadOnlyBooleanWrapper isAchieved = new ReadOnlyBooleanWrapper(false);

    /**
     * Generic constructor
     * @param name name of achievement
     * @param description : achievement description
     * @param target : target to be met
     * @param value : current progress
     */
    public Achievement(String name, String description, int target, int value) {
        this.name = name;
        this.description = description;
        this.target = target;
        this.value  = value;
        this.isAchieved.set(value >= target);
    }

    /**
     * Action when achievement progress is made
     * @param value : value added
     */
    public void onUpdate(int value) {
        this.value = value;

        if (isAchieved.get()) return;

        if (value >= target) {
            isAchieved.set(true);
            System.out.println("Achievement Unlocked: " + name);
        }
    }

    /**
     * Getter for the achievement name
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Check if achievement is met
     * @return true if achieved
     */
    public ReadOnlyBooleanProperty isAchieved() {
        return isAchieved.getReadOnlyProperty();
    }

    /**
     * Getter for current progress
     * @return current progress
     */
    public int getValue() {
        return value;
    }

    /**
     * Getter for the achievement target
     * @return the target
     */
    public int getTarget() {
        return target;
    }

    @Override
    public String toString() {
        return String.format("%s: %s [%d/%d]\n", name, description, value, target);
    }
}
