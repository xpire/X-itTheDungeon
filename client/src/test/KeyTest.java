package test;

import main.entities.Avatar;
import main.entities.pickup.Key;
import main.Level;
import main.maploading.MapLoader;
import main.math.Vec2i;
import org.junit.Test;

public class KeyTest {

    /*
    can pick up
    can shoot in the right direction
    arrows go through doors,
     */

    @Test
    public void avatarHoldKeys() {
        MapLoader mapLoader = new MapLoader();
        Level level = mapLoader.loadLevel("TestKey1", "../testdata/");
        Vec2i keyLocation = new Vec2i(2, 2);
        Vec2i avatarLocation = new Vec2i(2, 3);
//        Key key = new Key(level, keyLocation);
//        level.setAvatar(avatarLocation, new Avatar(level, avatarLocation));
//        level.setPickup(keyLocation, key);
        level.displayLevel();
        assert(!level.getAvatar().hasKey());

        level.moveAvatar(keyLocation);

        assert(level.getAvatar().hasKey());
    }


}