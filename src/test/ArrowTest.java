package test;

import javafx.scene.input.KeyCode;
import main.app.Game;
import main.GameWorld;
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
        Vec2i arrowLocation = new Vec2i(2, 2);
        Vec2i avatarLocation = new Vec2i(2, 3);
        level.setAvatar(avatarLocation, new Avatar(level, avatarLocation));
        level.setPickup(arrowLocation, new Arrow(level, arrowLocation));
        level.displayLevel();
        System.out.println(level.getAvatar().getNumArrowsProperty());
        assertEquals((int) level.getAvatar().getNumArrowsProperty().getValue(), 0);

        level.moveAvatar(arrowLocation);
        assertEquals((int) level.getAvatar().getNumArrowsProperty().getValue(), 1);
    }


    @Test
    public void avatarShootArrowsHitEnemy() {
        MapLoader mapLoader = new MapLoader();
        Level level = mapLoader.loadLevel("TestArrow1", "../testdata/");
        //TEST1 - RUN AWAY FROM PLAYER
        Vec2i cowardLocation = new Vec2i(2, 5);
        Vec2i avatarLocation = new Vec2i(3, 3);
        Vec2i arrowLocation = new Vec2i(2, 2);
        level.setAvatar(avatarLocation, new Avatar(level, avatarLocation));
        level.setPickup(arrowLocation, new Arrow(level, arrowLocation));
        Coward coward = new Coward(level, cowardLocation);
        level.setEnemy(cowardLocation, coward);

        level.displayLevel();
        System.out.println(level.getAvatar().getNumArrowsProperty());
        assertEquals((int) level.getAvatar().getNumArrowsProperty().getValue(), 0);

        level.moveAvatar(arrowLocation);
        assertEquals((int) level.getAvatar().getNumArrowsProperty().getValue(), 1);
        level.displayLevel();

        level.getAvatar().shootArrow();
        level.displayLevel();



    }



}