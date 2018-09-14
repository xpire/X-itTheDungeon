package main.behaviour;

import main.maploading.TileMap;
import main.math.Vec2i;

import java.util.ArrayList;

public class HunterBehaviour implements AIBehaviour {
    @Override
    public ArrayList<Vec2i> decideMove(TileMap map, Vec2i currLocation, Vec2i userLocation, ArrayList<Integer> pastMoves, ArrayList<main.entities.Entity> entities, ArrayList<Vec2i> entitiesCoord) {
        ArrayList<Vec2i> result = new ArrayList<>();
        result.add(userLocation);
        return result;
    }

}
