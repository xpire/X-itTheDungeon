package main.trigger.objective;

import javafx.scene.control.CheckBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.function.Function;

/**
 * Class representing the view of the games objectives
 * @param <T> An objective
 */
public class ObjectiveView<T extends TargetCountTrigger> {

    private CheckBox checkBox;

    private T objective;
    private Function<T, String> labelText;

    public ObjectiveView(T objective) {
        this(objective, null);
    }

    /**
     * Generic constructor
     * @param objective : the objective
     * @param labelText : the label of the objective
     */
    public ObjectiveView(T objective, Function<T, String> labelText) {
        this.objective = objective;
        this.labelText = labelText;

        checkBox = new CheckBox();
        checkBox.setTextFill(Color.BLACK);
        checkBox.setFont(Font.font(11));
        checkBox.setFocusTraversable(false);
        checkBox.setMouseTransparent(true);
        update();
        objective.subscribe(this::update);
    }

    /**
     * updates the objective progress
     */
    private void update() {
        if (objective.isTriggered()) {
            checkBox.setSelected(true);
            checkBox.setTextFill(Color.GREEN);
        }
        else {
            checkBox.setSelected(false);
            checkBox.setTextFill(Color.BLACK);
        }

        checkBox.setText(labelText == null ? "" : labelText.apply(objective));
    }


    /**
     * Gets the associated checkbox
     * @return the checkbox
     */
    public CheckBox getCheckBox() {
        return checkBox;
    }
}
