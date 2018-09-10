package main.behaviour;

import main.math.Vec2i;

import java.util.ArrayList;

public interface AIBehavior {
    public ArrayList<Vec2i> decideMove(int[] map, Vec2i currLocation, Vec2i userLocation, ArrayList<Integer> pastMoves);
}
