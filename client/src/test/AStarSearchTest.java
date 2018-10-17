//package test;
//
//import java.util.ArrayList;
//
//import main.math.Vec2i;
//import org.junit.Test;
//
//// Note that hunter logic is trivial and not really is to be tested
//
//public class AStarSearchTest {
//    @Test
//    // Test Aggregate Manhanttan distance
//    public void testOptimal() {
//        // Generic case
//        //---------------------------------------- Test 1 -------------------------------------------//
//        ArrayList<Vec2i> targets = new ArrayList<>();
//        targets.add(new Vec2i(4, 4));
//
//        ArrayList<Vec2i> results = TestGeneric.testAlgo(
//                "TestAstar1",
//                targets,
//                new Vec2i(0,0)
//        );
//        //----------------------------- Criterias for above test -----------------------------------//
//        assert(results.size() == 9);
//        assert(TestGeneric.validPath(results));
//        assert(results.get(results.size() - 1).equals(new Vec2i(4,4)));
//
//         // Rectangular case with obstacles
//        //---------------------------------------- Test 2 -------------------------------------------//
//        ArrayList<Vec2i> targets1 = new ArrayList<>();
//        targets1.add(new Vec2i(0, 0));
//
//        ArrayList<Vec2i> results1 = TestGeneric.testAlgo(
//                "TestAstar2",
//                targets1,
//                new Vec2i(10,5)
//        );
//        //----------------------------- Criterias for above test -----------------------------------//
//        assert(TestGeneric.validPath(results1));
//        assert(results1.get(results1.size() - 1).equals(new Vec2i(0,0)));
//
//        // Same line input with all passable items
//        //---------------------------------------- Test 3 -------------------------------------------//
//        ArrayList<Vec2i> targets2 = new ArrayList<>();
//        targets2.add(new Vec2i(0, 0));
//
//        ArrayList<Vec2i> results2 = TestGeneric.testAlgo(
//                "TestAstar3",
//                targets2,
//                new Vec2i(10,0)
//        );
//        //----------------------------- Criterias for above test -----------------------------------//
//        assert(results2.size() == 11);
//        assert(TestGeneric.validPath(results2));
//        assert(results2.get(results2.size() - 1).equals(new Vec2i(0,0)));
//
//        // Miscellaneous goal type and start
//        //---------------------------------------- Test 4 -------------------------------------------//
//        ArrayList<Vec2i> targets3 = new ArrayList<>();
//        targets3.add(new Vec2i(8, 5));
//
//        ArrayList<Vec2i> results3 = TestGeneric.testAlgo(
//                "TestAstar4",
//                targets3,
//                new Vec2i(2,3)
//        );
//        //----------------------------- Criterias for above test -----------------------------------//
//        assert(TestGeneric.validPath(results3));
//        assert(results3.get(results3.size() - 1).equals(new Vec2i(8,5)));
//
//        // More constraints on map conditions
//        //---------------------------------------- Test 5 -------------------------------------------//
//        ArrayList<Vec2i> targets4 = new ArrayList<>();
//        targets4.add(new Vec2i(5, 4));
//
//        ArrayList<Vec2i> results4 = TestGeneric.testAlgo(
//                "TestAstar4",
//                targets4,
//                new Vec2i(0,0)
//        );
//        //----------------------------- Criterias for above test -----------------------------------//
//        assert(TestGeneric.validPath(results4));
//        assert(results4.get(results4.size() - 1).equals(new Vec2i(5,4)));
//    }
//
//    @Test
//    // Test that multiple targets specified can be done
//    public void testMultipleInput() {
//        // Generic case
//        //---------------------------------------- Test 1 -------------------------------------------//
//        ArrayList<Vec2i> targets = new ArrayList<>();
//        targets.add(new Vec2i(3, 0));
//        targets.add(new Vec2i(3, 1));
//        targets.add(new Vec2i(3, 2));
//
//        ArrayList<Vec2i> results = TestGeneric.testAlgo(
//                "TestAstar2",
//                targets,
//                new Vec2i(11,2)
//        );
//        //----------------------------- Criterias for above test -----------------------------------//
//
//        assert(TestGeneric.validPath(results));
//        assert(results.get(results.size() - 1).equals(new Vec2i(3,0)) ||
//                results.get(results.size() - 1).equals(new Vec2i(3,1)) ||
//                results.get(results.size() - 1).equals(new Vec2i(3,2))
//        );
//    }
//
//    @Test
//    // Test for cases where targets given to the Algorithm is not reachable
//    public void testNotReachable() {
//        // Generic case
//        //---------------------------------------- Test 1 -------------------------------------------//
//        ArrayList<Vec2i> targets = new ArrayList<>();
//        targets.add(new Vec2i(0, 0));
//
//        ArrayList<Vec2i> results = TestGeneric.testAlgo(
//                "TestAstar5",
//                targets,
//                new Vec2i(11,2)
//        );
//        //----------------------------- Criterias for above test -----------------------------------//
//        assert(TestGeneric.validPath(results));
//    }
//}