package main;

import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class PlayModeUILocator {

    // Inventory
    private InventoryView invSword;
    private InventoryView invArrow;
    private InventoryView invBomb;
    private InventoryView invKey;
    private InventoryView invGold;
    private Label lblTitle;
    private Label lblSubtitle;

    private VBox objectivesPanel;

    private PlayModeUILocator(Builder builder) {
        invSword    = builder.invSword;
        invArrow    = builder.invArrow;
        invBomb     = builder.invBomb;
        invKey      = builder.invKey;
        invGold     = builder.invGold;
        lblTitle    = builder.lblTitle;
        lblSubtitle = builder.lblSubtitle;
        objectivesPanel = builder.objectivesPanel;
    }

    public InventoryView getInvSword() {
        return invSword;
    }

    public InventoryView getInvArrow() {
        return invArrow;
    }

    public InventoryView getInvBomb() {
        return invBomb;
    }

    public InventoryView getInvKey() {
        return invKey;
    }

    public InventoryView getInvGold() {
        return invGold;
    }

    public Label getLblTitle() {
        return lblTitle;
    }

    public Label getLblSubtitle() {
        return lblSubtitle;
    }


    public VBox getObjectivesPanel() {
        return objectivesPanel;
    }

    public static class Builder {

        private InventoryView invSword;
        private InventoryView invArrow;
        private InventoryView invBomb;
        private InventoryView invKey;
        private InventoryView invGold;
        private Label lblTitle;
        private Label lblSubtitle;

        private VBox objectivesPanel;

        public Builder sword(InventoryView sword) {
            invSword = sword;
            return this;
        }

        public Builder arrow(InventoryView arrow) {
            invArrow = arrow;
            return this;
        }

        public Builder bomb(InventoryView bomb) {
            invBomb = bomb;
            return this;
        }

        public Builder key(InventoryView key) {
            invKey = key;
            return this;
        }

        public Builder gold(InventoryView gold) {
            invGold = gold;
            return this;
        }

        public Builder title(Label title) {
            lblTitle = title;
            return this;
        }

        public Builder subtitle(Label subtitle) {
            lblSubtitle = subtitle;
            return this;
        }

        public Builder objectives(VBox objectivesPanel) {
            this.objectivesPanel = objectivesPanel;
            return this;
        }

        public PlayModeUILocator build() {
            return new PlayModeUILocator(this);
        }
    }
}
