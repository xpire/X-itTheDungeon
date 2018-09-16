package main.entities.enemies;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import main.behaviour.CowardBehaviour;
import main.behaviour.HunterBehaviour;
import main.maploading.MapLoader;
import main.math.Vec2d;
import main.math.Vec2i;
import main.maploading.Level;

import java.util.ArrayList;

public class Hunter extends Enemy {

    {
        isHunter = true;
        symbol = '1';
    }

    public Hunter(Level level) {
        super(level);
        super.setCurrBehavior(new HunterBehaviour());
        super.setManager(null);
    }

    public Hunter(Level level, Vec2i pos) {
        super(level, pos);
        super.setCurrBehavior(new HunterBehaviour());
        super.setManager(null);
    }


    @Override
    public void decideBehaviour(Level map) {
        if (level.getAvatar().isRaged()) {
            super.setCurrBehavior(new CowardBehaviour());
        } else {
            super.setCurrBehavior(new HunterBehaviour());
        }
    }

    @Override
    public void onCreated(){
        Circle hunt = new Circle();

        hunt.setRadius(10);
        hunt.setFill(Color.RED);

        view.addNode(hunt);
        view.setCentre(new Vec2d(0, 0));
    }

//
//    // Testing code for A* search
//    public static void main(String[] args) {
//        MapLoader ML = new MapLoader();
//
//        Level map = ML.loadLevel("map2", "levels");
//        Enemy testHunter = new Hunter(map, new Vec2i(4,4));
//
//        System.out.println(map.getNRows() + "\t" + map.getNCols());
//
////        testHunter.setCurrBehavior(new HunterBehaviour());
//        // Print map
//        //Code to print map should be here
//
////        ArrayList<Vec2i> target = new ArrayList<>();
////        target.add(new Vec2i(0,0));
////        target.add(new Vec2i(4,0));
//        ArrayList<Vec2i> target = testHunter.getCurrBehavior().decideMove(
//                map,
//                new Vec2i(4,4),
//                new Vec2i(0,0),
//                new ArrayList<>(),
//                new ArrayList<>()
//        );
//        ArrayList<Vec2i> path = testHunter.shortestPath(target);
//
//        for (Vec2i x: path) {
//            System.out.printf("(%d, %d)\n",x.getX(),x.getY());
//        }
//    }
}