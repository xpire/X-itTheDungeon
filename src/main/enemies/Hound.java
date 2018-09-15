package main.enemies;

import main.behaviour.AIBehaviour;
import main.behaviour.CowardBehaviour;
import main.behaviour.HoundBehaviour;
import main.behaviour.HunterBehaviour;
import main.maploading.MapLoader;
import main.math.Vec2i;
import main.maploading.Level;

import java.util.ArrayList;

public class Hound extends Enemy implements stateDecision {
    public Hound(String name, Level map, Vec2i pos) {
        super(name, map, pos, false);
        super.setCurrBehavior(new HoundBehaviour());
        super.setManager(null);
    }

    @Override
    public void decideBehaviour(Level map) {
        if (super.manager.hunterExist()) {
            super.setCurrBehavior(new HoundBehaviour());
        }
        else {
            super.setCurrBehavior(new HunterBehaviour());
        }
    }

    @Override
    public boolean IsHunter() {
        return false;
    }

    // Testing code for A* search
    public static void main(String[] args) {
        MapLoader ML = new MapLoader();
        Level map = ML.getTileMap("C:\\Users\\Justin Or\\IdeaProjects\\ecksDee\\src\\main\\levels\\map2.txt");

        int nRow = map.getNRows(), nCol = map.getNCols();
        System.out.println(nRow + "\t" + nCol);

        for (int i = 0; i < nRow; i++) {
            for (int j = 0; j < nCol; j++) {
                Vec2i coord = new Vec2i(i, j);
                map.getTile(coord).listEntities();
                System.out.print("\t");
            }
            System.out.println();
        }
        Vec2i hunterLocation = new Vec2i(2,0);
        Vec2i userLocation = new Vec2i(2,2);
        System.out.printf("c:%d %d,u:%d %d\n",hunterLocation.getX(), hunterLocation.getY(), userLocation.getX(), userLocation.getY());
        Enemy testHound = new Hound("Hound", map, hunterLocation);
        char [] [] charMap = new char[5][5];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                charMap[i][j] = '_';
                if (hunterLocation.getX() == j && hunterLocation.getY() == i) charMap[i][j] = 'H';
                if (userLocation.getX() == j && userLocation.getY() == i) charMap[i][j] = 'U';
                System.out.printf("%c ",charMap[i][j]);
            }
            System.out.println();
        }

        ArrayList<Vec2i> target = testHound.getCurrBehavior().decideMove(map, hunterLocation, userLocation, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        // Always use an Array list
//        ArrayList<Vec2i> path = testHound.shortestPath(target);

//        for (Vec2i x: path) {
//            System.out.printf("(%d, %d)\n",x.getX(),x.getY());
//        }
        for (Vec2i v: target) {
            charMap[v.getY()][v.getX()] = '1';
        }
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                System.out.printf("%c ",charMap[i][j]);
            }
            System.out.println();
        }
    }
}
