package main;

import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;

import java.util.function.Predicate;

public class InventoryView {

    private ImageView view;
    private Label label;

    public InventoryView(ImageView view, Label label) {
        this.view  = view;
        this.label = label == null ? new Label() : label;
    }

    public void deactivate() {
        view.setEffect(new ColorAdjust(0, -0.9, -0.5, -0.5));
        label.setVisible(false);
    }

    public void activate() {
        view.setEffect(null);
        label.setVisible(true);
    }

    public void bindBoolean(ObservableValue<Boolean> value) {
        value.addListener((o, wasActive, isActive) -> {
            update(isActive);
        });
        update(value.getValue());
    }

    public void bindInteger(ObservableValue<Integer> value) {
        bindCountText(value);
        value.addListener((o, prevCount, count) -> {
            update(value.getValue() > 0);
        });
        update(value.getValue() > 0);
    }

    public void bindCountText(ObservableValue<Integer> value) {
        label.textProperty().bind(Bindings.format("%d", value));
    }

    public void update(boolean isActive) {
        if (isActive)
            activate();
        else
            deactivate();
    }
}
