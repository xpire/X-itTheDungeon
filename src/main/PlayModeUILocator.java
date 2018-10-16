package main;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;
import main.app.engine.Game;
import main.app.engine.GameLoop;
import main.app.engine.Input;
import main.app.engine.UserAction;
import main.component.ViewComponent;
import main.entities.Avatar;
import main.entities.Entity;
import main.entities.enemies.EnemyManager;
import main.events.AvatarDeathEvent;
import main.events.AvatarEvent;
import main.events.LevelEvent;
import main.maploading.MapLoader;

public class PlayModeUILocator {


    // Inventory
    private Group invSword;
    private Group invArrow;
    private Group invBomb;
    private Group invKey;
    private Group invGold;

    private PlayModeUILocator(Builder builder) {
        invSword    = builder.invSword;
        invArrow    = builder.invArrow;
        invBomb     = builder.invBomb;
        invKey      = builder.invKey;
        invGold     = builder.invGold;
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

    public static class Builder {

        private Group invSword;
        private Group invArrow;
        private Group invBomb;
        private Group invKey;
        private Group invGold;

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

        public PlayModeUILocator build() {
            return new PlayModeUILocator(this);
        }
    }
}
