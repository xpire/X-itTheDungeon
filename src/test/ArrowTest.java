package test;

import main.entities.Avatar;
import main.entities.enemies.Coward;
import main.entities.pickup.Arrow;
import main.maploading.Level;
import main.maploading.MapLoader;
import main.math.Vec2i;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class ArrowTest {

    /*
    can pick up
    can shoot in the right direction
    arrows go through doors,
     */

    @Test
    public void avatarHoldArrows() {
        MapLoader mapLoader = new MapLoader();
        Level level = mapLoader.loadLevel("TestArrow1", "../testdata/");
        //TEST1 - RUN AWAY FROM PLAYER
        Vec2i arrowLocation = new Vec2i(2, 2);
        Vec2i avatarLocation = new Vec2i(3, 3);
        level.setAvatar(avatarLocation, new Avatar(level, avatarLocation));
        level.setPickup(arrowLocation, new Arrow(level, arrowLocation));
        level.displayLevel();
    }


    @Test
    public void avatarShootArrowsHitEnemy() {
        MapLoader mapLoader = new MapLoader();
        Level level = mapLoader.loadLevel("TestArrow1", "../testdata/");
        //TEST1 - RUN AWAY FROM PLAYER
        Vec2i cowardLocation = new Vec2i(2, 2);
        Vec2i avatarLocation = new Vec2i(3, 3);

        ArrayList<Vec2i> expected = new ArrayList<>();
        expected.add(new Vec2i(1, 2));
        expected.add(new Vec2i(2, 1));
        level.setAvatar(avatarLocation, new Avatar(level, avatarLocation));
        Coward coward = new Coward(level, cowardLocation);
        level.setEnemy(cowardLocation, coward);
        level.displayLevel();
    }



}