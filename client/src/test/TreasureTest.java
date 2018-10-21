package test;

import main.entities.Avatar;
import main.entities.enemies.Coward;
import main.entities.pickup.Treasure;
import main.Level;
import main.maploading.MapLoader;
import main.math.Vec2i;
import org.junit.Test;

import static org.junit.Assert.*;

public class TreasureTest {

    /*
    can pick up
    can shoot in the right direction
    Treasures go through doors,
     */

    @Test
    public void avatarHoldTreasures() {
        MapLoader mapLoader = new MapLoader();
        Level level = mapLoader.loadLevel("TestTreasure1", "../testdata/");
        Vec2i TreasureLocation = new Vec2i(2, 2);
        Vec2i avatarLocation = new Vec2i(2, 3);
//        level.setAvatar(avatarLocation, new Avatar(level, avatarLocation));
//        level.setPickup(TreasureLocation, new Treasure(level, TreasureLocation));
        level.displayLevel();
        System.out.println(level.getAvatar().getNumTreasuresProperty());
        assertEquals((int) level.getAvatar().getNumTreasuresProperty().getValue(), 0);

        level.moveAvatar(TreasureLocation);
        assertEquals((int) level.getAvatar().getNumTreasuresProperty().getValue(), 1);
    }


    @Test
    public void avatarShootTreasuresHitEnemy() {
        MapLoader mapLoader = new MapLoader();
        Level level = mapLoader.loadLevel("TestTreasure1", "../testdata/");
        //TEST1 - RUN AWAY FROM PLAYER
        Vec2i cowardLocation = new Vec2i(2, 5);
        Vec2i avatarLocation = new Vec2i(3, 3);
        Vec2i TreasureLocation = new Vec2i(2, 2);
//        level.setAvatar(avatarLocation, new Avatar(level, avatarLocation));
//        level.setPickup(TreasureLocation, new Treasure(level, TreasureLocation));
//        Coward coward = new Coward(level, cowardLocation);
//        level.setEnemy(cowardLocation, coward);

        level.displayLevel();
        System.out.println(level.getAvatar().getNumTreasuresProperty());
        assertEquals((int) level.getAvatar().getNumTreasuresProperty().getValue(), 0);

        level.moveAvatar(TreasureLocation);
        assertEquals((int) level.getAvatar().getNumTreasuresProperty().getValue(), 1);
        level.displayLevel();

//        level.getAvatar().shootTreasure();
//        level.displayLevel();



    }



}