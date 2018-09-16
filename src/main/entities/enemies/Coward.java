package main.entities.enemies;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import main.behaviour.CowardBehaviour;
import main.behaviour.HunterBehaviour;
import main.maploading.MapLoader;
import main.math.Vec2d;
import main.math.Vec2i;
import main.maploading.Level;

import java.util.ArrayList;

public class Coward extends Enemy {

    {
        symbol = '4';
        isHunter = false;
    }

    public Coward(Level map) {
        super(map);
        super.setCurrBehavior(new CowardBehaviour());
        super.setManager(null);
    }

    public Coward(Level map, Vec2i pos) {
        super(map, pos);
        super.setCurrBehavior(new CowardBehaviour());
        super.setManager(null);
    }

    @Override
    public void decideBehaviour(Level map) {
        if (pos.mDist(level.getAvatar().getGridPos()) < 4 || level.getAvatar().isRaged()) {
            super.setCurrBehavior(new CowardBehaviour());
        }
        else {
            super.setCurrBehavior(new HunterBehaviour());
        }
    }

    @Override
    public void onCreated(){
        Circle hunt = new Circle();

        hunt.setRadius(10);
        hunt.setFill(Color.GREEN);

        view.addNode(hunt);
        view.setCentre(new Vec2d(0, 0));
    }

    public static void main(String[] args) {
        MapLoader ML = new MapLoader();
        Level map = ML.loadLevel("map2", "levels");

        int nRow = map.getNRows(), nCol = map.getNCols();
        System.out.println(nRow + "\t" + nCol);

        Vec2i cowardLocation = new Vec2i(2,1);
        Vec2i userLocation = new Vec2i(2,2);
        System.out.printf(
                "c:%d %d,u:%d %d\n",
                cowardLocation.getX(),
                cowardLocation.getY(),
                userLocation.getX(),
                userLocation.getY()
        );
        Enemy testCoward = new Coward(map, cowardLocation);
        char [] [] charMap = new char[5][5];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                charMap[i][j] = '_';
                if (cowardLocation.getX() == j && cowardLocation.getY() == i) charMap[i][j] = 'C';
                if (userLocation.getX() == j && userLocation.getY() == i) charMap[i][j] = 'U';
                System.out.printf("%c ",charMap[i][j]);
            }
            System.out.println();
        }
        System.out.println("help");
        ArrayList<Vec2i> target = testCoward.getCurrBehavior().decideMove(
                map,
                cowardLocation,
                userLocation,
                new ArrayList<>(),
                new ArrayList<>()
        );
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
