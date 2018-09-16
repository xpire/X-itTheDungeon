package test;

import javafx.scene.input.KeyCode;
import main.app.Game;
import main.GameWorld;
import main.entities.Avatar;
import main.entities.enemies.Coward;
import main.entities.pickup.Arrow;
import main.entities.pickup.HoverPotion;
import main.maploading.Level;
import main.maploading.MapLoader;
import main.math.Vec2i;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class InvincibilityPotionTest {

    /*
    can pick up
    can shoot in the right direction
    arrows go through doors,
     */
    @Test
    public void avatarPicksUpInvincibilityPotion() {
        MapLoader mapLoader = new MapLoader();
        Level level = mapLoader.loadLevel("TestInvincibilityPotion", "../testdata/");
        Vec2i avatarLocation = new Vec2i(3, 3);
        Vec2i InvincibilityPotionLocation = new Vec2i(2, 2);
        level.setAvatar(avatarLocation, new Avatar(level, avatarLocation));
        level.setPickup(InvincibilityPotionLocation, new HoverPotion(level, InvincibilityPotionLocation));

        level.displayLevel();
        System.out.println(level.getAvatar().isOnRage());
        assert(!level.getAvatar().isOnRage());
        level.moveAvatar(InvincibilityPotionLocation);
        System.out.println(level.getAvatar().isOnRage());
        assert(level.getAvatar().isOnRage());
        level.displayLevel();
    }
}