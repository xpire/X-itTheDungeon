package main.behaviour;

import main.entities.enemies.Enemy;
import main.maploading.Level;
import main.entities.*;
import main.math.Vec2i;
import java.util.ArrayList;


public interface AIBehaviour {
    public ArrayList<Vec2i> decideMove(Level map,
                                       Vec2i currLocation,
                                       Vec2i userLocation,
                                       ArrayList<Integer> pastMoves,
                                       ArrayList<Enemy> entities);
}
