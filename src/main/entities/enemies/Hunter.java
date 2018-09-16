package main.entities.enemies;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import main.behaviour.CowardBehaviour;
import main.behaviour.HunterBehaviour;
import main.behaviour.StrategistBehaviour;
import main.maploading.Level;
import main.math.Vec2i;

public class Hunter extends Enemy {

    {
        symbol = '1';
        isHunter = true;
    }

    public Hunter(Level level) {
        super(level);
    }

    public Hunter(Level level, Vec2i pos) {
        super(level, pos);
    }


    @Override
    public void onCreated(){
        super.onCreated();
        view.addNode(new Circle(10, Color.RED));
        setCurrBehaviour(new StrategistBehaviour());
    }


    @Override
    public void decideBehaviour() {
        if (level.getAvatar().isOnRage())
            setCurrBehaviour(new CowardBehaviour());

        else
            setCurrBehaviour(new HunterBehaviour());
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
////        testHunter.setCurrBehaviour(new HunterBehaviour());
//        // Print map
//        //Code to print map should be here
//
////        ArrayList<Vec2i> target = new ArrayList<>();
////        target.add(new Vec2i(0,0));
////        target.add(new Vec2i(4,0));
//        ArrayList<Vec2i> target = testHunter.getCurrBehaviour().decideMove(
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