package test;

import main.Game;
import main.GameWorld;
import main.entities.Avatar;
import main.entities.enemies.Coward;
import main.entities.pickup.Arrow;
import main.entities.pickup.Sword;
import main.maploading.Level;
import main.maploading.MapLoader;
import main.math.Vec2i;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class SwordTest {

    /*
    can pick up
    can shoot in the right direction
    arrows go through doors,
     */

    @Test
    public void avatarHoldSwords() {
        MapLoader mapLoader = new MapLoader();
        Level level = mapLoader.loadLevel("TestSword1", "../testdata/");
        Vec2i swordLocation = new Vec2i(2, 2);
        Vec2i avatarLocation = new Vec2i(2, 3);
        Sword sword = new Sword(level, swordLocation);
        level.setAvatar(avatarLocation, new Avatar(level, avatarLocation));
        level.setPickup(swordLocation, sword);
        level.displayLevel();
        assert(!level.getAvatar().hasSword());

        level.moveAvatar(swordLocation);

        assert(level.getAvatar().hasSword());
    }


}