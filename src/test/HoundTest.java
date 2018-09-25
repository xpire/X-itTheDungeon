package test;

import main.entities.Avatar;
import main.entities.enemies.EnemyManager;
import main.entities.enemies.Hound;
import main.entities.enemies.Hunter;
import main.Level;
import main.maploading.MapLoader;
import main.math.Vec2i;

import java.util.ArrayList;

import static org.junit.Assert.*;

    public class HoundTest {

        @org.junit.Test
        public void decideMoveTestNormalQuadrant() {
            MapLoader mapLoader = new MapLoader();
            Level level = mapLoader.loadLevel("TestHound1", "../testdata/");
            //TEST1 - NO HUNTER
            Vec2i houndLocation = new Vec2i(8, 8);
            Vec2i avatarLocation = new Vec2i(3, 3);
            Vec2i hunterLocation = new Vec2i(4,4);


            ArrayList<Vec2i> expected = new ArrayList<>();
            int[] list = {3, 3, 3, 2, 3, 1, 3, 0, 2, 3, 2, 2, 2, 1, 2, 0, 1, 3, 1, 2, 1, 1, 1, 0, 0, 3, 0, 2, 0, 1, 0, 0};
            for (int j = 0; j < list.length; j +=2) {
                expected.add(new Vec2i(list[j], list[j+1]));
            }

            ArrayList<Vec2i> result = runDecideMove(level,
                    houndLocation,
                    avatarLocation,
                    hunterLocation);
            System.out.println(expected);
            assert (assertArrayLists(expected, result));
        }

        @org.junit.Test
        public void decideMoveTestHorizontalQuadrant() {
            MapLoader mapLoader = new MapLoader();
            Level level = mapLoader.loadLevel("TestHound1", "../testdata/");
            //TEST1 - NO HUNTER
            Vec2i houndLocation = new Vec2i(3, 0);
            Vec2i avatarLocation = new Vec2i(3, 3);
            Vec2i hunterLocation = new Vec2i(4,3);

            ArrayList<Vec2i> expected = new ArrayList<>();
            int[] list = {3, 3, 2, 2, 2, 3, 2, 4, 1, 2, 1, 3, 1, 4, 0, 3};
            for (int j = 0; j < list.length; j +=2) {
                expected.add(new Vec2i(list[j], list[j+1]));
            }

            ArrayList<Vec2i> result = runDecideMove(level,
                    houndLocation,
                    avatarLocation,
                    hunterLocation);
            System.out.println(expected);
            assert (assertArrayLists(expected, result));
        }

        @org.junit.Test
        public void decideMoveTestVerticalQuadrant() {
            MapLoader mapLoader = new MapLoader();
            Level level = mapLoader.loadLevel("TestHound1", "../testdata/");
            //TEST1 - NO HUNTER
            Vec2i houndLocation = new Vec2i(3, 0);
            Vec2i avatarLocation = new Vec2i(3, 3);
            Vec2i hunterLocation = new Vec2i(3,4);

            ArrayList<Vec2i> expected = new ArrayList<>();
            int[] list = {3, 3, 2, 2, 3, 2, 4, 2, 2, 1, 3, 1, 4, 1};
            for (int j = 0; j < list.length; j +=2) {
                expected.add(new Vec2i(list[j], list[j+1]));
            }

            ArrayList<Vec2i> result = runDecideMove(level,
                    houndLocation,
                    avatarLocation,
                    hunterLocation);
            System.out.println(expected);
            assert (assertArrayLists(expected, result));
        }

        public ArrayList<Vec2i> runDecideMove(Level level, Vec2i houndLocation, Vec2i avatarLocation, Vec2i hunterLocation) {
            level.setAvatar(avatarLocation, new Avatar(level, avatarLocation));
            EnemyManager enemyManager = new EnemyManager(level);

            Hound hound = new Hound(level, houndLocation);
            hound.setManager(enemyManager);
            level.setEnemy(houndLocation, hound);

            Hunter hunter = new Hunter(level, hunterLocation);
            hunter.setManager(enemyManager);
            level.setEnemy(hunterLocation, hunter);

            level.displayLevel();
            hound.decideBehaviour();
            System.out.println(enemyManager.hunterExist());
            ArrayList<Vec2i> targets =  hound.getCurrBehaviour().decideMove(
                    level,
                    houndLocation,
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
