package main.enemies;

import main.math.Vec2i;

public class Strategist extends Enemy {
    public Strategist(int [] [] map, Vec2i userLocation, Vec2i currLocation) {
        super.setCurrLocation(currLocation);
        super.setMap(map);
        super.setPlayerLocation(userLocation);
    }
}
