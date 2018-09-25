package test;

import main.entities.Avatar;
import main.entities.enemies.Coward;
import main.Level;
import main.maploading.MapLoader;
import main.math.Vec2i;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class CowardTest {


    @org.junit.Test
    public void decideMoveTestRunTowards() {
        MapLoader mapLoader = new MapLoader();
        Level level = mapLoader.loadLevel("TestCoward1", "../testdata/");
        //TEST1 - RUN AWAY FROM PLAYER
        Vec2i cowardLocation = new Vec2i(2, 2);
        Vec2i avatarLocation = new Vec2i(3, 3);

        ArrayList<Vec2i> expected = new ArrayList<>();
        expected.add(new Vec2i(1, 2));
        expected.add(new Vec2i(2, 1));

        ArrayList<Vec2i> result = runDecideMove(level, cowardLocation, avatarLocation);
        assert (assertArrayLists(expected, result));
    }

    @org.junit.Test
    public void decideMoveTestRunAway() {
        MapLoader mapLoader = new MapLoader();
        Level level = mapLoader.loadLevel("TestCoward1", "../testdata/");
        //TEST2 - RUN TOWARDS PLAYER
        Vec2i cowardLocation = new Vec2i(8, 8);
        Vec2i avatarLocation = new Vec2i(3, 3);

        ArrayList<Vec2i> expected = new ArrayList<>();
        expected.add(new Vec2i(3, 3));

        ArrayList<Vec2i> result = runDecideMove(level, cowardLocation, avatarLocation);
        assert (assertArrayLists(expected, result));
    }

    @org.junit.Test
    public void decideMoveTestNoMove() {
        MapLoader mapLoader = new MapLoader();
        Level level = mapLoader.loadLevel("TestCoward1", "../testdata/");
        //TEST3 NO POSSIBLE MOVE
        Vec2i cowardLocation = new Vec2i(0, 0);
        Vec2i avatarLocation = new Vec2i(1, 1);

        ArrayList<Vec2i> expected = new ArrayList<>();

        ArrayList<Vec2i> result = runDecideMove(level, cowardLocation, avatarLocation);
        assert (assertArrayLists(expected, result));
    }

    public ArrayList<Vec2i> runDecideMove(Level level, Vec2i cowardLocation, Vec2i avatarLocation) {
        level.setAvatar(avatarLocation, new Avatar(level, avatarLocation));
        Coward coward = new Coward(level, cowardLocation);
        level.setEnemy(cowardLocation, coward);
        level.displayLevel();
        coward.decideBehaviour();
        ArrayList<Vec2i> targets = coward.getCurrBehaviour().decideMove(
                level,
                cowardLocation,
                avatarLocation,
                new ArrayList<>(),
                new ArrayList<>()
        );
        System.out.println(targets);
        return targets;
    }

    public Boolean assertArrayLists(ArrayList<Vec2i> expected, ArrayList<Vec2i> result) {
        if (expected.size() != result.size()) return false;
        for (int i = 0; i < expected.size(); i++) {
            Vec2i e = expected.get(i);
            Vec2i t = result.get(i);
            assertEquals(e.getX(), t.getX());
            assertEquals(e.getY(), t.getY());
        }
        return true;

    }
}

