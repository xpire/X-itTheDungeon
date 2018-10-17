package test;

import main.entities.Avatar;
import main.entities.enemies.Coward;
import main.entities.pickup.Bomb;
import main.entities.prop.LitBomb;
import main.Level;
import main.maploading.MapLoader;
import main.math.Vec2i;
import org.junit.Test;

import static org.junit.Assert.*;

public class BombTest {

    @Test
    public void avatarPlacesBomb() {
        MapLoader mapLoader = new MapLoader();
        Level level = mapLoader.loadLevel("TestBomb1", "../testdata/");
        //TEST1 - RUN AWAY FROM PLAYER
        Vec2i cowardLocation = new Vec2i(2, 5);
        Vec2i avatarLocation = new Vec2i(3, 3);
        Vec2i bombLocation = new Vec2i(2, 2);
        level.setAvatar(avatarLocation, new Avatar(level, avatarLocation));
        level.setPickup(bombLocation, new Bomb(level, bombLocation));
        Coward coward = new Coward(level, cowardLocation);
        level.setEnemy(cowardLocation, coward);

        level.displayLevel();
        System.out.println(level.getAvatar().getNumBombsProperty());
        assertEquals((int) level.getAvatar().getNumBombsProperty().getValue(), 0);

        level.moveAvatar(bombLocation);
        assertEquals((int) level.getAvatar().getNumBombsProperty().getValue(), 1);
        level.displayLevel();

        level.getAvatar().placeBomb();
        level.displayLevel();

    }

    public void bombExplodes() {
        MapLoader mapLoader = new MapLoader();
        Level level = mapLoader.loadLevel("TestBomb1", "../testdata/");
        //TEST1 - RUN AWAY FROM PLAYER
        Vec2i cowardLocation = new Vec2i(2, 3);
        Vec2i avatarLocation = new Vec2i(3, 3);
        Vec2i bombLocation = new Vec2i(2, 2);
        level.setAvatar(avatarLocation, new Avatar(level, avatarLocation));
        LitBomb litBomb = new LitBomb(level, bombLocation);
        level.setProp(bombLocation, litBomb);
        Coward coward = new Coward(level, cowardLocation);
        level.setEnemy(cowardLocation, coward);

        level.displayLevel();
        litBomb.onTurnUpdate();
        litBomb.onTurnUpdate();
        litBomb.onTurnUpdate();
        litBomb.onTurnUpdate();
        litBomb.onTurnUpdate();


        level.displayLevel();

    }

}