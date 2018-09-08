package Behavior;

import java.util.ArrayList;

public interface AIBehavior {
    public int[] decideMove(int[] map, int[] currLocation, int[] userLocation, ArrayList<Integer> pastMoves);
}
