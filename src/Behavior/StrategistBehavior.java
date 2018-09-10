package Behavior;

import main.math.Vec2i;

import java.util.ArrayList;

public class StrategistBehavior implements AIBehavior {
    @Override
    public int[] decideMove(int[] map, Vec2i currLocation, Vec2i userLocation, ArrayList<Integer> pastMoves) {
        return new int[0];
    }
}
