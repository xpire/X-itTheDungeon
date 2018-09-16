package Test;

import main.entities.enemies.Enemy;
import main.entities.enemies.Hunter;
import main.maploading.Level;
import main.maploading.MapLoader;
import main.math.Vec2i;
//import org.junit.Test;

import java.util.ArrayList;

//import static org.junit.Assert.*;

public class AStarSearchTest {

//    @Test
//    // Test Aggregate Manhanttan distance
//    public void testManhanttan() {
//    }
//
//    /**
//     * Get the test data from the correct directory
//     * @param name name of file
//     * @return Loaded level
//     */
//    private Level getMap(String name) {
//        MapLoader ML = new MapLoader();
//        Level map = ML.loadLevel("map2", "../Test");
//        Enemy testHunter = new Hunter(map, new Vec2i(4,4));
//
//        System.out.println(map.getNRows() + "\t" + map.getNCols());
//
//        ArrayList<Vec2i> target = testHunter.getCurrBehaviour().decideMove(
//                map,
//                new Vec2i(4,4),
//                new Vec2i(0,0),
//                new ArrayList<>(),
//                new ArrayList<>()
//        );
//        return null;
//    }
}