import main.math.Vec2i;

import java.util.ArrayList;

public class Hunter extends Enemy {
    public Hunter(int [] [] map, Vec2i userLocation, Vec2i currLocation) {
        super.setCurrLocation(currLocation);
        super.setMap(map);
        super.setPlayerLocation(userLocation);
    }

    // Testing code for A* search
    public static void main(String[] args) {
        int [] [] map = {{0, 0, 1, 0, 0},{0, 0, 1, 0, 0},{1, 1, 1, 0, 0},{0, 0, 0, 0, 0},{0, 0, 0, 0, 0}};
        //int [] [] map = {{0, 0, 0, 0, 0},{0, 0, 0, 0, 0},{0, 0, 0, 0, 0},{0, 0, 0, 0, 0},{0, 0, 0, 0, 0}};

        Enemy testHunter = new Hunter(map,new Vec2i(0,0), new Vec2i(4,4));
        // Print map
        for (int [] x: map) {
            for (int y: x) {
                System.out.print(Integer.toString(y) + " ");
            }
            System.out.print("\n");
        }

        testHunter.setDimensions(5,5);

        ArrayList<Vec2i> target = new ArrayList<>();
        target.add(new Vec2i(0,0));
        target.add(new Vec2i(4,0));
        // Always use an Array list
        ArrayList<Vec2i> path = testHunter.shortestPath(target);

        for (Vec2i x: path) {
            System.out.printf("(%d, %d)\n",x.getX(),x.getY());
        }
    }
}