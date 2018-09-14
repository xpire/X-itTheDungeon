package main.enemies;

import main.behaviour.HunterBehaviour;
import main.maploading.MapLoader;
import main.maploading.TileMap;
import main.math.Vec2i;

import java.util.ArrayList;

public class Hunter extends Enemy {
    public Hunter(TileMap map, Vec2i userLocation, Vec2i currLocation) {
        super.setCurrLocation(currLocation);
        super.setMap(map);
        super.setPlayerLocation(userLocation);
        super.setCurrBehavior(new HunterBehaviour());
    }

    // Testing code for A* search
    public static void main(String[] args) {
        MapLoader ML = new MapLoader();
        TileMap map = ML.getTileMap("C:\\Users\\Justin Or\\IdeaProjects\\ecksDee\\group-ecksDee\\src\\main\\levels\\map2.txt");

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

        Enemy testHunter = new Hunter(map,new Vec2i(0,0), new Vec2i(4,4));
//        testHunter.setCurrBehavior(new HunterBehaviour());
        // Print map
        //Code to print map should be here

        testHunter.setDimensions(5,5);

//        ArrayList<Vec2i> target = new ArrayList<>();
//        target.add(new Vec2i(0,0));
//        target.add(new Vec2i(4,0));
        ArrayList<Vec2i> target = testHunter.getCurrBehavior().decideMove(map, new Vec2i(4,4), new Vec2i(0,0), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        // Always use an Array list
        ArrayList<Vec2i> path = testHunter.shortestPath(target);

        for (Vec2i x: path) {
            System.out.printf("(%d, %d)\n",x.getX(),x.getY());
        }
    }
}