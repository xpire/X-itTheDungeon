//package test;
//
//import main.behaviour.AIBehaviour;
//import main.behaviour.StrategistBehaviour;
//import main.Level;
//import main.math.Vec2i;
//import org.junit.Test;
//
//import java.util.ArrayList;
//
//public class StrategistTest {
//
//    @Test
//    // Test case 1 Generic cases
//    public void testBlank() {
//        // Generic case
//        //---------------------------------------- Test 1 -------------------------------------------//
//        AIBehaviour test = new StrategistBehaviour();
//        Level level = TestGeneric.getMap("TestStrategist1");
//         ArrayList<Vec2i> result = test.decideMove(
//                 level,
//                 new Vec2i(4,4 ),
//                 new Vec2i (0,0),
//                 new ArrayList<>(),
//                 new ArrayList<>()
//         );
//        // System.out.println(result);
//        //----------------------------- Criterias for above test -----------------------------------//
//        assert(result != null);
//    }
//
//    @Test
//    // Test case 2 more complex cases
//    public void testComplex() {
//        // More complex case
//        //---------------------------------------- Test 2 -------------------------------------------//
//        AIBehaviour test = new StrategistBehaviour();
//        Level level = TestGeneric.getMap("TestAstar4");
//        ArrayList<Vec2i> result = test.decideMove(
//                level,
//                new Vec2i(10,5 ),
//                new Vec2i (0,0),
//                new ArrayList<>(),
//                new ArrayList<>()
//        );
//        // System.out.println(result);
//        //----------------------------- Criterias for above test -----------------------------------//
//        assert(result != null);
//    }
//
//    @Test
//    // Test case 3 more complex cases
//    public void testEdge() {
//        // More edge cases
//        //---------------------------------------- Test 2 -------------------------------------------//
//        AIBehaviour test = new StrategistBehaviour();
//        Level level = TestGeneric.getMap("TestAstar4");
//        ArrayList<Integer> pastMoves = new ArrayList<>();
//        pastMoves.add(1);
//        pastMoves.add(0);
//        pastMoves.add(0);
//        pastMoves.add(3);
//        pastMoves.add(2);
//        pastMoves.add(1);
//        pastMoves.add(3);
//        pastMoves.add(1);
//        pastMoves.add(2);
//
//        ArrayList<Vec2i> result = test.decideMove(
//                level,
//                new Vec2i(10,5 ),
//                new Vec2i (0,0),
//                pastMoves,
//                new ArrayList<>()
//        );
//        // System.out.println(result);
//        //----------------------------- Criterias for above test -----------------------------------//
//        assert(result != null);
//    }
//}