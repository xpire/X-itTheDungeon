package test;

import main.entities.Avatar;
import main.entities.pickup.HoverPotion;
import main.maploading.Level;
import main.maploading.MapLoader;
import main.math.Vec2i;
import org.junit.Test;

public class HoverPotionTest {

    /*
    can pick up
    can shoot in the right direction
    arrows go through doors,
     */
    @Test
    public void avatarPicksUpHoverPotion() {
        MapLoader mapLoader = new MapLoader();
        Level level = mapLoader.loadLevel("TestHoverPotion", "../testdata/");
        Vec2i avatarLocation = new Vec2i(3, 3);
        Vec2i HoverPotionLocation = new Vec2i(2, 2);
        level.setAvatar(avatarLocation, new Avatar(level, avatarLocation));
        level.setPickup(HoverPotionLocation, new HoverPotion(level, HoverPotionLocation));

        level.displayLevel();
        System.out.println(level.getAvatar().isHovering());

        assert(!level.getAvatar().isHovering());
        level.moveAvatar(HoverPotionLocation);
        System.out.println(level.getAvatar().isHovering());

        assert(level.getAvatar().isHovering());
        level.displayLevel();
    }
}