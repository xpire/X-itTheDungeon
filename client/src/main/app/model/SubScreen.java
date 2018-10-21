package main.app.model;

import javafx.stage.Stage;

/**
 * Abstract class of a subscreen; a screen which keeps track of its parent
 */
public abstract class SubScreen extends AppScreen{

    private AppScreen parent;

    /**
     * Generic constructor
     * @param stage : corresponding stage
     * @param parent : parent screen
     */
    public SubScreen(Stage stage, AppScreen parent) {
        super(stage);
        this.parent = parent;
    }

    /**
     * Getter for the parent
     * @return
     */
    public AppScreen getParent() {
        return parent;
    }
}
