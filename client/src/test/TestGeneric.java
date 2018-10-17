//package test;
//
//import main.Level;
//import main.maploading.MapLoader;
//import main.math.Vec2i;
//
//import java.util.ArrayList;
//
//public class TestGeneric {
//    /**
//     * Get the test data from the correct directory
//     * @param name name of file
//     * @return Loaded level
//     */
//    public static Level getMap(String name) {
//        MapLoader ML = new MapLoader();
//        return ML.loadLevel(name, "../testdata");
//    }
//
//    /**
//     * Function to test algorithm
//     * @param fileName the desired filename to load in
//     * @param targets target tile
//     * @return the path
//     */
//    public static ArrayList<Vec2i> testAlgo(
//            String fileName,
//            ArrayList<Vec2i> targets,
//            Vec2i self
//    ) {
//        Level testLevel = getMap(fileName);
//        //testLevel.displayLevel();
//        AStarSearch AlgoOp = new AStarSearch(testLevel, targets, self);
//        return AlgoOp.search();
//    }
//
//    /**
//     * @param check Requested list
//     * @return If the path passed in is a valid path
//     */
//    public static boolean validPath(ArrayList<Vec2i> check) {
//        Vec2i prev = null;
//        for (Vec2i x: check) {
//            if (prev == null) {prev = x;}
//            else {
//                Vec2i buffer = x.sub(prev);
//                if (!checkDiff(buffer)) {
//                    return false;
//                }
//                prev = x;
//            }
//        }
//        return true;
//    }
//
//    /**
//     * Check the difference of 2 vectors
//     * @param x1 Requested vector
//     * @return If the vector is a part of path
//     */
//    public static boolean checkDiff(Vec2i x1) {
//        if (Math.abs(x1.getX() + x1.getY()) == 1) { return true; }
//        return false;
//    }
//}
