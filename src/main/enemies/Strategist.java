package main.enemies;

import main.behaviour.StrategistBehaviour;
import main.maploading.TileMap;
import main.math.Vec2i;

public class Strategist extends Enemy {
    public Strategist(TileMap map, Vec2i userLocation, Vec2i currLocation) {
        super.setCurrLocation(currLocation);
        super.setMap(map);
        super.setPlayerLocation(userLocation);
        super.setCurrBehavior(new StrategistBehaviour());
    }
}
