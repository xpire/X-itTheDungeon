package main;

import javafx.scene.Group;
import javafx.scene.layout.VBox;

public class PlayModeUILocator {

    // Inventory
    private Group invSword;
    private Group invArrow;
    private Group invBomb;
    private Group invKey;
    private Group invGold;

    private VBox objectivesPanel;

    private PlayModeUILocator(Builder builder) {
        invSword    = builder.invSword;
        invArrow    = builder.invArrow;
        invBomb     = builder.invBomb;
        invKey      = builder.invKey;
        invGold     = builder.invGold;
        objectivesPanel = builder.objectivesPanel;
    }

    public Group getInvSword() {
        return invSword;
    }

    public Group getInvArrow() {
        return invArrow;
    }

    public Group getInvBomb() {
        return invBomb;
    }

    public Group getInvKey() {
        return invKey;
    }

    public Group getInvGold() {
        return invGold;
    }

    public VBox getObjectivesPanel() {
        return objectivesPanel;
    }

    public static class Builder {

        private Group invSword;
        private Group invArrow;
        private Group invBomb;
        private Group invKey;
        private Group invGold;

        private VBox objectivesPanel;

        public Builder sword(Group sword) {
            invSword = sword;
            return this;
        }

        public Builder arrow(Group arrow) {
            invArrow = arrow;
            return this;
        }

        public Builder bomb(Group bomb) {
            invBomb = bomb;
            return this;
        }

        public Builder key(Group key) {
            invKey = key;
            return this;
        }

        public Builder gold(Group gold) {
            invGold = gold;
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
