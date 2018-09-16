//package Test;
////
////import static org.junit.Assert.*;
////import main.entities.enemies.*;
////import main.maploading.Level;
////import main.maploading.MapLoader;
////import main.math.Vec2i;
////
////import java.util.ArrayList;
////
////public class HunterTest {
////
////    //
////    @org.junit.Test
////    public void shortestPath() {
////        MapLoader ML = new MapLoader();
////
////        Level map = ML.loadLevel("map2", "levels");
////        Enemy testHunter = new Hunter(map, new Vec2i(4,4));
////
////        System.out.println(map.getNRows() + "\t" + map.getNCols());
////
////        ArrayList<Vec2i> target = testHunter.getCurrBehaviour().decideMove(
////                map,
////                new Vec2i(4,4),
////                new Vec2i(0,0),
////                new ArrayList<>(),
////                new ArrayList<>()
////        );
////        //ArrayList<Vec2i> path = testHunter.shortestPath(target);
////
//////        for (Vec2i x: path) {
//////            System.out.printf("(%d, %d)\n",x.getX(),x.getY());
//////        }
////    }
////
//////    /**
//////     * An interface for creating a map from a text file for testing
//////     * @param name The name of a file in constant directory
//////     * @return The loaded map
//////     */
//////    private Level Mapsetter(String name) {
//////    }
////}
