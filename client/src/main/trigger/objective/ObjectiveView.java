package main.trigger.objective;

import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import main.trigger.Trigger;

import java.util.function.Function;

public class ObjectiveView<T extends TargetCountTrigger> {

    private CheckBox checkBox;

    private T objective;
    private Function<T, String> labelText;

    public ObjectiveView(T objective) {
        this(objective, null);
    }

    public ObjectiveView(T objective, Function<T, String> labelText) {
        this.objective = objective;
        this.labelText = labelText;

        checkBox = new CheckBox();
        checkBox.setTextFill(Color.BLACK);
        checkBox.setFont(Font.font(11));
        update();
        objective.subscribe(this::update);
    }

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

    public void setLabelText(Function<T, String> labelText) {
        this.labelText = labelText;
    }

    public CheckBox getCheckBox() {
        return checkBox;
    }
}
