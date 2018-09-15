package main.behaviour;

import main.math.Vec2i;
import main.entities.*;
import main.maploading.Level;
import java.util.ArrayList;

public class HunterBehaviour implements AIBehaviour {
    @Override
    public ArrayList<Vec2i> decideMove(Level map, Vec2i currLocation, Vec2i userLocation, ArrayList<Integer> pastMoves, ArrayList<Entity> entitiess) {
        ArrayList<Vec2i> result = new ArrayList<>();
        result.add(userLocation);
        return result;
    }

}
