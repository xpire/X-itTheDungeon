package main.enemies;

import main.behaviour.AIBehaviour;
import main.behaviour.HunterBehaviour;
import main.maploading.MapLoader;
import main.math.Vec2i;
import main.maploading.Level;

import java.util.ArrayList;

public class Hunter extends Enemy implements stateDecision {
    public Hunter(String name, Level map, Vec2i pos) {
        super(name, map, pos, true);
        super.setCurrBehavior(new HunterBehaviour());
        super.setManager(null);
    }

    @Override
    public void decideBehaviour(Level map) {
        // Do nothing
    }

    @Override
    public boolean IsHunter() {
        return true;
    }

    // Testing code for A* search
    public static void main(String[] args) {
        MapLoader ML = new MapLoader();
        Level map = ML.getTileMap("C:\\Users\\Justin Or\\IdeaProjects\\ecksDee\\group-ecksDee\\src\\main\\levels\\map2.txt");
        Enemy testHunter = new Hunter("Hunter", map, new Vec2i(4,4));

        testHunter.printMap(map);
        System.out.println(map.getNRows() + "\t" + map.getNCols());

//        testHunter.setCurrBehavior(new HunterBehaviour());
        // Print map
        //Code to print map should be here

//        ArrayList<Vec2i> target = new ArrayList<>();
//        target.add(new Vec2i(0,0));
//        target.add(new Vec2i(4,0));
        ArrayList<Vec2i> target = testHunter.getCurrBehavior().decideMove(
                map,
                new Vec2i(4,4),
                new Vec2i(0,0),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        );
        ArrayList<Vec2i> path = testHunter.shortestPath(target);

        for (Vec2i x: path) {
            System.out.printf("(%d, %d)\n",x.getX(),x.getY());
        }
    }
}